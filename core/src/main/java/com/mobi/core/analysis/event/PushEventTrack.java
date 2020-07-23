package com.mobi.core.analysis.event;

import android.text.TextUtils;

import com.mobi.core.CoreSession;
import com.mobi.core.MobiConstantValue;
import com.mobi.core.db.use.DataManager;
import com.mobi.core.network.HttpClient;
import com.mobi.core.network.Request;
import com.mobi.core.network.RequestUtil;
import com.mobi.core.network.Response;
import com.mobi.core.network.SdkExecutors;
import com.mobi.core.utils.DeviceUtil;
import com.mobi.core.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/19 16:57
 * @Dec 略
 */
public class PushEventTrack {
    public static final String TAG = "PushEventTrack";
    public static String DEVELOPER_URL = "";

    /**
     * 然后后面上传是，通过获取 {@link CoreSession#getReportUrl()}
     */
    public static String REPORT_URL = "";


    private static volatile boolean isFirstReport = true;

    /**
     * 是否上传失败了的标志
     * 默认第一次是true的，这样第一次上报的时候
     * <p>
     * 就会去扫描本地的数据库，有数据的话，
     * 就会同时带着第一次的数据上传给服务器了
     */
    private static volatile boolean isFailureReport = true;


    public static void trackAD(int event,
                               int styleType,
                               String postId,
                               int sortType,
                               String network,
                               String md5) {

        SdkExecutors.DB_OPERATION_POOL.execute(new Runnable() {
            @Override
            public void run() {

                PushEvent pushEvent = new PushEvent(event, styleType, postId, sortType, network, md5);
                //一开始就存库里面
                //存库操作放子线程里面
                saveDb(pushEvent);

                //然后再执行上报操作
                CoreSession.get().getDispatcher().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            trackAD(pushEvent);
                        } finally {
                            CoreSession.get().getDispatcher().finishRunnable(this);
                        }
                    }
                });
            }
        });

    }

    public static void trackAD(int event,
                               int styleType,
                               String postId,
                               int sortType,
                               String network,
                               String md5,
                               int errorType,
                               int code,
                               String message,
                               String debug) {

        SdkExecutors.DB_OPERATION_POOL.execute(new Runnable() {
            @Override
            public void run() {
                PushEvent pushEvent = new PushEvent(event, styleType, postId, sortType, network, md5);
                pushEvent.setType(errorType);
                pushEvent.setCode(code);
                pushEvent.setMessage(message);
                pushEvent.setDebug(debug);
                //一开始就存库里面
                saveDb(pushEvent);

                CoreSession.get().getDispatcher().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            trackAD(pushEvent);
                        } finally {
                            CoreSession.get().getDispatcher().finishRunnable(this);
                        }
                    }
                });
            }
        });

    }

    private static synchronized void trackAD(PushEvent bean) {

        //用户启动app第一次
        //删除本地已经上传过的数据
        if (isFirstReport) {
            int count = DataManager.deletePushEventFromPushSuccess(CoreSession.get().getContext(), 1);
            LogUtils.e(TAG, "删除了成功的个数" + count);
            //进行状态改变
            isFirstReport = false;
        }

        //获取UpdateUrl
        if (!updateUrlGetSuccess()) {
            LogUtils.e(TAG, "updateUrl为空 数据存数据库里头");
            return;
        }

        if (!updateReportUrlGetSuccess()) {
            LogUtils.e(TAG, "reportUrl为空 数据存数据库里头");
            return;
        }

        if (!DeviceUtil.isNetAvailable(CoreSession.get().getContext())) {
            //网络不可用存数据库里头
            LogUtils.e(TAG, "网络不可用 数据存数据库里头");
            return;
        }

        HttpClient httpClient = new HttpClient();

        List<PushEvent> beanList = new ArrayList<>();
        beanList.add(bean);

        //读取库里面的所有未上传的数据
        List<PushEvent> dpList = DataManager.getPushEventPushSuccessList(CoreSession.get().getContext(), 0);
        if (!dpList.isEmpty()) {
            beanList.addAll(dpList);
        }

        //把数据变成json数据传给后台
        String fromBody = PushEventUtil.toPushEventJson(beanList);

        String url = isMushPushEvent(bean.getEvent()) ? REPORT_URL : DEVELOPER_URL;
        Request request = new Request.Builder()
                .setUrl(url)
                .setMethod(Request.POST)
                .setFromBody(fromBody)
                .build();

        RequestUtil.putEventHeader(request);

        Response response = httpClient.execute(request);
        if (response.getCode() == 200) {
            String body = response.body();
            try {
                JSONObject jsonObject = new JSONObject(body);
                int serverCode = jsonObject.optInt("code");
                if (serverCode == 200) {
                    LogUtils.e(TAG, "上传成功 postId : " + bean.getPostId() + " network : " + bean.getNetwork());
                    //上传成功的标志修改，用于第一次启动app去删除
                    for (PushEvent pushEvent : beanList) {
                        pushEvent.setIsPushSuccess(1);
                    }
                    //更新数据库上传的数据状态
                    DataManager.updatePushEventList(CoreSession.get().getContext(), beanList);

                } else {
                    LogUtils.e(TAG, "上传失败 code = " + serverCode + " postId : " + bean.getPostId() + " network : " + bean.getNetwork());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtils.e(TAG, "上传失败 e = " + e.getMessage());
            }
        } else {
            LogUtils.e(TAG, "上传失败 postId : " + bean.getPostId() + " network : " + bean.getNetwork());
        }
    }

    private static boolean updateUrlGetSuccess() {
        if (TextUtils.isEmpty(DEVELOPER_URL)) {
            DEVELOPER_URL = CoreSession.get().getDeveloperUrl();
            if (TextUtils.isEmpty(DEVELOPER_URL)) {
                LogUtils.e(TAG, "连接还是空的");
                return false;
            }
        }
        return true;
    }

    private static boolean updateReportUrlGetSuccess() {
        if (TextUtils.isEmpty(REPORT_URL)) {
            REPORT_URL = CoreSession.get().getReportUrl();
            if (TextUtils.isEmpty(REPORT_URL)) {
                LogUtils.e(TAG, "连接还是空的");
                return false;
            }
        }
        return true;
    }

    /**
     * 必须要上传的事件
     *
     * @param eventType
     * @return
     */
    public static boolean isMushPushEvent(int eventType) {
        return eventType == MobiConstantValue.EVENT.START
                || eventType == MobiConstantValue.EVENT.LOAD
                || eventType == MobiConstantValue.EVENT.SHOW
                || eventType == MobiConstantValue.EVENT.CLICK;
    }

    private static void saveDb(PushEvent bean) {
        if (bean != null) {
            if (isMushPushEvent(bean.getEvent())) {
                DataManager.addPushEvent(CoreSession.get().getContext(), bean);
                LogUtils.e(TAG, "save " + bean.toString());
            }
        }
    }

}

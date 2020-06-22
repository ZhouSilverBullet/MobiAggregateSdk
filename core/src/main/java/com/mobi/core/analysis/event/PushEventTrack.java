package com.mobi.core.analysis.event;

import android.text.TextUtils;

import com.mobi.core.CoreSession;
import com.mobi.core.analysis.AnalysisBean;
import com.mobi.core.analysis.AnalysisUtil;
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

    public static void trackAD(int event,
                               int styleType,
                               String postId,
                               int sortType,
                               String network) {

        SdkExecutors.SDK_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                PushEvent pushEvent = new PushEvent(event, styleType, postId, sortType, network);
                trackAD(pushEvent);
            }
        });

    }

    public static void trackAD(int event,
                               int styleType,
                               String postId,
                               int sortType,
                               String network,
                               int type,
                               int code,
                               String message,
                               String debug) {

        SdkExecutors.SDK_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                PushEvent pushEvent = new PushEvent(event, styleType, postId, sortType, network);
                pushEvent.setType(type);
                pushEvent.setCode(code);
                pushEvent.setMessage(message);
                pushEvent.setDebug(debug);
                trackAD(pushEvent);
            }
        });

    }

    private static void trackAD(PushEvent bean) {
        //获取UpdateUrl
        if (!updateUrlGetSuccess()) {
            LogUtils.e(TAG, "updateUrl为空 数据存数据库里头");
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

        //把数据变成json数据传给后台
        String fromBody = PushEventUtil.toPushEventJson(beanList);

        Request request = new Request.Builder()
                .setUrl(DEVELOPER_URL)
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
                } else {
                    LogUtils.e(TAG, "上传失败 code = " + serverCode + " postId : " + bean.getPostId() + " network : " + bean.getNetwork());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //失败了就存数据库里头
                LogUtils.e(TAG, "上传失败 e = " + e.getMessage());

            }
        } else {
            //失败了就存数据库里头
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
}

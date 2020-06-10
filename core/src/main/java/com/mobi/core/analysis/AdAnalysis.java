package com.mobi.core.analysis;

import android.text.TextUtils;

import com.mobi.core.CoreSession;
import com.mobi.core.db.use.DataManager;
import com.mobi.core.network.HttpClient;
import com.mobi.core.network.Request;
import com.mobi.core.network.Response;
import com.mobi.core.network.SdkExecutors;
import com.mobi.core.utils.DeviceUtil;
import com.mobi.core.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdAnalysis {
    public static final int STATUS_CODE_TRUE = 1;
    public static final int STATUS_CODE_FALSE = 0;

    public static final String TAG = "AdAnalysis";

    /**
     * 然后后面上传是，通过获取 {@link CoreSession#getReportUrl()}
     */
    public static String REPORT_URL = "";

    /**
     * 是否上传失败了的标志
     * 默认第一次是true的，这样第一次上报的时候
     *
     * 就会去扫描本地的数据库，有数据的话，
     * 就会同时带着第一次的数据上传给服务器了
     *
     */
    private static boolean isFailReport = true;

    /**
     * 统计广告
     */
    public static void trackAD(String network, String posid, int pv, int click) {

        SdkExecutors.SDK_THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                AnalysisBean bean = new AnalysisBean(network, posid, pv, click);
                trackAD(bean);
            }
        });

    }

    private static void trackAD(AnalysisBean bean) {
        //获取UpdateUrl
        if (!updateUrlGetSuccess()) {
            //地址不可用存数据库里头
            saveDb(bean);
            LogUtils.e(TAG, "updateUrl为空 数据存数据库里头");
            isFailReport = true;
            return;
        }

        if (!DeviceUtil.isNetAvailable(CoreSession.get().getContext())) {
            //网络不可用存数据库里头
            saveDb(bean);
            LogUtils.e(TAG, "网络不可用 数据存数据库里头");
            isFailReport = true;
            return;
        }

        HttpClient httpClient = new HttpClient();

        List<AnalysisBean> beanList = new ArrayList<>();
        if (!beanList.isEmpty()) {
            beanList.clear();
        }
        beanList.add(bean);
        //有上传失败的时候，就把库里面的也读出来
        boolean isDbDataNotEmpty = false;
        if (isFailReport) {
            //获取库里面所有的数据
            List<AnalysisBean> dpList = DataManager.getAllAnalysis(CoreSession.get().getContext());
            if (!dpList.isEmpty()) {
                beanList.addAll(dpList);
                isDbDataNotEmpty = true;
            }
        }

        //把数据变成json数据传给后台
        String fromBody = AnalysisUtil.toStatisticalJson(beanList);

        Request request = new Request.Builder()
                .setUrl(REPORT_URL)
                .setMethod(Request.POST)
                .setFromBody(fromBody)
                .build();

        Response response = httpClient.execute(request);

        //默认都算上传失败
        isFailReport = true;

        if (response.getCode() == 200) {
            String body = response.body();
            try {
                JSONObject jsonObject = new JSONObject(body);
                int serverCode = jsonObject.optInt("code");
                if (serverCode == 200) {
                    LogUtils.e(TAG, "上传成功 postId : " + bean.getPosid() + " network : " + bean.getNetwork());
                    isFailReport = false;

                    if (isDbDataNotEmpty) {
                        int index = DataManager.deleteAllAnalysis(CoreSession.get().getContext());
                        LogUtils.e(TAG, " 删除了所有数据库的数据条数：" + index);
                    }
                } else {
                    //失败了就存数据库里头
                    saveDb(bean);
                    LogUtils.e(TAG, "上传失败 code = " + serverCode + " postId : " + bean.getPosid() + " network : " + bean.getNetwork());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //失败了就存数据库里头
                saveDb(bean);
            }
        } else {
            //失败了就存数据库里头
            saveDb(bean);
            LogUtils.e(TAG, "上传失败 postId : " + bean.getPosid() + " network : " + bean.getNetwork());
        }
    }

    private static boolean updateUrlGetSuccess() {
        if (TextUtils.isEmpty(REPORT_URL)) {
            REPORT_URL = CoreSession.get().getReportUrl();
            if (TextUtils.isEmpty(REPORT_URL)) {
                LogUtils.e(TAG, "连接还是空的");
                return false;
            }
        }
        return true;
    }

    private static void saveDb(AnalysisBean bean) {
        DataManager.addAnalysis(CoreSession.get().getContext(), bean);
    }

}

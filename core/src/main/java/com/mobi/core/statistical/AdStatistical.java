package com.mobi.core.statistical;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-11-17  18:47
 */
public class AdStatistical {

    private static List<StatisticalBean> mList = new ArrayList<>();
    private static boolean isUpload = true;//是否上报数据


    public static void init(Context context, String uploadUrl, boolean isUpload) {
//        try {
//            Constants.UPLOAD_URL = uploadUrl;
//            AdStatistical.isUpload = isUpload;
//
//            String saveString = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), Constants.Statistical_ad, "");
//            List<StatisticalBean> list = null;
//            if (!TextUtils.isEmpty(saveString)) {
//                list = JSONObject.parseArray(saveString, StatisticalBean.class);
//            }
//            if (mList == null) {
//                mList = new ArrayList<>();
//            }
//            if (list != null) {
//                mList.addAll(list);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 统计广告
     */
    public static void trackAD(Context context, String network, String posid, int pv, int click) {
//        try {
//            if (!isUpload || TextUtils.isEmpty(Constants.UPLOAD_URL)) return;
//            StatisticalBean bean = new StatisticalBean();
//            bean.posid = posid;
//            bean.network = network;
//            bean.pv = pv;
//            bean.click = click;
//            if (mList == null) {
//                mList = new ArrayList<>();
//            }
//            mList.add(bean);
//            if (mList.size() % 20 == 0) {//每20条发送一次给服务器
//                upLoadData(context);
//            } else if (mList.size() % 3 == 0) {//每3条存一次本地
//                SaveToSP(context);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 上传广告统计数据
     */
    public static void upLoadData(final Context context) {
//        try {
//            if (mList != null) {
//                if (TextUtils.isEmpty(Constants.UPLOAD_URL)) return;
//                SharedPreferencesUtils.remove(context.getApplicationContext(), Constants.Statistical_ad);
//                JSONObject jb = new JSONObject();
//                jb.put("content", JSONArray.parseArray(JSON.toJSONString(mList)));
//                final String postData = jb.toJSONString();
//                mList = new ArrayList<>();
//                HttpClientUtils.post(Constants.UPLOAD_URL, postData, new HttpClientUtils.OnRequestCallBack() {
//                    @Override
//                    public void onSuccess(String json) {
//
//                    }
//
//                    @Override
//                    public void onError(String errorMsg) {
//                        List<StatisticalBean> list = new ArrayList<>();
//                        JSONObject object = JSON.parseObject(postData);
//                        list = JSONObject.parseArray(object.getJSONArray("content").toJSONString(), StatisticalBean.class);
//                        if (list != null) {
//                            mList.addAll(list);
//                            SaveToSP(context);
//                        }
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static void SaveToSP(Context context) {
//        try {
//            String saveString = JSONArray.parseArray(JSON.toJSONString(mList)).toJSONString();
//            SharedPreferencesUtils.setParam(context.getApplicationContext(), Constants.Statistical_ad, saveString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}

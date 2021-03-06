package com.mobi.core.analysis;

import com.mobi.core.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Email: zhousaito@163.com
 * Created by zhousaito 2020/6/8 20:51
 * Version: 1.0
 * Description:
 */
public class AnalysisUtil {
    private static JSONObject getStatistical(AnalysisBean bean) {
        if (bean == null) {
            return null;
        }

        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("network", bean.getNetwork());
            jsonObject.put("posid", bean.getPosid());
            jsonObject.put("pv", bean.getPv());
            jsonObject.put("click", bean.getClick());
            jsonObject.put("day", bean.getDay());
            jsonObject.put("time", bean.getTime());
            jsonObject.put("deviceid", bean.getDeviceid());
            jsonObject.put("platform", bean.getPlatform());
            jsonObject.put("sdkv", bean.getSdkv());
            jsonObject.put("channel_no", bean.getChannel_no());
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toStatisticalJson(List<AnalysisBean> beanList) {
        ArrayList<JSONObject> list = new ArrayList<>();
        for (AnalysisBean statisticalBean : beanList) {
            JSONObject statistical = getStatistical(statisticalBean);
            if (statistical != null) {
                list.add(statistical);
            }
        }
        final JSONArray jsonArray = new JSONArray(list);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", jsonArray);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<AnalysisBean> fromStatisticalJson(String strJson) {
        ArrayList<AnalysisBean> list = new ArrayList<>();
        JSONObject jsonObject = JsonUtil.string2JSONObject(strJson);
        if (jsonObject == null) {
            return list;
        }

        String content = jsonObject.optString("content");
        //new JSONArray(content).get(0)
        if (content != null) {
            final JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(content);
                for (int i = 0; i < jsonArray.length(); i++) {
//                    System.out.println(jsonArray.opt(i));
                    String beanJsonStr = jsonArray.optString(i);
                    if (beanJsonStr != null) {
                        final JSONObject beanJson = new JSONObject(beanJsonStr);
                        list.add(new AnalysisBean(beanJson.optString("network"),
                                beanJson.optString("posid"),
                                beanJson.optInt("posid"),
                                beanJson.optInt("click")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

}

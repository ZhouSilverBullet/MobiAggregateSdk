package com.mobi.core.analysis.event;

import android.text.TextUtils;

import com.mobi.core.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/18 20:39
 * @Dec 略
 */
public class PushEventUtil {
    private static JSONObject getPushEvent(PushEvent bean) {
        if (bean == null) {
            return null;
        }

        /**
         *  private int event;
         *     private String postId;
         *     private int styleType;
         *     private int sortType;
         *     private String network;
         *
         *     private String day;
         *     private String time;
         *     private long timestamp;
         *     private String bundle;
         *     private String appId;
         */
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("event", bean.getEvent());
            jsonObject.put("style_type", bean.getStyleType());
            jsonObject.put("sort_type", bean.getSortType());
            jsonObject.put("posid", bean.getPostId());
            jsonObject.put("network", bean.getNetwork());

            jsonObject.put("day", bean.getDay());
            jsonObject.put("time", bean.getTime());
            jsonObject.put("timestamp", bean.getTimestamp());
            jsonObject.put("bundle", bean.getBundle());
            jsonObject.put("appId", bean.getAppId());

            //获取的消息和debug的消息不为空，就加入下面字段
            if (!TextUtils.isEmpty(bean.getMessage())
                    || !TextUtils.isEmpty(bean.getDebug())
                    || bean.getType() != 0) {
                jsonObject.put("type", bean.getType());
                jsonObject.put("code", bean.getCode());
                jsonObject.put("message", bean.getMessage());
                jsonObject.put("debug", bean.getDebug());
            }

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toPushEventJson(List<PushEvent> beanList) {
        ArrayList<JSONObject> list = new ArrayList<>();
        for (PushEvent pushEvent : beanList) {
            JSONObject eventJsonObject = getPushEvent(pushEvent);
            if (eventJsonObject != null) {
                list.add(eventJsonObject);
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

    public static List<PushEvent> fromPushEventJson(String strJson) {
        ArrayList<PushEvent> list = new ArrayList<>();
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
                        list.add(new PushEvent(
                                beanJson.optInt("event"),
                                beanJson.optInt("style_type"),
                                beanJson.optString("posid"),
                                beanJson.optInt("sort_type"),
                                beanJson.optString("network")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}

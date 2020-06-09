package com.mobi.core.utils;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/8 22:27
 * @Dec 略
 */
public class JsonUtil {

    public static JSONObject string2JSONObject(String json) {
        JSONObject jsonObject = null;
        try {
            JSONTokener jsonParser = new JSONTokener(json);
            jsonObject = (JSONObject) jsonParser.nextValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

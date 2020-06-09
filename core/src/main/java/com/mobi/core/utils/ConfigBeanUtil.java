package com.mobi.core.utils;

import android.text.TextUtils;

import com.mobi.core.bean.AdBean;
import com.mobi.core.bean.ConfigAdBean;
import com.mobi.core.bean.ConfigBean;
import com.mobi.core.bean.ConfigItemBean;
import com.mobi.core.bean.ParameterBean;
import com.mobi.core.bean.SdkInfoItem;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/8 17:13
 * @Dec configBean 本地json解析
 */
public class ConfigBeanUtil {
    public static ConfigBean getConfigBean(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        JSONObject jsonObject = JsonUtil.string2JSONObject(jsonStr);

        if (jsonObject != null) {

            List<ConfigItemBean> configItemBeanList = getConfigItemBeanList(jsonObject);
            List<SdkInfoItem> sdk_info = getSdkInfoItem(jsonObject);
            ConfigAdBean configAdBean = getConfigAdBean(jsonObject);

            return new ConfigBean(0,
                    jsonObject.optLong("timeout"),
                    jsonObject.optLong("ad_adk_req_timeout"),
                    configItemBeanList,
                    sdk_info,
                    configAdBean);
        }

        return null;
    }

    private static ConfigAdBean getConfigAdBean(JSONObject jsonObject) {
        return new ConfigAdBean(jsonObject.optLong("timeout"),
                jsonObject.optLong("ad_adk_req_timeout"),
                jsonObject.optString("report_url"),
                jsonObject.optString("developer_url"),
                jsonObject.optString("proto_url"));
    }

    private static List<SdkInfoItem> getSdkInfoItem(JSONObject jsonObject) {
        return null;
    }

    private static List<ConfigItemBean> getConfigItemBeanList(JSONObject jsonObject) {
        JSONArray list = jsonObject.optJSONArray("list");
        //能解析到 list
        if (list == null) {
            return null;
        }
        List<ConfigItemBean> configItemBeanList = new ArrayList<>();
        for (int i = 0; i < list.length(); i++) {
            final JSONObject configItemBeanJson = list.optJSONObject(i);
            if (configItemBeanJson == null) {
                configItemBeanList.add(new ConfigItemBean("0", 0, new ArrayList<>(), new ArrayList<>()));
            } else {
                List<AdBean> networkList;
                final JSONArray network = configItemBeanJson.optJSONArray("network");
                if (network == null) {
                    networkList = new ArrayList<>();
                } else {
                    networkList = getNetworkList(network);
                }

                configItemBeanList.add(new ConfigItemBean(configItemBeanJson.optString("posid")
                        , configItemBeanJson.optInt("sort_type"), networkList, new ArrayList<>()));
            }
        }
        return configItemBeanList;
    }

    private static List<AdBean> getNetworkList(JSONArray network) {
        List<AdBean> networkList = new ArrayList<>();
        for (int j = 0; j < network.length(); j++) {
            final JSONObject adBeanObj = network.optJSONObject(j);
            AdBean adBean = null;
            if (adBeanObj != null) {
                final JSONObject parameter = adBeanObj.optJSONObject("parameter");
                ParameterBean parameterBean = null;
                if (parameter != null) {
                    parameterBean = new ParameterBean(
                            parameter.optString("appid"),
                            parameter.optString("appname"),
                            parameter.optString("posid"));
                }

                adBean = new AdBean(adBeanObj.optString("name"),
                        adBeanObj.optString("sdk"),
                        adBeanObj.optInt("order"),
                        parameterBean);
            }
            networkList.add(adBean);
        }
        return networkList;
    }
}

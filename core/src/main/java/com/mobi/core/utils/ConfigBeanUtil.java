package com.mobi.core.utils;

import android.text.TextUtils;

import com.mobi.core.bean.AdBean;
import com.mobi.core.bean.ConfBean;
import com.mobi.core.bean.ConfigAdBean;
import com.mobi.core.bean.ConfigBean;
import com.mobi.core.bean.ConfigItemBean;
import com.mobi.core.bean.ParameterBean;
import com.mobi.core.bean.SdkInfoItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
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
        JSONObject configObject = jsonObject.optJSONObject("config");
        ConfigAdBean configAdBean = null;
        if (configObject != null) {
            configAdBean = new ConfigAdBean(configObject.optLong("timeout"),
                    configObject.optLong("ad_adk_req_timeout"),
                    configObject.optString("report_url"),
                    configObject.optString("developer_url"),
                    configObject.optString("proto_url"));
        }
        return configAdBean;
    }

    public static ConfigAdBean getConfigAdBean(String jsonStr) {
        JSONObject jsonObject = JsonUtil.string2JSONObject(jsonStr);
        if (jsonObject != null) {
            return getConfigAdBean(jsonObject);
        }
        return null;
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
                configItemBeanList.add(new ConfigItemBean("0", 0, null, new ArrayList<>(), new ArrayList<>()));
            } else {
                List<AdBean> networkList;
                final JSONArray network = configItemBeanJson.optJSONArray("network");
                if (network == null) {
                    networkList = new ArrayList<>();
                } else {
                    networkList = getNetworkList(network);
                }
                List<String> sortParameterList;
                JSONArray sortParameter = configItemBeanJson.optJSONArray("sort_parameter");
                if (sortParameter == null) {
                    sortParameterList = new ArrayList<>();
                } else {
                    sortParameterList = getSortParameterList(sortParameter);
                }

                int rpErr = getRpErr(configItemBeanJson);

                configItemBeanList.add(new ConfigItemBean(configItemBeanJson.optString("posid"),
                        configItemBeanJson.optInt("sort_type"),
                        new ConfBean(rpErr),
                        networkList,
                        sortParameterList));
            }
        }
        return configItemBeanList;
    }

    /**
     * 获取控制的状态
     * @param configItemBeanJson
     * @return
     */
    private static int getRpErr(JSONObject configItemBeanJson) {
        if (configItemBeanJson != null) {
            JSONObject conf = configItemBeanJson.optJSONObject("conf");
            if (conf != null) {
                return conf.optInt("rp_err");
            }
        }

        return 0;
    }

    private static List<String> getSortParameterList(JSONArray sortParameter) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < sortParameter.length(); i++) {
            final String e = sortParameter.optString(i);
            if (!TextUtils.isEmpty(e)) {
                list.add(e);
            }
        }
        return list;
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
        //排序根据order
        Collections.sort(networkList);
        return networkList;
    }
}

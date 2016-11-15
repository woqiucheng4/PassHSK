/**
 *
 */
package com.qc.hsk.network.param;

import com.qc.corelibrary.network.RequestParamMaker;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author CreditEase/FSO
 */
public abstract class BaseRequestParamMaker implements RequestParamMaker {

    @Override
    public Map<String, String> make(Object... params) {
        Map<String, String> map = new LinkedHashMap<>();
        map.putAll(addCommonParams());
        map.putAll(addExtraParams(params));
        return map;
    }

    private Map<String, String> addCommonParams() {
//        Map<String, String> maps = new LinkedHashMap<>();
//        maps.put(NetworkConstants.PARAM_V, HTTPInterface.getVersion());
//        maps.put(NetworkConstants.PARAM_APPID, HTTPInterface.getAppid());
//        maps.put(NetworkConstants.PARAM_T, HTTPInterface.getTime());
        return null;
    }

    private Map<String, String> addExtraParams(Object... params) {
//        Map<String, String> maps = new LinkedHashMap<>();
//        maps.put(NetworkConstants.PARAM_K, NetworkEncryptUtils.getEncryptedKey());
//        String pValue = generateExtraParamString(params);
//        maps.put(NetworkConstants.PARAM_P, NetworkEncryptUtils.getEncryptedValue(pValue));
        return null;
    }

    /**
     * 生成额外参数字符串
     *
     * @param params
     * @return
     */
    protected abstract String generateExtraParamString(Object... params);

}

package com.qc.hsk.network.param;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class VersionUpdateInfoRequestParamMaker extends BaseRequestParamMaker {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.creditease.fso.crediteasemanager.network.param.BaseRequestParamMaker
	 * #generateParamString(java.lang.Object[])
	 */
	@Override
	protected String generateExtraParamString(Object... params) {
		String pValue = null;
		JSONObject jObj = new JSONObject();
		try {
//			jObj.put(NetworkConstants.PARAM_OS, (String) params[0]);
//			jObj.put(NetworkConstants.PARAM_VERSION, (String) params[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			pValue = jObj.toString();
		}
		return pValue;
	}

}
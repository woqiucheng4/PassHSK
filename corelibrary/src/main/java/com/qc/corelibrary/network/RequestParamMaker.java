/**
 *
 */
package com.qc.corelibrary.network;


import java.util.Map;

/**
 * @author CreditEase/FSO
 */
public interface RequestParamMaker {

    public Map<String, String> make(Object... params);

}

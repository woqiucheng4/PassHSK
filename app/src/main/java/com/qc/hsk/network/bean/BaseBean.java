/**
 *
 */
package com.qc.hsk.network.bean;

import java.io.Serializable;

/**
 * @author CreditEase/FSO
 */
public class BaseBean<T> implements Serializable {

    private String code;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

}

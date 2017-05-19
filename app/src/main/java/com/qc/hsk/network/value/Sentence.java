package com.qc.hsk.network.value;

import java.io.Serializable;

/**
 * <ul>
 * <li>功能职责：</li>
 * </ul>
 *
 * @author chengqiu
 * @date 2017-05-19
 */
public class Sentence implements Serializable {

    private String hanyu;

    private String hanyupinyin;

    public String getHanyu() {
        return hanyu;
    }

    public void setHanyu(String hanyu) {
        this.hanyu = hanyu;
    }

    public String getHanyupinyin() {
        return hanyupinyin;
    }

    public void setHanyupinyin(String hanyupinyin) {
        this.hanyupinyin = hanyupinyin;
    }

    @Override
    public String toString() {
        return "Sentence{" + "hanyu='" + hanyu + '\'' + ", hanyupinyin='" + hanyupinyin + '\'' + '}';
    }
}

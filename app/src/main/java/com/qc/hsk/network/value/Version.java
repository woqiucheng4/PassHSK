package com.qc.hsk.network.value;

import java.io.Serializable;

public class Version implements Serializable {

    private static final long serialVersionUID = 1345432342542L;

    private String os; //操作系统 1代表ios，2代表android

    private String version;//系统版本号

    private String description;//版本升级描述

    private String downloadUrl;//下载链接

    private int forceUpdate;//是否强制更新 0代表否，1代表是

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
}

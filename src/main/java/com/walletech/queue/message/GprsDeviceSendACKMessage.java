package com.walletech.queue.message;

public class GprsDeviceSendACKMessage extends Message {
    private String gprsId ;
    private boolean success;

    public String getGprsId() {
        return gprsId;
    }

    public void setGprsId(String gprsId) {
        this.gprsId = gprsId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

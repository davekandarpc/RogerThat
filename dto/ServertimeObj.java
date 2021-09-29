package com.rogerthat.rlvltd.com.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JOHN on 01-01-2017.
 */

public class ServertimeObj {


    /**
     * ProjectId : 900f5e54-90a5-45cb-8adc-b0e72f3c2741
     * Name : PHED-RO
     */

    @SerializedName("serverTime")
    private String serverTime;

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}

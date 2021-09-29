package com.rogerthat.rlvltd.com.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JOHN on 01-01-2017.
 */

public class ServerTimeResponse {



    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;


    @SerializedName("result")
    private List<ServertimeObj> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ServertimeObj> getResult() {
        return result;
    }

    public void setResult(List<ServertimeObj> result) {
        this.result = result;
    }
}

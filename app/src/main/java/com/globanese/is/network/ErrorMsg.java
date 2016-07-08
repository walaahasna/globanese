package com.globanese.is.network;

/**
 * Created by Hamdy on 12/5/2016.
 */
public class ErrorMsg {
    private String fieldName;
    private String message;

    public ErrorMsg() {

    }

    public ErrorMsg(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

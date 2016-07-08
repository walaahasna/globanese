package com.globanese.is.network;

import java.util.ArrayList;

/**
 * Created by Hamdy on 12/5/2016.
 */
public class ResponseObject {
    private boolean status;
    private String message;
    private Object items;
    private ArrayList<ErrorMsg> errors;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }

    public ArrayList<ErrorMsg> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<ErrorMsg> errors) {
        this.errors = errors;
    }
}

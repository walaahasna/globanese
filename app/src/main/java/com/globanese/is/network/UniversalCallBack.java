package com.globanese.is.network;

/**
 * Created by hamdy on 11/5/14.
 */
public interface UniversalCallBack {

    void onResponse(Object result);
    void onFailure(Object result);
    void onFinish();

}

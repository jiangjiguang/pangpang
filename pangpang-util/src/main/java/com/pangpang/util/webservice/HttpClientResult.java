package com.pangpang.util.webservice;

/**
 * Created by jiangjg on 2016/9/20.
 */
public class HttpClientResult {
    private int errorCode;
    private String errorMessage;
    private String content;

    public HttpClientResult() {
    }

    public HttpClientResult(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public HttpClientResult(int errorCode, String errorMessage, String content) {
        this(errorCode, errorMessage);
        this.content = content;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

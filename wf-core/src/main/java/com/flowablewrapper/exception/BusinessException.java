package com.flowablewrapper.exception;

public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = -7289238698048230824L;

    public IStatusCode statusCode;

    public BusinessException(String msg) {
        super(msg);
        this.statusCode = StatusCode.SYSTEM_ERROR;
    }

    public BusinessException(String msg, Throwable throwable) {
        super(msg, throwable);
        this.statusCode = StatusCode.SYSTEM_ERROR;
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
        this.statusCode = StatusCode.SYSTEM_ERROR;
    }

    public BusinessException(String msg, IStatusCode errorCode) {
        super(msg);
        this.statusCode = StatusCode.SYSTEM_ERROR;
        this.statusCode = errorCode;
    }

    public BusinessException(IStatusCode errorCode) {
        super(errorCode.getDesc());
        this.statusCode = StatusCode.SYSTEM_ERROR;
        this.statusCode = errorCode;
    }

    public BusinessException(IStatusCode errorCode, Throwable throwable) {
        super(errorCode.getDesc(), throwable);
        this.statusCode = StatusCode.SYSTEM_ERROR;
        this.statusCode = errorCode;
    }

    public BusinessException(String msg, IStatusCode errorCode, Throwable throwable) {
        super(errorCode.getDesc() + ":" + msg, throwable);
        this.statusCode = StatusCode.SYSTEM_ERROR;
        this.statusCode = errorCode;
    }

    public String getStatuscode() {
        return this.statusCode == null ? "" : this.statusCode.getCode();
    }

    public IStatusCode getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(IStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}

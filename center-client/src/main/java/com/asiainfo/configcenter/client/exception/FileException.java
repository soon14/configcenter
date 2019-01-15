package com.asiainfo.configcenter.client.exception;

public class FileException extends RuntimeException  {

    public FileException() {
        super();
    }

    public FileException(String msg) {
        super(msg);
    }

    public FileException(String msg, Throwable t) {
        super(msg,t);
    }

    public FileException(Throwable t) {
        super(t);
    }
}

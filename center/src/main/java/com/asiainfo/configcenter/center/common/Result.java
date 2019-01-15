/*
 *   Copyright 1999-2016 Asiainfo Technologies(China),Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.asiainfo.configcenter.center.common;

import java.io.Serializable;

/**
 * 前端请求结果封装类
 * Created by digimonster on 16/9/8.
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -5785378220885344119L;
    /**
     * 是否成功
     */
    private boolean success = true;
    /**
     * 错误代码
     */
    private String errorCode;
    private String errorMsg;
    /**
     * 业务对象
     */
    private T result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}


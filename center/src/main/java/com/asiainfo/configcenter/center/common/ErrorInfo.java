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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 错误信息统一包装类
 * Created by bawy on 18/7/3.
 */
public class ErrorInfo {

    public static String errorInfo(Throwable t) {
        StringWriter sw = new StringWriter();
        try {
            t.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        } catch(Exception e) {
            return "获取异常栈失败";
        } finally {
            try {
                sw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static Result handError(Exception e) {
        Result result = new Result();
        result.setSuccess(false);
        String head = "\\{\"?message?\"?:?[\\\\\\\\\\[|\\{]?";
        String body = ":?[\\[\"\\]?\\}?]?";
        result.setErrorMsg(e.getMessage().replaceAll(head,"").replaceAll(body,""));
        return result;
    }
}

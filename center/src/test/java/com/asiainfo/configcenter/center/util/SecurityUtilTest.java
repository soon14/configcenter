package com.asiainfo.configcenter.center.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class SecurityUtilTest {

    @Test
    public void getMD5Code() {
        String password =  "123456";
        System.out.println(SecurityUtil.getMD5Code(password));
    }
}
package com.asiainfo.configcenter.center.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 校验类单元测试
 * Created by bawy on 2018/7/17 14:45.
 */
public class ValidateUtilTest {
    @Test
    public void check() throws Exception {
        String username = "123455";
        Assert.assertEquals(false, ValidateUtil.check(ValidateUtil.CHECK_USERNAME, username));
        username = "lis";
        Assert.assertEquals(true, ValidateUtil.check(ValidateUtil.CHECK_USERNAME, username));
        username = "ba";
        Assert.assertEquals(false, ValidateUtil.check(ValidateUtil.CHECK_USERNAME, username));
        String password = "zzzziii/";
        Assert.assertEquals(false, ValidateUtil.check(ValidateUtil.CHECK_PASSWORD, password));
        password = "bawy_bawy";
        Assert.assertEquals(true, ValidateUtil.check(ValidateUtil.CHECK_PASSWORD, password));

    }

    @Test
    public void checkMail() {
        String mail = "oulc@shpso.com";
        Assert.assertTrue(ValidateUtil.checkMail(mail));

        String errorMail = "oulc@@shpso.com";
        Assert.assertFalse(ValidateUtil.checkMail(errorMail));
    }

    @Test
    public void checkPhone(){
        String phone = "17702184733";
        Assert.assertTrue(ValidateUtil.checkPhone(phone));

        String errorPhone = "01770218473";
        Assert.assertFalse(ValidateUtil.checkPhone(errorPhone));
    }

    @Test
    public void checkAppEnvName(){
        String envName = "rm";
        Assert.assertTrue(ValidateUtil.checkAppEnvName(envName));
    }

    @Test
    public void checkClzPath(){
        String data ="com.test";
        Assert.assertTrue(ValidateUtil.checkClzPath(data));
    }

}
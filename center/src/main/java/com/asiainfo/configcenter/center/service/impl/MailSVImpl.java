package com.asiainfo.configcenter.center.service.impl;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ResultCodeEnum;
import com.asiainfo.configcenter.center.entity.CcUserEntity;
import com.asiainfo.configcenter.center.service.interfaces.IMailSV;
import com.asiainfo.configcenter.center.service.interfaces.IUserSV;
import com.asiainfo.configcenter.center.util.SendEmailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by oulc on 2018/7/26.
 */
@Service
public class MailSVImpl implements IMailSV{
    @Resource
    private JavaMailSender sender;

    @Resource
    private IUserSV iUserSV;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMail(String title,String content,String ... address){
        SendEmailUtil sendEmail = new SendEmailUtil(sender, from);
        sendEmail.setTitle(title);
        sendEmail.setContent(content);
        sendEmail.setAddressList(Arrays.asList(address));
        new Thread(sendEmail).start();
    }

    @Override
    public void sendMailByUserId(int userId, String title, String content) {
        CcUserEntity ccUserEntity = iUserSV.getUserById(userId);
        Assert4CC.notNull(ccUserEntity, ResultCodeEnum.USER_NOT_EXIST,userId);
        String email = ccUserEntity.getEmail();
        Assert4CC.hasLength(email,ResultCodeEnum.USER_COMMON_ERROR,"用户没有邮件");
        sendMail(title,content,email);
    }
}

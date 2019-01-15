package com.asiainfo.configcenter.center.util;

import com.asiainfo.configcenter.center.common.ErrorInfo;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 发送邮件
 * Created by bawy on 2018/7/19 17:36.
 */
public class SendEmailUtil implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(SendEmailUtil.class);

    private JavaMailSender sender;
    private String from;
    private List<String> addressList;
    private String title;
    private String content;
    //附件 k：名字 v:内容
    private Map<String, File> attachment;

    public SendEmailUtil(JavaMailSender sender, String from){
        this.sender = sender;
        this.from = from;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public void addAddress(String address){
        if (addressList == null){
            addressList = new ArrayList<>();
        }
        addressList.add(address);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAttachment(Map<String, File> attachment) {
        this.attachment = attachment;
    }

    public void send() throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        int len = addressList.size();
        String[] addresses = new String[len];
        for (int i=0;i<len;i++){
            addresses[i] = addressList.get(i);
        }
        helper.setTo(addresses);
        helper.setSubject(title);
        helper.setText(content, true);
        if (attachment != null && !attachment.isEmpty()){
            Set<String> keySet = attachment.keySet();
            for (String key : keySet) {
                helper.addAttachment(key, attachment.get(key));
            }
        }
        sender.send(message);
    }

    @Override
    public void run() {
        try {
            this.send();
        } catch (Throwable e) {
            LOGGER.error("邮件发送失败" + ErrorInfo.errorInfo(e));
        }
    }
}

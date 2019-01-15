package com.asiainfo.configcenter.center.service.interfaces;


public interface IMailSV {
    /**
     * 发送邮件
     * @author oulc
     * @date 18-8-22 上午11:01
     * @param title 邮件标题
     * @param content 邮件内容
     * @param address 邮件地址
     */
    void sendMail(String title,String content,String ... address);

    /**
     * 根据用户主键发送邮件
     * @author oulc
     * @date 18-8-22 上午11:01
     * @param userId 用户主键
     * @param title 邮件标题
     * @param content 邮件内容
     */
    void sendMailByUserId(int userId,String title,String content);
}

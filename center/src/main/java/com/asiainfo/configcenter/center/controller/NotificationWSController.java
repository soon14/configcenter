package com.asiainfo.configcenter.center.controller;

import com.asiainfo.configcenter.center.common.Assert4CC;
import com.asiainfo.configcenter.center.common.ProjectConstants;
import com.asiainfo.configcenter.center.common.Result;
import com.asiainfo.configcenter.center.common.websocket.HttpSessionConfigurator;
import com.asiainfo.configcenter.center.service.interfaces.INotificationSV;
import com.asiainfo.configcenter.center.util.JSONUtil;
import com.asiainfo.configcenter.center.vo.user.UserInfoVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听web socket
 */
@ServerEndpoint(value = "/center/notify/websocket" , configurator = HttpSessionConfigurator.class)
@Controller
public class NotificationWSController {

    private static Logger logger = Logger.getLogger(NotificationWSController.class);


    private static INotificationSV notificationSV;
    private static RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    public NotificationWSController(INotificationSV iNotificationSV, RedisMessageListenerContainer redisMessageListenerContainer){
        NotificationWSController.notificationSV = iNotificationSV;
        NotificationWSController.redisMessageListenerContainer = redisMessageListenerContainer;
        redisMessageListenerContainer.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] bytes) {
                int userId = Integer.parseInt(new String (message.getBody()));
                try {
                    NotificationWSController.sendMessage(userId,notificationSV.getNotReadNotificationCount(userId));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        },new PatternTopic(ProjectConstants.NOTIFICATION_REDIS_KEY));
    }

    public NotificationWSController(){

    }

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private static ConcurrentHashMap<Integer,Session> contexts = new ConcurrentHashMap<>();

    /**
     * 连接成功*/
    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {
        //预校验 如果没有登陆 则断开连接
        UserInfoVO userInfoVO = checkConnect(config);
        if(userInfoVO == null){
            logger.debug("web socket连接之前没有登陆，断开连接");
            return;
        }
        setUserInfoVoToSession(session,userInfoVO);

        //保存连接信息
        contexts.put(userInfoVO.getId(),session);

        //连接初始化  用户第一次连接时把消息推送给用户
        try {
            sendMessage(session,notificationSV.getNotReadNotificationCount(userInfoVO.getId()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param session 上下文
     * @param userInfoVO 用户信息
     * @author oulc
     * @date 2018/7/20 14:25
     */
    private void setUserInfoVoToSession(Session session,UserInfoVO userInfoVO){
        session.getUserProperties().put(ProjectConstants.WEB_SOCKET_USER_KEY,userInfoVO);
    }

    /**
     * 从session中获取用户信息
     * @param session 上下文
     * @return 用户信息
     */
    private UserInfoVO getUserInfoVOFromSession(Session session){
        return (UserInfoVO)session.getUserProperties().get(ProjectConstants.WEB_SOCKET_USER_KEY);
    }

    /**
     * 检查连接
     * @param config 上下文中间容器
     * @return 用户信息
     * @author oulc
     * @date 2018/7/20 14:26
     */
    private UserInfoVO checkConnect( EndpointConfig config){
        Object httpSessionObj = config.getUserProperties().get(HttpSession.class.getName());
        if(httpSessionObj == null){
            return null;
        }else{
            HttpSession httpSession = (HttpSession)httpSessionObj;
            UserInfoVO userInfoVO =  (UserInfoVO)httpSession.getAttribute(ProjectConstants.CURRENT_USER);
            return userInfoVO;
        }
    }

    /**
     * 连接关闭调用的方法
     * @param session 上下文
     * @author oulc
     * @date 2018/7/20 14:33
     */
    @OnClose
    public void onClose(Session session) {
        UserInfoVO userInfoVO = getUserInfoVOFromSession(session);
        logger.info("("+userInfoVO.getNickname()+","+userInfoVO.getId()+")连接已经关闭");
    }

    /**
     * 收到消息
     * @param message 收到消息
     * @param session 上下文
     * @author oulc
     * @date 2018/7/20 14:33
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自浏览器的消息:" + message);
    }

    /**
     * 发生错误时调用
     * @param session 上下文
     * @param error 异常
     * @author oulc
     * @date 2018/7/20 14:32
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.debug("websocket连接异常："+ error.getMessage());
        //error.printStackTrace();
    }

    /**
     * 发送消息
     * @param userId 用户主键
     * @param data 数据
     * @throws IOException 读写异常
     */
    public static void sendMessage(int userId,Object data)throws IOException{
        Session session = contexts.get(userId);
        Assert4CC.notNull(session ,"用户:" + userId + "不在线");
        sendMessage(session,data);
    }
    /**
     * 发送消息
     * @param session 上下文
     * @param data 数据
     * @throws IOException 读写异常
     */
    public static void sendMessage(Session session,Object data)throws IOException{
        Result<Object> result = new Result();
        result.setResult(data);
        session.getBasicRemote().sendText(JSONUtil.obj2JsonStr(result));
    }

}

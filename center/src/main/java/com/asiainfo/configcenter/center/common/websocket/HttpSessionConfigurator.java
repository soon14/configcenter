package com.asiainfo.configcenter.center.common.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * 获取到httpSession后 塞进web socket session里
 */
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    /**
     * 把httpSession塞进web socket里的session
     * @param sec
     * @param request
     * @param response
     * @author oulc
     * @date 2018/7/19 16:05
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {

        Object sessionObj = request.getHttpSession();
        if(sessionObj != null){
            HttpSession httpSession = (HttpSession) sessionObj;
            sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
        }

    }


}

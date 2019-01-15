package com.asiainfo.configcenter.client.util;

import com.asiainfo.configcenter.client.constants.ProjectConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * http请求处理工具类
 * Created by chenxun on 2018/8/28.
 */
public class HTTPClientUtils {

    private static final CloseableHttpClient httpClient;
    private static final RequestConfig config;
    // 采用静态代码块，初始化超时时间配置，再根据配置生成默认httpClient对象
    static {
        config = RequestConfig.custom().setConnectTimeout(ProjectConstants.CONNECTION_TIME_OUT).
                setSocketTimeout(ProjectConstants.SOCKET_TIME_OUT).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    public HTTPClientUtils() {
    }

    /**
     * 封装HTTP GET方法
     * 无参数的Get请求
     * @param url 传入的服务器的地址
     * @return JsonObject 服务器返回的对象
     * @throws ClientProtocolException ClientProtocol异常
     * @throws IOException IO异常
     */
    public static JsonElement get(String url) throws IOException {
        //先创建一个HttpGet对象,传入目标的网络地址,然后调用HttpClient的execute()方法即可:
        HttpGet httpGet = new HttpGet();
        //设置请求URL
        httpGet.setURI(URI.create(url));
        //发起请求
        HttpResponse response = httpClient.execute(httpGet);
        JsonElement jsonElement = getHttpEntityContent(response, url);
        httpGet.abort();
        return jsonElement;
    }

    /**
     * 封装HTTP GET方法
     * 有参数的Get请求
     * @param url 传入的服务器的地址
     * @param paramMap 以map形式传进来的get参数
     * @return JsonObject 服务器返回的对象
     * @throws ClientProtocolException ClientProtocol异常
     * @throws IOException IO异常
     */
    public static JsonElement get(String url, Map<String, String> paramMap) throws IOException {
        HttpGet httpGet = new HttpGet();
        List<NameValuePair> formparams = setHttpParams(paramMap);
        String param = URLEncodedUtils.format(formparams, ProjectConstants.DEFAULT_ENCODING_TYPE);
        httpGet.setURI(URI.create(url + "?" + param));
        HttpResponse response = httpClient.execute(httpGet);
        JsonElement jsonElement = getHttpEntityContent(response, url);
        httpGet.abort();
        return jsonElement;
    }

    /**
     * 封装HTTP POST方法
     * @param url 传入的服务器的地址
     * @param obj 以对象形式传进来的post参数
     * @return JsonObject 服务器返回的对象
     * @throws ClientProtocolException ClientProtocol异常
     * @throws IOException IO异常
     */
    public static JsonElement post(String url,Object obj) throws IOException {
        String jsonStr = JSONUtil.obj2JsonStr(obj);
        StringEntity stringEntity = jsonStr2StringEntity(jsonStr);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(httpPost);
        return getHttpEntityContent(response, url);
    }

    /**
     * 将字符串转化为字符串实体
     * @param jsonStr json字符串
     * @return StringEntity 字符串实体
     */
    private static StringEntity jsonStr2StringEntity(String jsonStr) {
        StringEntity stringEntity =new StringEntity(jsonStr, ProjectConstants.DEFAULT_ENCODING_TYPE);
        stringEntity.setContentEncoding(ProjectConstants.DEFAULT_ENCODING_TYPE);
        stringEntity.setContentType(ProjectConstants.DEFAULT_MIME_TYPE);
        return stringEntity;
    }

    /**
     * 设置请求参数
     * @param paramMap 传入的map参数
     * @return List 返回List列表
     */
    private static List<NameValuePair> setHttpParams(Map<String, String> paramMap) {
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, String>> set = paramMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formParams;
    }

    /**
     * 将从服务器获取到的对象转化为json对象
     * @param response 服务端返回的对象
     * @param url 服务请求地址
     * @return JsonObject 返回json对象
     * @throws IOException IO异常
     */
    private static JsonElement getHttpEntityContent(HttpResponse response, String url) throws IOException {
        HttpEntity entity = response.getEntity();
        if(entity == null) {
            throw new RuntimeException("调用配置中心服务“"+url+"”异常");
        }else {
            String entityContent = EntityUtils.toString(entity, ProjectConstants.DEFAULT_ENCODING_TYPE);
            JsonObject entityContentObj = JSONUtil.jsonStr2JsonObject(entityContent);
            return entityContentObj.get(ProjectConstants.RETURN_RESULT_NAME);
        }
    }

}

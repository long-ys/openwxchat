package com.example.openwxchat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WChatUtil {

    //凭证有效时间，单位：秒。目前是7200秒之内的值。
    private static Integer expiresIn = 7200;


    private final static String APPID = "wx9e69*****4aadf0d";  // 你的小程序App ID
    private final static String APPSECRET = "4fc41066**********9ad68124ee39fd";  // 你的小程序App Secret

    // 存储AccessToken和其有效期的变量
    private static String accessToken;
    private static long expireTime; // 过期时间戳，单位：秒

    /**
     * 获取微信小程序的AccessToken。
     * 如果AccessToken尚未获取或已过期，则会向微信服务器发起请求获取新的AccessToken。
     *
     * @return 微信小程序AccessToken
     */
    public static String getAccessToken() {
        // 如果AccessToken为空或已过期，则重新获取
        if (accessToken == null || System.currentTimeMillis() > expireTime * 1000) {
            refreshAccessToken();
        }
        return accessToken;
    }

    /**
     * 刷新AccessToken，向微信服务器发送请求获取新的AccessToken。
     */
    private static void refreshAccessToken() {
        // 构建请求URL
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET;

        // 使用HttpClient发送POST请求
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseJson = EntityUtils.toString(response.getEntity());
            // 使用FastJSON解析JSON响应
            JSONObject jsonObject = JSONObject.parseObject(responseJson);

            // 从JSON响应中获取AccessToken
            accessToken = jsonObject.getString("access_token");
            // 计算过期时间戳
            expireTime = System.currentTimeMillis() + (expiresIn - 60) * 1000; // 提前60秒过期，避免刚好过期时的并发问题
        } catch (IOException e) {
            // 处理异常
        }
    }

    //获取scheme
    public static String generateScheme(String jump_wxa) {
        String accessToken = getAccessToken();
        // 拼接URL
        String url = "https://api.weixin.qq.com/wxa/generatescheme?access_token=" + accessToken;
        try {
            HttpClient client = HttpClients.createDefault(); // 构建一个Client
            HttpPost post = new HttpPost(url); // 构建一个Post请求
            post.setHeader("Content-Type", "application/json"); // 设置请求头为JSON

            // 创建一个 JSON 格式的请求主体
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("jump_wxa", JSON.parseObject(jump_wxa));
            StringEntity input = new StringEntity(jsonRequest.toString(), "UTF-8");
            post.setEntity(input);

            HttpResponse response = client.execute(post); // 提交POST请求
            HttpEntity result = response.getEntity(); // 拿到返回的HttpResponse的"实体"
            String content = EntityUtils.toString(result);
            // 返回JSON格式的响应字符串
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            // 返回错误信息
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}


package com.example.jingqu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WechatService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String WECHAT_API_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String APP_ID = "wx460c13d5461db9b3";
    private static final String APP_SECRET = "b28be3477542a818c479228c37bf92c1";

    /**
     * 调用微信API获取openid和session_key
     * @param code 前端传来的微信登录code
     * @return 包含openid和session_key的Map，如果失败返回null
     */
    public Map<String, Object> getWechatSession(String code) {
        try {
            String url = WECHAT_API_URL +
                    "?appid=" + APP_ID +
                    "&secret=" + APP_SECRET +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
                
                // 检查微信API返回的错误
                if (result.containsKey("errcode")) {
                    System.err.println("微信API错误: " + result.get("errmsg"));
                    return null;
                }
                
                return result;
            }
        } catch (Exception e) {
            System.err.println("调用微信API失败: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从微信API响应中获取openid
     * @param code 微信登录code
     * @return openid，如果获取失败返回null
     */
    public String getOpenid(String code) {
        Map<String, Object> sessionInfo = getWechatSession(code);
        if (sessionInfo != null && sessionInfo.containsKey("openid")) {
            return (String) sessionInfo.get("openid");
        }
        return null;
    }
}

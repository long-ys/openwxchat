package com.example.openwxchat.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.openwxchat.param.GenerateschemeParam;
import com.example.openwxchat.utils.WChatUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/wchat/api")
@RestController
public class ApiController {

    @PostMapping("/generatescheme")
    public Object generatescheme(@RequestBody GenerateschemeParam param){
        JSONObject jump_wxa_json = new JSONObject();
        jump_wxa_json.put("path",param.getPath());
        jump_wxa_json.put("query",param.getQuery());
        String scheme = WChatUtil.generateScheme(JSONObject.toJSONString(jump_wxa_json));
        return ResponseEntity.ok(scheme);
    }
}

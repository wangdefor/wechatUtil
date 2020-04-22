package org.example.controller;

import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.example.util.HttpClientCustomSSL;
import org.example.util.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName WechatController
 * @Description 主要用来做一些初始化以及更新的操做
 * @Date 2020/4/22 15:20
 * @Author wangyong
 * @Version 1.0
 **/
@Api(tags = {"微信相关证书初始化类"}, protocols = "http")
@RestController
@Slf4j
public class WechatInitController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static String WECHAT_KEY_FOR_HASH = "wechat:key:for:hash";

    private static String WECHAT_APP_ID = "WECHAT_APP_ID";

    private static String WECHAT_APP_SECRET = "WECHAT_APP_SECRET";

    private static String PASSWORD = "PASSWORD";

    private static String PUBLIC_KEY = "PUBLIC_KEY";

    @GetMapping("/get/public/key/and/save")
    @ApiOperation(value = "获取publicKey 并存入redis 同时转化为public_key_pkcs8", notes = "获取publicKey 并存入redis 同时转化为public_key_pkcs8")
    public ResponseBody<Void> getPublicKeyAndSave(@ApiParam(value = "path") @RequestParam("path") String path,
                                                  @ApiParam(value = "appId") @RequestParam("appId") String appId,
                                                  @ApiParam(value = "appSecret") @RequestParam("appSecret") String appSecret,
                                                  @ApiParam(value = "password")  @RequestParam("password") String password) throws Exception {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        Map<String, String> entries = Maps.newConcurrentMap();
        entries.put(WECHAT_APP_ID,appId);
        entries.put(WECHAT_APP_SECRET,appSecret);
        entries.put(PASSWORD,password);
        //由于拿出来的会有 横线 ， 所以就需要去掉
        String[] publicKeys = HttpClientCustomSSL.getPublicKey(appSecret,path,password,appId).replace(" ","").split("[-][-][-][-][-]");
        String publicKey =  publicKeys[2];
        entries.put(PUBLIC_KEY, HttpClientCustomSSL.convertPubToPub8(publicKey));
        hashOperations.putAll(WECHAT_KEY_FOR_HASH,entries);
        return ResponseBody.ok(null);
    }

}

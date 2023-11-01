package com.longqin.system.util;

import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * jwt工具类
 *
 */
public class JWTUtil {
    
    //wellsun_wssp字符串的MD5加密
    private static final String SECRET = "544D2860ABB8857AAEFA4BF4626E83C5";

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**获取token包含的信息（主要为当前账号信息）
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String,Claim> getJWTClaim(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaims();
        } catch (JWTDecodeException e) {
        	System.out.println("token失效请重新登录");
        	return null;
           // throw new WsspException(ResponseEnum.ERROR.getCode(), "token失效请重新登录");
        }
    }
}

package net.qixiaowei.integration.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.TokenConstants;
import net.qixiaowei.integration.common.text.Convert;

import java.util.Map;

/**
 * Jwt工具类
 */
public class JwtUtils {
    public static String secret = TokenConstants.SECRET;

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserKey(Claims claims) {
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据令牌获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserId(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据身份信息获取用户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getUserId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USER_ID);
    }

    /**
     * 根据令牌获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUserAccount(String token) {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.DETAILS_USER_ACCOUNT);
    }

    /**
     * 根据身份信息获取用户名
     *
     * @param claims 身份信息
     * @return 用户名
     */
    public static String getUserAccount(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_USER_ACCOUNT);
    }

    /**
     * 根据身份信息获取用户销售云的token
     *
     * @param claims 身份信息
     * @return 销售云token
     */
    public static String getUserSalesToken(Claims claims) {
        return getValue(claims, SecurityConstants.SALES_TOKEN_NAME);
    }

    /**
     * 根据身份信息获取租户ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getTenantId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_TENANT_ID);
    }

    /**
     * 根据身份信息获取员工ID
     *
     * @param claims 身份信息
     * @return 用户ID
     */
    public static String getEmployeeId(Claims claims) {
        return getValue(claims, SecurityConstants.DETAILS_EMPLOYEE_ID);
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key    键
     * @return 值
     */
    public static String getValue(Claims claims, String key) {
        return Convert.toStr(claims.get(key), "");
    }
}

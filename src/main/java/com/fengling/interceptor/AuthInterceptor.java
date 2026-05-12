package com.fengling.interceptor;

import com.fengling.common.constant.CacheConstants;
import com.fengling.common.constant.ResultCodeEnum;
import com.fengling.common.context.AuthUserInfo;
import com.fengling.common.context.UserContext;
import com.fengling.common.exception.BusinessException;
import com.fengling.common.util.JWTUtil;
import com.fengling.common.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行跨域预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 获取token
        String token = request.getHeader("Authorization");

        // token为空
        if (token == null || token.isBlank()) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }

        //如果不以'Bearer '抛出异常
        if (!token.startsWith("Bearer ")) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }

        token = token.substring(7);
        // 截取后的token是空
        if (token.isBlank()) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        // 判断token是否合法
        if (!jwtUtil.validateJwtToken(token)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        // 解析token
        AuthUserInfo authUserInfo = jwtUtil.parseJWT(token);
        Long userId = authUserInfo.getUserId();
        String redisToken = redisUtil.getValueForKey(CacheConstants.AUTH_TOKEN + userId);
        // Redis中是否有这个token
        if (redisToken == null || redisToken.isBlank() || !token.equals(redisToken)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }

        UserContext.setAuthUserInfo(authUserInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContext.remove();
    }
}

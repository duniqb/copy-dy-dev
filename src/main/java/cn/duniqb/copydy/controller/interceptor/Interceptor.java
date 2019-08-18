package cn.duniqb.copydy.controller.interceptor;

import cn.duniqb.copydy.common.utils.JSONResult;
import cn.duniqb.copydy.common.utils.JsonUtils;
import cn.duniqb.copydy.common.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import static cn.duniqb.copydy.controller.BasicController.USER_REDIS_SESSION;

public class Interceptor implements HandlerInterceptor {
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
            String uniqueToken = redisOperator.get(USER_REDIS_SESSION + ":" + userId);
            if (StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)) {
                System.out.println("请登录...");
                returnErrorResponse(response, new JSONResult().errorTokenMsg("请登录"));
                return false;
            } else {
                if (!uniqueToken.equals(userToken)) {
                    System.out.println("账号被挤出...");
                    returnErrorResponse(response, new JSONResult().errorTokenMsg("账号在别处登录"));
                    return false;
                }
            }
        } else {
            System.out.println("请登录...");
            returnErrorResponse(response, new JSONResult().errorTokenMsg("请登录"));
            return false;
        }
        return true;
    }

    public void returnErrorResponse(HttpServletResponse response, JSONResult result)
            throws IOException, UnsupportedEncodingException {
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

package cn.duniqb.copydy.controller;


import cn.duniqb.copydy.common.utils.RedisOperator;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {
    @Autowired
    protected RedisOperator redisOperator;

    public static final String USER_REDIS_SESSION = "user-redis-session";
}

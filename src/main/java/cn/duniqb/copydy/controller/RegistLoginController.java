package cn.duniqb.copydy.controller;


import cn.duniqb.copydy.common.utils.JSONResult;
import cn.duniqb.copydy.common.utils.MD5Utils;
import cn.duniqb.copydy.model.Users;
import cn.duniqb.copydy.model.UsersVO;
import cn.duniqb.copydy.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 登录和注册
 */
@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录的 controller"})
public class RegistLoginController extends BasicController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户注册", notes = "用户注册的接口")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody Users user) throws Exception {

        // 判断用户名和密码必须不为空
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return JSONResult.errorMsg("用户名和密码不能为空");
        }

        // 判断用户名是否存在
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());

        // 保存用户，注册信息
        if (!usernameIsExist) {
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } else {
            return JSONResult.errorMsg("用户名已经存在，请换一个再试");
        }
        // 注册成功后返回给前端用户对象，但把密码去敏
        user.setPassword("");

        // 给用户加入 token 并放入 redis 里，并返回 vo 对象
        UsersVO usersVO = setUserRedisSessionToken(user);

        // 返回 usersVO 给前端
        return JSONResult.ok(usersVO);
    }

    /**
     * 给用户加入 token 并放入 redis 里，并返回 vo 对象
     *
     * @param userModel
     * @return
     */
    public UsersVO setUserRedisSessionToken(Users userModel) {
        // 生成唯一 token 并放入 redis 里
        String uniqueToken = UUID.randomUUID().toString();
        redisOperator.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);
        // 把 user 对象加入到 VO
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userModel, usersVO);
        usersVO.setUserToken(uniqueToken);
        return usersVO;
    }


    /**
     * 用户登录
     *
     * @param user
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户登录", notes = "用户登录的接口")
    @PostMapping("/login")
    public JSONResult login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();

        Thread.sleep(3000);

        // 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.ok("用户名或密码不能为空!");
        }

        // 判断用户是否存在
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(user.getPassword()));

        // 返回
        if (userResult != null) {
            // 密码脱敏
            userResult.setPassword("");
            UsersVO userVO = setUserRedisSessionToken(userResult);
            return JSONResult.ok(userVO);
        } else {
            return JSONResult.errorMsg("用户名或密码不正确, 请重试!");
        }
    }

    /**
     * 注销，删除 redis 里的相应 key
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户注销", notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true,
            dataType = "String", paramType = "query")
    @PostMapping("/logout")
    public JSONResult logout(String userId) {
        redisOperator.del(USER_REDIS_SESSION + ":" + userId);
        return JSONResult.ok();
    }
}
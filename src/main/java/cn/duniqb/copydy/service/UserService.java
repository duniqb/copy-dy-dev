package cn.duniqb.copydy.service;


import cn.duniqb.copydy.model.Users;
import org.springframework.stereotype.Service;


public interface UserService {

    /**
     * 用户名是否存在
     *
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 用户登录，根据用户名和密码查询用户
     */
    Users queryUserForLogin(String username, String password);

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUser(Users user);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateUserInfo(Users user);

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);
}

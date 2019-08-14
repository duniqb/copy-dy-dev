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
     * 保存用户
     *
     * @param user
     */
    void saveUser(Users user);
}

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

    /**
     * 查询用户是否喜欢点赞视频
     *
     * @param userId
     * @param videoId
     * @return
     */
    boolean isUserLikeVideo(String userId, String videoId);

    /**
     * 增加用户和粉丝的关系
     *
     * @param userId
     * @param fanId
     */
    void saveUserFanRelation(String userId, String fanId);

    /**
     * 删除用户和粉丝的关系
     *
     * @param userId
     * @param fanId
     */
    void deleteUserFanRelation(String userId, String fanId);

    /**
     * 是否已经关注
     *
     * @param userId
     * @param fanId
     */
    boolean queryIsFollow(String userId, String fanId);
}

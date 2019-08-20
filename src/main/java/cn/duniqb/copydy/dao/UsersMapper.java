package cn.duniqb.copydy.dao;

import cn.duniqb.copydy.common.utils.MyMapper;
import cn.duniqb.copydy.model.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UsersMapper extends MyMapper<Users> {

    /**
     * 用户受喜欢数累加
     *
     * @param userId
     */
    void addReceiveLikeCount(String userId);

    /**
     * 用户受喜欢数累减
     *
     * @param userId
     */
    void reduceReceiveLikeCount(String userId);

    /**
     * 增加粉丝数
     *
     * @param userId
     */
    void addFansCount(String userId);

    /**
     * 减少粉丝数
     *
     * @param userId
     */
    void reduceFansCount(String userId);

    /**
     * 增加关注数
     *
     * @param userId
     */
    void addFollowerCount(String userId);

    /**
     * 减少关注数
     *
     * @param userId
     */
    void reduceFollowerCount(String userId);
}
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
}
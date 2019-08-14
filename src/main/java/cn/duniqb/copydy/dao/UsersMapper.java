package cn.duniqb.copydy.dao;

import cn.duniqb.copydy.model.Users;
import cn.duniqb.copydy.common.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Mapper
@Component
public interface UsersMapper extends MyMapper<Users> {
}
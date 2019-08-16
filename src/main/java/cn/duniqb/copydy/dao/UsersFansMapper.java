package cn.duniqb.copydy.dao;

import cn.duniqb.copydy.model.UsersFans;
import cn.duniqb.copydy.common.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UsersFansMapper extends MyMapper<UsersFans> {
}
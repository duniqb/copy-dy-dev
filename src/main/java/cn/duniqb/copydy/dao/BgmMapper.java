package cn.duniqb.copydy.dao;

import cn.duniqb.copydy.model.Bgm;
import cn.duniqb.copydy.common.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BgmMapper extends MyMapper<Bgm> {
}
package cn.duniqb.copydy.dao;

import cn.duniqb.copydy.model.UsersReport;
import cn.duniqb.copydy.common.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UsersReportMapper extends MyMapper<UsersReport> {
}
package cn.duniqb.copydy.service;


import cn.duniqb.copydy.model.Bgm;
import cn.duniqb.copydy.model.Users;

import java.util.List;


public interface BgmService {

    /**
     * 查询背景音乐列表
     *
     * @return
     */
    List<Bgm> queryBgmList();


}

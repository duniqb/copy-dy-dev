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

    /**
     * 根据 id 查询 bgm 信息
     *
     * @param bgmId
     * @return
     */
    Bgm queryBgmById(String bgmId);

}

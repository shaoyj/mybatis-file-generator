package com.mylomen.mybatis.strategy;

import com.mylomen.mybatis.domain.XxqMybatisBO;

/**
 * @author: Shaoyongjun
 * @date: 2021/3/26
 * @time: 10:42 上午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public interface ParseTabInfoListStrategy {

    /**
     * 解析 TabInfoList
     *
     * @param dbBaseInfo
     * @return
     */
    XxqMybatisBO parseTabInfoList(XxqMybatisBO dbBaseInfo);
}

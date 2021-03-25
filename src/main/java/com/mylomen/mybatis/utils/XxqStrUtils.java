package com.mylomen.mybatis.utils;

import com.google.common.base.CaseFormat;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/3
 * @time: 2:24 下午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public class XxqStrUtils {

    /**
     * 将下划线分隔的 转成 驼峰
     *
     * @param str
     * @return
     */
    static String underscoreToLowerCamel(String str) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }


}

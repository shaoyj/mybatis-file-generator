package com.mylomen.mybatis.utils;

import java.io.File;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/17
 * @time: 9:57 上午
 * @copyright
 */
public class XxqFileUtils {


    static File generateFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            File parent = file.getParentFile();
            //如果目录不存在 则生成目录
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
        }

        return file;
    }
}

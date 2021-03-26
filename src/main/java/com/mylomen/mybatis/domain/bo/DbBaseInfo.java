package com.mylomen.mybatis.domain.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author: Shaoyongjun
 * @date: 2021/3/25
 * @time: 8:20 下午
 * @copyright
 */
@Builder
@Setter
@Getter
public class DbBaseInfo implements Serializable {
    /**
     * 默认 com.mysql.jdbc.Driver
     */
    private String driver;

    private String dbUrl;

    private String userName;

    private String password;

    private String table;

}

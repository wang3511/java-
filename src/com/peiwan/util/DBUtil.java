package com.peiwan.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库工具类:负责加载驱动、获取连接
 * 如果连接失败,请检查下面的 URL / USER / PASS 是否与你本机 MySQL 一致
 */
public class DBUtil {
    private static final String URL =
            "jdbc:mysql://localhost:3306/peiwan?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到 MySQL 驱动,请把 mysql-connector-j jar 放到 WEB-INF/lib", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

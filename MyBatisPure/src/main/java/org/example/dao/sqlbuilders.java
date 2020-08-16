package org.example.dao;

import org.apache.ibatis.jdbc.SQL;

public class sqlbuilders {
    public static void main(String[] args) {
        {
            //查询
            String sql = new SQL()
                    .SELECT("id", "name")
                    .FROM("user")
                    .WHERE("id = 1")
                    .toString();
            System.out.println(sql);
        }
        {
            //插入
            String sql = new SQL()
                    .INSERT_INTO("user")
                    .VALUES("id", "#{id}")
                    .VALUES("name", "#{name}")
                    .toString();
            System.out.println(sql);
        }
        {
            //更新
            String sql = new SQL()
                    .UPDATE("user")
                    .SET("name = aaa")
                    .WHERE("id = 1")
                    .toString();
            System.out.println(sql);
        }
        {
            //删除
            String sql = new SQL()
                    .DELETE_FROM("user")
                    .WHERE("id = 1")
                    .toString();
            System.out.println(sql);
        }
    }
}

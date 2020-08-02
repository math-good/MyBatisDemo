package org.example.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.dao.bean.User;
import org.example.dao.bean.UserWithStore;

import java.util.List;
import java.util.Map;

public interface UserDao {
    List<User> getUserList();

    List<User> getUserByPage();

    List<User> getUserByPage1(@Param("pageNum") int pageNum,
                              @Param("pageSize") int pageSize);

    /**
     * 使用注解方式写SQL
     *
     * @return
     */
    @Select("select * from user limit 5")
    List<User> getTopUserList();

    User findById(long userId);

    List<Map<String, Object>> findUserMap();

    void addUser(User user);

    int updateUser(User user);

    long deleteUser(long userId);

    UserWithStore getUserAndStoresById(long userId);
}

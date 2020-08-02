package org.example.dao;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.example.dao.bean.ItemGeneral;
import org.example.dao.bean.Store;
import org.example.dao.bean.User;
import org.example.dao.bean.UserWithStore;
import org.example.dao.mapper.ItemDao;
import org.example.dao.mapper.StoreDao;
import org.example.dao.mapper.UserDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class MyBatisPure {
    public static void main(String[] args) throws IOException {
        String config = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(config);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        boolean skip = true;

        if (!skip) {
            //各种查询
            {
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                List<User> userList = userDao.getUserList();
                System.out.println(userList);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            //清空缓存
            sqlSession.clearCache();
            {
                List<User> userList = sqlSession.selectList("org.example.dao.mapper.UserDao.getUserList");
                System.out.println(userList);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            //清空缓存
            sqlSession.clearCache();
            {
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                List<User> userList = userDao.getTopUserList();
                System.out.println(userList);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            {
                //返回map
                PageHelper.startPage(3, 5);
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                List<Map<String, Object>> userMap = userDao.findUserMap();
                System.out.println("返回map" + userMap);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            {
                //根据id查询
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                User user = userDao.findById(302001734786486273L);
                System.out.println(user);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            {
                //分页查询1
                List<User> userList = sqlSession.selectList("org.example.dao.mapper.UserDao.getUserByPage", null, new RowBounds(1, 2));
                System.out.println(userList);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            {
                //分页查询2
                PageHelper.startPage(3, 10);
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                List<User> userList = userDao.getUserByPage();
                System.out.println(userList);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            {
                //分页查询3
                PageHelper.offsetPage(4, 10);
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                List<User> userList = userDao.getUserByPage();
                System.out.println(userList);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            {
                //分页查询4
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                List<User> userList = userDao.getUserByPage1(5, 10);
                System.out.println(userList);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            //添加
            {
                //插入3个用户
                for (int i = 0; i < 3; i++) {
                    UserDao userDao = sqlSession.getMapper(UserDao.class);
                    User user = User.builder().status(i).type(i + 1).is_deleted(0).last_modify_user_id(0).create_user_id(0).build();
                    userDao.addUser(user);
                    sqlSession.commit();
                    System.out.println("新插入用户id：" + user.getUser_id());
                }
            }
            System.out.println("-------------------------------------------------------------------------------------");
            {
                //更新
                long userId = 302022858920433485L;
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                User user = userDao.findById(userId);
                System.out.println("更新前用户：" + user);

                //调换字段值
                user.setType(user.getStatus());
                user.setStatus(user.getType());
                userDao.updateUser(user);
                sqlSession.commit();

                user = userDao.findById(userId);
                System.out.println("更新后用户：" + user);
            }
            System.out.println("-------------------------------------------------------------------------------------");


            {
                //关联查询 一个store中包含一个user
                StoreDao storeDao = sqlSession.getMapper(StoreDao.class);
                Store store = storeDao.getById(302001734786489473L);
                System.out.println(store);
            }

            {
                //关联查询 一个user中包含多个store
                UserDao userDao = sqlSession.getMapper(UserDao.class);
                UserWithStore user = userDao.getUserAndStoresById(302001734786488673L);
                System.out.println(user);
            }
        }
        {
            //动态sql
            ItemDao itemDao = sqlSession.getMapper(ItemDao.class);
            ItemGeneral byId = itemDao.getByIdOrName(302001734786489696L, null);
            System.out.println(byId);
            ItemGeneral byIdAndName = itemDao.getByIdOrName(302001734786489697L, "DCAYMOKUTJYPYLZXCEML");
            System.out.println(byIdAndName);
        }
        sqlSession.close();
    }
}

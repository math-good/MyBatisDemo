# MyBatis实例

> 文档：https://mybatis.org/mybatis-3/zh/index.html
>
> 测试数据使用https://github.com/wuda0112/mysql-tester创建

## 核心类

SqlSessionFactoryBuilder

SqlSessionFactory

SqlSession

> SqlSession不是线程安全  需要用完就关闭

构建SqlSessionFactory

方式1

```java
String resource = "org/mybatis/example/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
```

方式2

```java
DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
TransactionFactory transactionFactory = new JdbcTransactionFactory();
Environment environment = new Environment("development", transactionFactory, dataSource);
Configuration configuration = new Configuration(environment);
configuration.addMapper(BlogMapper.class);
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
```

获取SqlSession
```java
try (SqlSession session = sqlSessionFactory.openSession()) {
  Blog blog = (Blog) session.selectOne("org.mybatis.example.BlogMapper.selectBlog", 101);
}
```

#### 流程

Resources获取加载全局配置

实例化SqlSessionFactoryBuilder构造器

解析xml配置文件

Configuration的所有配置

SqlSessionFactory实例化

打开SqlSession

创建executor

执行SQL

提交事务

#### mybatis-config.xml

> 文档：https://mybatis.org/mybatis-3/zh/configuration.html

打印sql日志

```xml
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING" />
    </settings>
```

#### Mapper.xml

> 文档：https://mybatis.org/mybatis-3/zh/sqlmap-xml.html

namespace：java接口的完全限定名

id：接口中的方法

resultType：返回值的类型

parameterType：参数类型

别名：

```xml
通过完全限定名设置别名
        <typeAlias type="org.example.dao.bean.User" alias="MyUser"/>

通过包设置别名，别名为包下所有类名的小写
        <package name="org.example.dao.bean" />
```

结果集映射：

>解决bean中字段名称和数据库中列名不一致问题

```xml
<resultMap id="userResultMap" type="User">
  <id property="id" column="user_id" />
  <result property="username" column="user_name"/>
  <result property="password" column="hashed_password"/>
</resultMap>
```

结果集嵌套：

关联查询：
> teacher中包含user信息
```xml
    <select id="getById" resultMap="StoreWithUser">
        select * from store where store_id = #{id}
    </select>

    <select id="getUser" resultType="User">
        select * from user where user_id = #{id}
    </select>

    <resultMap id="StoreWithUser" type="Store">
        <result property="userId" column="user_id"/>
        <result property="storeId" column="store_id"/>
        <association property="user" column="user_id" javaType="User" select="getUser"/>
    </resultMap>
```

> user中包含多个store信息
```xml
    <select id="getUserAndStoresById" resultMap="UserAndStores">
        SELECT u.user_id as user_id,u.type as user_type,u.status as user_status,u.create_time as user_create_time,
        s.store_id as store_id,s.type as store_type,s.status as store_status
         FROM user u,store s WHERE u.user_id = #{userId} and u.user_id = s.user_id
    </select>
    <resultMap id="UserAndStores" type="UserWithStore">
        <result property="type" column="user_type"/>
        <result property="status" column="user_status"/>
        <result property="create_time" column="user_create_time"/>
        <collection property="storeList" ofType="Store">
            <result property="type" column="store_type"/>
            <result property="status" column="store_status"/>
            <result property="storeId" column="store_id"/>
        </collection>
    </resultMap>
```

javaType: 实体类属性类型

ofType:泛型中的约束类型

#### 查询

1. xml配置方式

```xml
    <select id="getUserList" resultType="org.example.dao.bean.User">
        SELECT * FROM user limit 10
    </select>
```

2. 注解方式

```java
    @Select("select * from user limit 5")
    List<User> getTopUserList();
```

#### 模糊查询

> 避免SQL注入
```xml
<select id="getUserByName" result="org.example.dao.bean.User">
    select * from user where name like "%"#{name}"%"
</select>
```

>MySQL执行SQL预处理避免SQL注入

```sql
-- 执行sql预处理，将sql语句转换为test1
prepare test1 from 'select sqrt(pow(?,2) + pow(?,2)) as result';

set @x=3;
set @y=4;
-- 调用sql预处理结果
execute test1 using @x,@y;
-- 删除预处理test1
deallocate prepare test1;
```


#### 分页查询

```xml
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.2.0</version>
        </dependency>
```

1. 
```java
List<User> userList = sqlSession.selectList("org.example.dao.mapper.UserDao.getUserByPage", null, new RowBounds(1, 2));
```
2. 
```java
PageHelper.startPage(3, 10);
```
3.
```java
PageHelper.offsetPage(4, 10);
```
4.
```java
    List<User> getUserByPage1(@Param("pageNum") int pageNum,
                             @Param("pageSize") int pageSize);
```

#### 新增

```xml
    <insert id="addUser" parameterType="org.example.dao.bean.User" useGeneratedKeys="true" keyProperty="user_id">
        INSERT INTO `mysql_tester`.`user`
        (`type`,
        `status`,
        `create_user_id`,
        `last_modify_user_id`,
        `is_deleted`)
        VALUES
        (#{type},
        #{status},
        #{create_user_id},
        #{last_modify_user_id},
        #{is_deleted})
    </insert>
```

插入用户返回自增id：

useGeneratedKeys="true" 设置为自增主键

keyProperty="user_id" 设置自增后的主键赋值给bean中的user_id字段

#### 修改

```xml
UPDATE user SET type = #{type}, status = #{status} WHERE user_id = #{user_id}
```

#### 删除

//TODO

#### 动态SQL

> 根据不同的条件生成SQL语句

if
```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <if test="title != null">
    AND title like #{title}
  </if>
  <if test="author != null and author.name != null">
    AND author_name like #{author.name}
  </if>
</select>
```

choose when otherwise
>类似于java中的switch/case/default
```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <when test="author != null and author.name != null">
      AND author_name like #{author.name}
    </when>
    <otherwise>
      AND featured = 1
    </otherwise>
  </choose>
</select>
```

where
>自动移除开头的AND/OR，
```xml
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG
  <where>
    <if test="state != null">
         state = #{state}
    </if>
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
```

set
>自动移除后面的逗号
```xml
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>
```

foreach
>集合遍历
```xml
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  WHERE ID in
  <foreach item="item" index="index" collection="list"
      open="(" separator="," close=")">
        #{item}
  </foreach>
</select>
```

script
>在注解中使用动态SQL
```java
    @Update({"<script>",
      "update Author",
      "  <set>",
      "    <if test='username != null'>username=#{username},</if>",
      "    <if test='password != null'>password=#{password},</if>",
      "    <if test='email != null'>email=#{email},</if>",
      "    <if test='bio != null'>bio=#{bio}</if>",
      "  </set>",
      "where id=#{id}",
      "</script>"})
    void updateAuthorValues(Author author);
```

#### 缓存

>默认之启用了一级缓存，要启用二级缓存需要在SQL映射文件中配置

1. 一级缓存(会话缓存)

默认启用

```xml
        <setting name="cacheEnabled" value="true"/>
```

只有会话关闭或提交时才会写到二级缓存

2. 二级缓存(全局缓存)

启用：

```xml
<cache
  eviction="FIFO"
  flushInterval="60000"
  size="512"
  readOnly="true"/>
```

#### 生成器

http://mybatis.org/generator/

#### MyBatisPlus


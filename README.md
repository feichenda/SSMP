# SSMP

#### 介绍

这是基于SSM框架修改的SSMP框架，把SSM框架中的MyBatis换成MyBatis-Plus，方便操作数据库，程序猿无需再手写SQL语句。

#### 软件架构

首先在java目录下新建controller，dao，service，entity这几个包

#### 安装教程

1. databasesource.properties（这里以MySql8.0为例）

```properties
#数据库连接驱动
jdbc.driverClassName=com.mysql.cj.jdbc.Driver
#数据库连接地址
jdbc.url=jdbc:mysql://127.0.0.1:3306/mydb?useSSL=true&characterEncoding=utf8&serverTimezone=GMT
#数据库登录名
jdbc.username=root
#数据库登录密码
jdbc.password=123456
```

2. applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <!--引入数据库配置文件-->
    <context:property-placeholder location="classpath:databasesource.properties"/>
    <bean id="datasource" class="org.apache.commons.dbcp2.BasicDataSource">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
        <property name="maxTotal" value="30"/>
        <property name="maxIdle" value="10"/>
        <property name="initialSize" value="5"/>
    </bean>

    <!--配置数据库注解事务-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="datasource"/>
    </bean>

    <!--开启数据库注解事务-->
    <tx:annotation-driven transaction-manager="txManager"/>

    <bean id="countSqlParser"
          class="com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize">
        <!-- 设置为 true 可以优化部分 left join 的sql -->
        <property name="optimizeJoin" value="true"/>
    </bean>

    <!--这里配置mybatis-plus-->
    <bean id="sqlSessionFactory" class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean">
        <property name="dataSource" ref="datasource"/>
        <property name="mapperLocations" value="classpath:mapper/*.xml"/>
        <property name="globalConfig" ref="globalConfig"/> <!--  非必须  -->
        <property name="configuration" ref="configuration"/> <!--  非必须  -->
        <property name="plugins"> <!--  开启分页插件  -->
            <array>
                <bean class="com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor">
                    <!--                    <property name="sqlParser" ref="自定义解析类、可以没有"/>-->
                    <!--                    <property name="dialectClazz" value="自定义方言类、可以没有"/>-->
                    <!--                    &lt;!&ndash; COUNT SQL 解析.可以没有 &ndash;&gt;-->
                    <property name="countSqlParser" ref="countSqlParser"/>
                </bean>
            </array>
        </property>
    </bean>

    <bean id="configuration" class="com.baomidou.mybatisplus.core.MybatisConfiguration">
        <property name="mapUnderscoreToCamelCase" value="false"/><!-- 关闭Mybatis-plus驼峰命名-->
    </bean>

    <bean id="globalConfig" class="com.baomidou.mybatisplus.core.config.GlobalConfig">
        <property name="dbConfig" ref="dbConfig"/> <!--  非必须  -->
    </bean>

    <bean id="dbConfig" class="com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig">
        <property name="idType" value="AUTO"/><!-- 指定主键自动增长类型 -->
        <property name="tableUnderline" value="false"/><!-- 关闭Mybatis-plus驼峰命名-->
    </bean>
    <!--配置mybatis-plus结束-->

    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.lenovo.feizai.ssmp.dao"/><!--这里配置持久层，这里换成你的持久层路径-->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    </bean>

    <!--这里配置扫描服务层，这里换成你的服务层路径-->
    <context:component-scan base-package="com.lenovo.feizai.ssmp.service"/>

</beans>
```

3. springmvc-servlet.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--这里配置扫描控制层，这里换成你的控制层路径-->
    <context:component-scan base-package="com.lenovo.feizai.ssmp.controller"/>

    <!--自定义消息转换器的编码,解决后台传输json回前台时，中文乱码问题-->
    <mvc:annotation-driven>
        <mvc:message-converters>
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <property name="supportedMediaTypes">
                    <list>
                        <!--这就是以前的response.setContextType("text/html;charset=utf-8")-->
                        <value>text/html;charset=utf-8</value>
                    </list>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>

    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="internalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```  

4. web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>


    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--            加载springmvc.xml-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

    <!--处理前端请求数据中的中文-->
    <filter>
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

</web-app>
```

### 结束xml文件配置

### 使用说明

1. controller

```java

@CrossOrigin      //配置跨域问题
@RestController   //控制层注解
public class UserController {

}
```

2. dao

```java
   //继承BaseMapper<T>,T为对应的数据库实体类
public interface UserDao extends BaseMapper<User> {
}
```

3. entity

```java
//使用lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")//这里是数据库的表名，一定不能省略
public class User {
    //字段名
    //get，set方法
    //tostring方法
    //构造方法
    //这里可以使用lombok注解
}
```

4. servicedao
```java
//继承IService<T>,T为对应的数据库实体类
public interface UserServiceDao extends IService<User> {
}
```

5.service
```java
//继承ServiceImpl<M,T>,M为对应的DAO口,T为对应的数据库实体类
//同时实现ServiceDao
@Service
public class UserService extends ServiceImpl<UserDao, User> implements UserServiceDao {
}
```
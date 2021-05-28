package com.lenovo.feizai.ssmp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lenovo.feizai.ssmp.entity.User;
import com.lenovo.feizai.ssmp.service.UserServiceDao;
import com.lenovo.feizai.ssmp.util.GsonUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author feizai
 * @date 2021/5/28 0028 下午 6:20:21
 * @annotation
 */
@RestController
public class UserController {
    @Autowired
    UserServiceDao userServiceDao;

    @GetMapping("/getAllUser")
    public String getAllUser() {
        Page<User> page = new Page<>(1,2);
        Page<User> userPage = userServiceDao.page(page);
        List<User> users = userPage.getRecords();
        return GsonUtil.GsonString(users);
    }

    @GetMapping("/getUserByName")
    public String getUserByName(@Param("name") String name) {
        User user = userServiceDao.getOne(new QueryWrapper<User>().eq("name", name));
        if (user != null) {
            return GsonUtil.GsonString(user);
        } else {
            return GsonUtil.GsonString("null");
        }
    }

    @GetMapping("/getUserByKey")
    public String getUserByKey(@Param("key") String key) {
        User user = userServiceDao.getOne(new QueryWrapper<User>().like("name", key));
        if (user != null) {
            return GsonUtil.GsonString(user);
        } else {
            return GsonUtil.GsonString("null");
        }
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody User user) {
        boolean save = userServiceDao.save(user);
        if (save) {
            return GsonUtil.GsonString(user);
        } else {
            return GsonUtil.GsonString("null");
        }
    }
}

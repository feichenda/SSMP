package com.lenovo.feizai.ssmp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lenovo.feizai.ssmp.entity.User;
import com.lenovo.feizai.ssmp.service.UserServiceDao;
import com.lenovo.feizai.ssmp.util.BaseModel;
import com.lenovo.feizai.ssmp.util.GsonUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author feizai
 * @date 2021/5/28 0028 下午 6:20:21
 * @annotation
 */
@CrossOrigin
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
        User user = userServiceDao.getOne(new QueryWrapper<User>().eq("username", name));
        if (user != null) {
            return GsonUtil.GsonString(user);
        } else {
            return GsonUtil.GsonString("null");
        }
    }

    @GetMapping("/getUserByKey")
    public String getUserByKey(@Param("key") String key) {
        User user = userServiceDao.getOne(new QueryWrapper<User>().like("username", key));
        if (user != null) {
            return GsonUtil.GsonString(user);
        } else {
            return GsonUtil.GsonString("null");
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername())
                .eq("password",user.getPassword());
        User selectuser = userServiceDao.getOne(wrapper);
        BaseModel<User> model = new BaseModel<>();
        if (selectuser != null) {
            model.setCode(200);
            model.setMessage("登录成功");
            model.setData(selectuser);
        } else {
            model.setCode(201);
            model.setMessage("登录失败");
        }
        return GsonUtil.GsonString(model);
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        User selectuser = userServiceDao.getOne(wrapper);
        BaseModel<User> model = new BaseModel<>();
        if (selectuser == null) {
            boolean save = userServiceDao.save(user);
            if (save) {
                model.setCode(200);
                model.setMessage("注册成功");
                model.setData(user);
            } else {
                model.setCode(201);
                model.setMessage("注册失败");
            }
        } else {
            model.setCode(202);
            model.setMessage("该用户已存在");
        }
        return GsonUtil.GsonString(model);
    }
}

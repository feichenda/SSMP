package com.lenovo.feizai.ssmp.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lenovo.feizai.ssmp.dao.UserDao;
import com.lenovo.feizai.ssmp.entity.User;
import org.springframework.stereotype.Service;

/**
 * @author feizai
 * @date 2021/5/28 0028 下午 6:23:36
 * @annotation
 */
@Service
public class UserService extends ServiceImpl<UserDao, User> implements UserServiceDao {
}

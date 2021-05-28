package com.lenovo.feizai.ssmp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author feizai
 * @date 2021/5/28 0028 下午 6:14:02
 * @annotation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    Integer id;
    String name;
    String password;
}

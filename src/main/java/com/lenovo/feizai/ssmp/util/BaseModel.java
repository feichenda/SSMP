package com.lenovo.feizai.ssmp.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author feizai
 * @date 2021/5/28 0028 下午 11:38:20
 * @annotation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseModel<T> {
    Integer code;
    String message;
    T data;
    List<T> datas;
}

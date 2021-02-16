package com.imooc.o2o.dto;

import com.dyuproject.protostuff.Input;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
/**
 * 将InputStream和imageName封装在一个对象中
 */
public class ImageHolder {
    private String imageName;
    private InputStream imageInputStream;
}

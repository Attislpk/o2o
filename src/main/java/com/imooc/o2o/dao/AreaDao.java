package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Area;

import java.util.List;

/**
 *
 */
public interface AreaDao {

    /**
     * 查询所有area
     * @return 以list的形式返回所有area
     */
    public List<Area> queryAllArea();

}

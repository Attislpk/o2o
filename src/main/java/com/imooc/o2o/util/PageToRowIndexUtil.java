package com.imooc.o2o.util;

/**
 * 由于后端数据库读取数据分页是: rowIndex 从第几行开始读取数据  pageSize 每页数据条数
 * 而前端展示的是：pageIndex 第几页， pageSize 每页数据条数  因此需要进行pageIndex和rowIndex的转换
 * rowIndex = (pageIndex -1)*pageSize
 * 第0行   读5条数据        1页-1 * 5
 * 第5行   读5条数据        2页-1 * 5
 * 第10行  读5条数据        3页-1 * 5
 * 第15行  读5条数据        4页-1 * 5
 */
public class PageToRowIndexUtil {
    public static int pageToRow(int pageIndex, int pageSize) {
        return pageIndex > 0 ? (pageIndex - 1) * pageSize : 0;
    }
}

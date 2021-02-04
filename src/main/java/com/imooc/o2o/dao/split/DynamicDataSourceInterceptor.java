package com.imooc.o2o.dao.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

/**
 * 对sql语句进行拦截，判断需要执行sql语句对应的数据库是master还是slave
 * 增删改封装在update中
 * 查封装在query中
 */
@Intercepts({@Signature(type = Executor.class,method = "update",args = {MappedStatement.class,Object.class}),
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicDataSourceInterceptor implements Interceptor {
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);

    /**
     * 拦截方法
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    //Invocation 包括对象，方法和方法参数，process方法代表执行该对象的方法，使用方法参数，即反射
    public Object intercept(Invocation invocation) throws Throwable {
        //判断当前事务是否开启
        boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        Object[] args = invocation.getArgs(); //获取invocation中存在的sql语句，存放到args数组中
        MappedStatement ms = (MappedStatement) args[0]; //获取sql语句的类型
        String lookupKey = DynamicDataSourceHolder.DB_MASTER;
        //未开启事务
        if (!transactionActive) {
            //SELECT语句
            if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                //如果需要查询的数据包括id属性，使用主库读取
                //selectKey为自增id查询主键(SELECT LAST_INSERT_ID())方法，因此需要使用主库避免id冲突
                if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    lookupKey = DynamicDataSourceHolder.DB_MASTER;
                } else {
                    //sql语句
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(args[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("\\t\\n\\r", " ");
                    //如果是增删改
                    if (sql.matches(REGEX)) {
                        lookupKey = DynamicDataSourceHolder.DB_MASTER;
                    } else {
                        lookupKey = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }
        } else {
            //通过事务管理的，一般都是写操作，因此需要使用主库
            lookupKey = DynamicDataSourceHolder.DB_MASTER;
        }
        logger.debug("设置方法[{}] use[{}] Strategy, SqlCommanType [{}]..",ms.getId(),lookupKey,ms.getSqlCommandType().name());
        DynamicDataSourceHolder.setDbType(lookupKey);
        return invocation.proceed();//通过反射继续执行sql操作
    }

    /**
     * Executor支持一系列增删改查操作
     *
     * @param target 传入的目标对象
     * @return 返回原对象or代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);  //如果传入的是Executor对象，则用当前拦截器进行拦截,
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

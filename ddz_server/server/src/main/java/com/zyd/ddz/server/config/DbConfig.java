package com.zyd.ddz.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import xyz.noark.core.annotation.Configuration;
import xyz.noark.core.annotation.Value;
import xyz.noark.core.annotation.configuration.Bean;
import xyz.noark.orm.accessor.DataAccessor;
import xyz.noark.orm.accessor.sql.mysql.MysqlDataAccessor;
import xyz.noark.orm.write.AsyncWriteService;
import xyz.noark.orm.write.impl.DefaultAsyncWriteServiceImpl;

/**
 * @author zyd
 * @date 2023/3/10 14:27
 */
@Configuration
public class DbConfig {

    @Value("data.mysql.ip")
    private String mysqlIp;
    @Value("data.mysql.port")
    private int mysqlPort;
    @Value("data.mysql.db")
    private String mysqlDb;
    @Value("data.mysql.user")
    private String mysqlUser;
    @Value("data.mysql.password")
    private String mysqlPassword;

    @Bean
    public DataAccessor dataAccessor(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(mysqlUser);
        dataSource.setPassword(mysqlPassword);
        dataSource.setUrl(String.format("jdbc:mysql://%s:%d/%s?serverTimezone=Asia/Shanghai", mysqlIp, mysqlPort, mysqlDb));
        dataSource.setInitialSize(4);
        dataSource.setMinIdle(4);
        dataSource.setMaxActive(8);
        dataSource.setPoolPreparedStatements(false);

        MysqlDataAccessor accessor = new MysqlDataAccessor(dataSource);
        accessor.setStatementExecutableSqlLogEnable(true);
        accessor.setStatementParameterSetLogEnable(true);
        accessor.setSlowQuerySqlMillis(1);// 执行时间超过1秒的都要记录下.
        return accessor;
    }

    @Bean
    public AsyncWriteService asyncWriteService() {
        return new DefaultAsyncWriteServiceImpl();
    }
}

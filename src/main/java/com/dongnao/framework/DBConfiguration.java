package com.dongnao.framework;

import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * 类描述：springboot默认数据源为tomcat的c3p0
 * 更改为druid
 */
@Configuration
public class DBConfiguration implements EnvironmentAware {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Bean
	public DataSource dataSource(Environment env) {
		logger.info(new Date().toLocaleString() + "|==>" + env.getProperty("spring.datasource.url"));
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));// 用户名
		dataSource.setPassword(env.getProperty("spring.datasource.password"));// 密码
		dataSource.setInitialSize(2);
		dataSource.setMaxActive(20);
		dataSource.setMinIdle(0);
		dataSource.setMaxWait(60000);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setTestOnBorrow(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setPoolPreparedStatements(false);
		return dataSource;
	}

	@Override
	public void setEnvironment(Environment env) {
		logger.info(new Date().toLocaleString() + "|" + env.getProperty("spring.datasource.url"));
		dataSource(env);
	}

}

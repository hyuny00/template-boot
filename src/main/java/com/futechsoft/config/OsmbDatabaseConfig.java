package com.futechsoft.config;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.futechsoft.framework.annotation.OsmbMapper;
import com.zaxxer.hikari.HikariDataSource;

@Configuration 
@MapperScan(value="com.futechsoft", annotationClass=OsmbMapper.class, sqlSessionFactoryRef="osmbSqlSessionFactory") 
@EnableTransactionManagement 
public class OsmbDatabaseConfig {

	@Bean(name = "osmbDataSource", destroyMethod = "close")
	@ConfigurationProperties(prefix = "mybatis.osmb.datasource")
	public DataSource osmbDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}  

	@Bean(name = "osmbSqlSessionFactory")
	public SqlSessionFactory osmbSqlSessionFactory(@Qualifier("osmbDataSource") DataSource osmbDataSource,	ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(osmbDataSource);
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/sqlmap/mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/sqlmap/osmbMappers/**/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "osmbSqlSessionTemplate")
	public SqlSessionTemplate osmbSqlSessionTemplate(SqlSessionFactory osmbSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(osmbSqlSessionFactory);
	}
}

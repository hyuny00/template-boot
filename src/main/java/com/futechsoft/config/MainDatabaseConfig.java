package com.futechsoft.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.futechsoft.framework.annotation.Mapper;
import com.futechsoft.framework.util.RefreshableSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;

@Configuration 
@MapperScan(value={"com.futechsoft", "sample"}, annotationClass=Mapper.class, sqlSessionFactoryRef="mainSqlSessionFactory") 
@EnableTransactionManagement 
@EnableJpaRepositories(
	    basePackages = "sample", // Master Repository 경로
	    entityManagerFactoryRef = "jpaEntityManagerFactory", 
	    transactionManagerRef = "mainTransactionManager"
	)

public class MainDatabaseConfig { 
	
	
	
	
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis.main.datasource")
	public JndiPropertyHolder getJndiPropertyHolder() {
	    return new JndiPropertyHolder();
	}
	 
	/* 
	@Primary
	@Bean(name = "mainDataSource", destroyMethod = "close")
  	public DataSource jndiDatasource() throws IllegalArgumentException, NamingException {
		String jndiName = getJndiPropertyHolder().getJndiName();
	    return (DataSource) new JndiDataSourceLookup().getDataSource(jndiName);
	}
  */
	 
	
	@Bean(name = "mainDataSource", destroyMethod = "close")
	@Primary
	@ConfigurationProperties(prefix = "mybatis.main.datasource")
	public DataSource mainDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	
	@Bean(name = "mainSqlSessionFactory")
	@Primary
	public SqlSessionFactory mainSqlSessionFactory(@Qualifier("mainDataSource") DataSource mainDataSource, ApplicationContext applicationContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new RefreshableSqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(mainDataSource);
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/sqlmap/mybatis-config.xml"));
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/sqlmap/mappers/**/*.xml"));
		
		((RefreshableSqlSessionFactoryBean) sqlSessionFactoryBean).setInterval(5000);

		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "mainSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate mainSqlSessionTemplate(SqlSessionFactory mainSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(mainSqlSessionFactory);
	}
	
	class JndiPropertyHolder {
	    private String jndiName;
	 
	    public String getJndiName() {
	      return jndiName;
	    }  
	 
	    public void setJndiName(String jndiName) {
	      this.jndiName = jndiName;
	    }
	  }

	
	
    
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory( EntityManagerFactoryBuilder builder,  @Qualifier("mainDataSource") DataSource dataSource ) {
    	
    	
    	// Map<String, Object> properties = new HashMap<String, Object>();
        // properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
         
        return builder.dataSource(dataSource).packages("sample.domain"). build();
    }

   
    @Primary
    @Bean
    public JpaTransactionManager mainTransactionManager( @Qualifier("jpaEntityManagerFactory") LocalContainerEntityManagerFactoryBean mfBean ) {
       
    	JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( mfBean.getObject() );
        return transactionManager;
    }
    
    
    
}

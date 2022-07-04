package com.example.demo.datasource;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DataSourceConfig {

	@Value("${spring.datasource.url}")
	String url;
	@Value("${spring.datasource.driver-class-name}")
	String driverClass;
	@Value("${spring.datasource.username}")
	String userName;
	@Value("${spring.datasource.password}")
	String password;
	@Value("${spring.datasource.schema}")
	String schema;

	@Bean
	public DriverManagerDataSource getDataSource() {
		DriverManagerDataSource dataSourceBuilder = new DriverManagerDataSource();
		dataSourceBuilder.setDriverClassName(driverClass);
		dataSourceBuilder.setUrl(url);
		dataSourceBuilder.setUsername(userName);
		dataSourceBuilder.setPassword(password);
		dataSourceBuilder.setSchema(schema);
		return dataSourceBuilder;
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
	
	

}

package com.example.demo.datasource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TenantDataSource implements Serializable {

	/**
	 * 
	 */

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final long serialVersionUID = 1L;

	private Map<String, DataSource> dataSources = new HashMap<>();

	private Map<String, DataSourceDto> dataSourceList = new HashMap<>();

	public DataSource getDataSource(String name) {

		if (dataSources.get(name) != null) {
			return dataSources.get(name);
		}

		DataSource dataSource = createDataSource(name);

		if (dataSource != null) {
			dataSources.put(name, dataSource);
			log.info("Data source created {}", dataSources);
		}
		return dataSource;
	}

	public JdbcTemplate getJDBCTemplate(String name) {

		return new JdbcTemplate(this.getDataSource(name));
	}
	
	public Connection getConnection(String name) throws SQLException {

		return this.getDataSource(name).getConnection();
	}

	@PostConstruct
	public void getAll() {

		try {
			List<DataSourceDto> configList = jdbcTemplate.query(
					"SELECT PKEY,DBPWD, DBURL, DBUSER,tenant,driverclassname,databasetype FROM tenant",
					new TenantSchemaDetailsRowMapper());

			log.info("List of tenants [{}]", configList);

			configList.forEach(t -> dataSourceList.put(t.getName(), t));

			log.info("Mapped tenants [{}]", configList);

			for (DataSourceDto config : configList) {
				this.getDataSource(config.getName());
			}

		} catch (Exception e) {
			log.error("Exceptio while loading data source ", e);
		}
	}

	private DataSource createDataSource(String name) {
		DataSourceDto config = dataSourceList.get(name);
		if (config != null) {
			return DataSourceBuilder.create().driverClassName(config.getDriverClassName())
					.username(config.getUsername()).password(config.getPassword()).url(config.getUrl()).build();
		}
		return null;
	}
	
	

}
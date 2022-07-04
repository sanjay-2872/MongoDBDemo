package com.example.demo.datasource;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TenantSchemaDetailsRowMapper implements RowMapper<DataSourceDto> {

	public DataSourceDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		DataSourceDto dbDetails = new DataSourceDto();
		dbDetails.setUrl(rs.getString("dburl"));
		dbDetails.setUsername(rs.getString("dbuser"));
		dbDetails.setPassword(rs.getString("dbpwd"));
		dbDetails.setDriverClassName(rs.getString("driverclassname"));
		dbDetails.setName(rs.getString("tenant"));
		dbDetails.setDatabaseType(rs.getString("databasetype"));
		return dbDetails;
	}

}

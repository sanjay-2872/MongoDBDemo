package com.example.demo.datasource;

import lombok.Data;

@Data
public class DataSourceDto {

	private String name;
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	private String databaseType;
}

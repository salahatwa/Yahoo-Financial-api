package com.z2d.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class DB {
	private static final Logger LOGGER = LoggerFactory.getLogger(DB.class);
	private static DataSource DATA_SOURCE;

	static {
		try {
			DATA_SOURCE = new ComboPooledDataSource();
		} catch (Exception e) {
			LOGGER.error("Connection pool creation failed", e);
		}
	}

	private DB() {
	}

	public static Connection getConnection() throws SQLException {
		return DATA_SOURCE.getConnection();
	}
}

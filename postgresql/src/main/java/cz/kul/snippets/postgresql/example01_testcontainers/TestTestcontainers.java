package cz.kul.snippets.postgresql.example01_testcontainers;

import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class TestTestcontainers {

	@Rule
	public PostgreSQLContainer postgresContainer = new PostgreSQLContainer(); // todo

	@Test
	public void test() throws Exception {
		String jdbcUrl = postgresContainer.getJdbcUrl();
		String username = postgresContainer.getUsername();
		String password = postgresContainer.getPassword();
		Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
		ResultSet resultSet = conn.createStatement().executeQuery("SELECT 1");
		resultSet.next();
		int result = resultSet.getInt(1);
		assertEquals(1, result);
	}

}

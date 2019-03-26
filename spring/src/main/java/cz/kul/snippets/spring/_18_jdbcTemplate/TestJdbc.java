package cz.kul.snippets.spring._18_jdbcTemplate;

import cz.kul.snippets.SnippetsTest;
import cz.kul.snippets.spring.common.SpringTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class TestJdbc extends SnippetsTest {
    
    @Configuration
    public static class Config {

        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
            dataSource.setUrl("jdbc:hsqldb:mem:mymemdb");
            dataSource.setUsername("SA");
            dataSource.setPassword("");
            return dataSource;
        }
        
        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public NamedParameterJdbcTemplate namedJdbcTemplate(DataSource dataSource) {
            return new NamedParameterJdbcTemplate(dataSource);
        }
        
    }
    
    @Before
    public void prepareDatabase() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            JdbcTemplate jt = ctx.getBean(JdbcTemplate.class);
            
            // It clean the database. All tests run in one JVM run so all tests use the same
            // database.
            jt.execute("DROP SCHEMA PUBLIC CASCADE");
            
            jt.execute("create table employee (id int, name varchar(255))");
            jt.execute("insert into employee (id, name) values (1, 'monica')");
            jt.execute("insert into employee (id, name) values (2, 'rachel')");
            jt.execute("insert into employee (id, name) values (3, 'phoeboe')");
        });
    }

    @Test
    public void queryList() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            JdbcTemplate jt = ctx.getBean(JdbcTemplate.class);
            List<Map<String, Object>> data = jt.queryForList("select * from employee where id < ? order by id", 3);
            Assert.assertEquals( 2, data.size());
            Assert.assertEquals(1, data.get(0).get("id"));
            Assert.assertEquals("monica", data.get(0).get("name"));
        });
    }

    @Test
    public void queryListWithOneColumn() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            JdbcTemplate jt = ctx.getBean(JdbcTemplate.class);
            List<String> names = jt.queryForList("select name from employee order by id", String.class);
            Assert.assertArrayEquals(new String[] {"monica", "rachel", "phoeboe"}, names.toArray(new String[3]));
                        
            assertThrows(IncorrectResultSetColumnCountException.class, () -> {
                jt.queryForList("select * from employee order by id", Employee.class);
            });
            
        });
    }

    @Test
    public void queryListWithMapping() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            JdbcTemplate jt = ctx.getBean(JdbcTemplate.class);
            List<Employee> employees = jt.query(
                    "select * from employee order by id",
                    new BeanPropertyRowMapper<>(Employee.class));
            Assert.assertEquals( 3, employees.size());
            Assert.assertEquals(1, employees.get(0).getId());
            Assert.assertEquals("monica", employees.get(0).getName());
        });
    }
    
    @Test
    public void simpleQuery() {
        // NOTE: you can use this when you returns just one row with one column
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            JdbcTemplate jt = ctx.getBean(JdbcTemplate.class);
            Integer count = jt.queryForObject("select count(*) from employee", Integer.class);
            Assert.assertEquals((Integer) 3, count);
        });
    }

    @Test
    public void insert() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            JdbcTemplate jt = ctx.getBean(JdbcTemplate.class);
            jt.update("insert into employee values (?, ?)", 4, "ross");
            Integer count = jt.queryForObject("select count(*) from employee", Integer.class);
            Assert.assertEquals((Integer) 4, count);
        });
    }

    @Test
    public void queryWithNamedParameters() {
        SpringTestUtils.runInSpring(Config.class, ctx -> {
            NamedParameterJdbcTemplate jt = ctx.getBean(NamedParameterJdbcTemplate.class);
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("name1", "monica")
                    .addValue("name2", "rachel");
            String name = jt.queryForObject(
                    "select name from employee where name <> :name1 and name <> :name2",
                    parameterSource,
                    String.class);
            Assert.assertEquals("phoeboe", name);
        });
    }
    
}

package cz.kul.snippets.benchmark.psql;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableAndIndexSize {

  public static void main(String[] args) throws Exception {

//    final int TABLE_ROWS = 1_000_000;
    final int TABLE_ROWS = 1_000_000;

    Results results = new Results();

    NumTableParams numTableParams = results.getNumTableParams();
    numTableParams.setTableRows(TABLE_ROWS);

    StrTableParams strTableParams = results.getStrTableParams();
    strTableParams.setTableRows(TABLE_ROWS);
    strTableParams.setStrLength(16);

    NormalTableParams normalTableParams = results.getNormalTableParams();
    normalTableParams.setTableRows(TABLE_ROWS);

    try (Connection connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/benchmark",
        "postgres",
        "postgres")) {

      measureNumTable(connection, numTableParams, results);
      measureStrTable(connection, strTableParams, results);
      measureNormalTable(connection, normalTableParams, results);
    }

    String resultsJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(results);
    System.out.println(resultsJson);
  }

  private static void measureNumTable(Connection connection, NumTableParams params, Results results) throws Exception {
    execute("DROP TABLE IF EXISTS NUM_TABLE;", connection);

    execute("""
          create table NUM_TABLE (
            id bigserial primary key
          );
          """,
        connection);

    execute( """
          do $$
          begin
          for r in 1..%s loop
          insert into num_table(id) values(DEFAULT);
          end loop;
          end;
          $$;
          """,
        connection,
        params.getTableRows());

    Number tableSize = (Number) executeSingleRowQuery("select pg_table_size('num_table');", connection)[0];
    Number indexSize = (Number) executeSingleRowQuery("select pg_relation_size('num_table_pkey');", connection)[0];
    results.getNumTableResults().setTableSizeBytes(tableSize.longValue());
    results.getNumTableResults().setPkeySizeBytes(indexSize.longValue());
  }

  private static void measureStrTable(Connection connection, StrTableParams params, Results results) throws Exception {
    execute("DROP TABLE IF EXISTS STR_TABLE;", connection);

    execute("""
          create table STR_TABLE (
            id text primary key
          );
          """,
        connection);

    execute( """
          do $$
          begin
          for r in 1..%s loop
          insert into str_table(id) values(right('00000000000000000000000000000000000000000000000000'||r, %s));
          end loop;
          end;
          $$;
          """,
        connection,
        params.getTableRows(),
        params.getStrLength());

    Number tableSize = (Number) executeSingleRowQuery("select pg_table_size('str_table');", connection)[0];
    Number indexSize = (Number) executeSingleRowQuery("select pg_relation_size('str_table_pkey');", connection)[0];
    results.getStrTableResults().setTableSizeBytes(tableSize.longValue());
    results.getStrTableResults().setPkeySizeBytes(indexSize.longValue());
  }

  private static void measureNormalTable(Connection connection, NormalTableParams params, Results results) throws Exception {
    execute("DROP TABLE IF EXISTS NORMAL_TABLE;", connection);

    execute("""
          create table NORMAL_TABLE (
            id bigserial primary key,
            num1 bigint,
            num2 integer,
            num3 integer,
            text1 text,
            text2 text,
            text3 text
          );
          """,
        connection);

    execute( """
          do $$
          begin
          for r in 1..%s loop
          insert into normal_table(num1, num2, num3, text1, text2, text3) values(
            floor(random() * 1000000000)::bigint,
            floor(random() * 1000000)::int,
            floor(random() * 1000000)::int,
            substring(md5(random()::text), 1, 8),
            substring(md5(random()::text), 1, 16),
            substring(md5(random()::text), 1, 32)
          );
          end loop;
          end;
          $$;
          """,
        connection,
        params.getTableRows());

    Number tableSize = (Number) executeSingleRowQuery("select pg_table_size('normal_table');", connection)[0];
    Number indexSize = (Number) executeSingleRowQuery("select pg_relation_size('normal_table_pkey');", connection)[0];
    results.getNormalTableResults().setTableSizeBytes(tableSize.longValue());
    results.getNormalTableResults().setPkeySizeBytes(indexSize.longValue());
  }

  private static void execute(String query, Connection connection, Object... args) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.execute(String.format(query, args));
    }
  }

  private static Object[] executeSingleRowQuery(String query, Connection connection, Object... args) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement.executeQuery(String.format(query, args))) {
        if (resultSet.next()) {
          int columnCount = resultSet.getMetaData().getColumnCount();
          Object[] res = new Object[columnCount];
          for (int i = 0; i < columnCount; i++) {
            res[i] = resultSet.getObject(i + 1);
          }
          return res;
        } else {
          return null;
        }
      }
    }
  }

}

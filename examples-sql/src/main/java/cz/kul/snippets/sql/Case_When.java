/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.sql;

import cz.kul.snippets.sql.commons.PrintUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * "CASE WHEN ELSE" is "if else" construction for sql queries. 
 * 
 * Case statement is frequently used in select part, but it can be used in
 * any section of sql command. 
 * 
 * @author kulhalad
 */
public class Case_When
{

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
            prepareSchemeAndData(conn);
            caseWhen(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void prepareSchemeAndData(Connection conn) throws SQLException {
        StringBuilder scheme = new StringBuilder();
        scheme.append("CREATE TABLE phone (id INTEGER, name VARCHAR(64), price INTEGER, country VARCHAR(64), os VARCHAR(64));");

        StringBuilder data = new StringBuilder();
        data.append("INSERT INTO phone VALUES (1, 'Iphone6', 1000, 'us', 'osx');");
        data.append("INSERT INTO phone VALUES (2, 'Iphone6', 1200, 'cs', 'osx');");
        data.append("INSERT INTO phone VALUES (3, 'SamsungG5', 800, 'us', 'android');");
        data.append("INSERT INTO phone VALUES (4, 'SamsungG5', 900, 'de', 'android');");
        data.append("INSERT INTO phone VALUES (5, 'LG700', 600, 'us', 'android');");

        Statement stm = conn.createStatement();
        stm.executeUpdate(scheme.toString());
        stm.executeUpdate(data.toString());
    }

    private static void caseWhen(Connection conn) throws SQLException {
        {
            StringBuilder sb = new StringBuilder();
            sb.append("select id, ");
            sb.append("  case ");
            sb.append("    when country = 'cs' then 'Czech!' ");
            sb.append("    when country = 'us' then 'American' ");
            sb.append("    else 'unknown' ");
            sb.append("  end Nationality ");
            sb.append("from phone");
            PrintUtils.exectuteAndPrint("Case in select", conn, sb.toString());
        }

        {
            StringBuilder sb = new StringBuilder();
            sb.append("select * ");
            sb.append("from phone ");
            sb.append("where country = (case when 1=1 then 'cs' else 'us' end) ");
            PrintUtils.exectuteAndPrint("Case in where", conn, sb.toString());
        }
    }

}

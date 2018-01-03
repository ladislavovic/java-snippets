/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.sql;

import java.math.BigDecimal;
import org.junit.Assert;

/**
 *
 * @author kulhalad
 */
public class _06_Basics {
    
    public static void main(String[] args) {
        nullValue();
        joinViaNULLValue();
    }
    
    /**
     * SQL works differently with NULL value in comparison with common programming
     * language.
     * 
     * NULL is not regarded as a value. It means "missing value". Because of that
     * you can be surprised when you use NULL in some expressions.
     */
    private static void nullValue() {
        Database db = Database.connect("jdbc:hsqldb:mem:db1", "SA", "");
        db.executeUpdate("CREATE TABLE person (id_person INTEGER, name VARCHAR(64), age INTEGER);");
        db.executeUpdate("INSERT INTO person VALUES (1, 'Jane', 31);"); 
        db.executeUpdate("INSERT INTO person VALUES (1, 'Suzie', NULL);");
        
        // Simple, just condition
        Assert.assertEquals(1, db.executeSelect("select * from person where age = 31").getYSize());
        
        // NULL equals anything. You can not use it in expressions like this. It is always false.
        // If you want to test NULL, you must use IS (NOT) NULL syntax
        Assert.assertEquals(0, db.executeSelect("select * from person where age <> NULL").getYSize());
        Assert.assertEquals(0, db.executeSelect("select * from person where age = NULL").getYSize());
        Assert.assertEquals(1, db.executeSelect("select * from person where age IS NULL").getYSize());
        
        // Also when you use "not equal" operator, NULL rows are not included in the result
        Assert.assertEquals(0, db.executeSelect("select * from person where age <> 31").getYSize());
        Assert.assertEquals(1, db.executeSelect("select * from person where age <> 31 OR age is null").getYSize());
    }
    
    /**
     * Because NULL in SQL is not common value (like in Java for example),
     * the joining to NULL value works according to it. See examples below.
     */
    private static void joinViaNULLValue() {
        Database db = Database.connect("jdbc:hsqldb:mem:db3", "SA", "");
        db.executeUpdate("CREATE TABLE foo (id INTEGER, value VARCHAR(64));");
        db.executeUpdate("INSERT INTO foo VALUES (1, 'val1');"); 
        db.executeUpdate("INSERT INTO foo VALUES (2, NULL);"); 
        
        {
            // Join to NULL value
            //  * there is joined nothing to null value
            //  * does not matter if you use inner or left join. It works identically.
            StringBuilder q = new StringBuilder();
            q.append("SELECT f1.id ID1, f1.value VAL1, f2.id ID2, f2.value VAL2 FROM ");
            q.append("foo AS f1 ");
            q.append("LEFT JOIN foo AS f2 ON f2.value = f1.value ");
            q.append("ORDER BY f1.id");
            db.executeSelectAndPrint("Join via NULL - example1", q.toString());
            Data data = db.executeSelect(q.toString());
            Assert.assertEquals(null, data.getData("ID2", 1));
        }
        
        {
            // How to join to NULL value
            //  * if you want it you must adjust join expression a little bit
            StringBuilder q = new StringBuilder();
            q.append("SELECT f1.id ID1, f1.value VAL1, f2.id ID2, f2.value VAL2 FROM ");
            q.append("foo AS f1 ");
            q.append("LEFT JOIN foo AS f2 ON ( f2.value = f1.value OR (f2.value IS NULL AND f1.value IS NULL))");
            q.append("ORDER BY f1.id");
            db.executeSelectAndPrint("Join via NULL - example2", q.toString());
            Data data = db.executeSelect(q.toString());
            Assert.assertEquals(2, data.getData("ID2", 1));
        }
        
    }

    
}

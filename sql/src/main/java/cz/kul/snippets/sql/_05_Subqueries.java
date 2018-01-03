package cz.kul.snippets.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.NVList;

/**
 * Subqueries are very powerful.
 *
 *
 * @author kulhalad
 * @since 7.4
 *
 */
public class _05_Subqueries {

    /**
     * You can put subquery in the select part. It is very strong feature expecially
     * when you use correlated version. You can load any other data which are
     * not in the from part of the primary query.
     * 
     * In this example there are four subqueries.
     * The first one returns count of accessories in every car.
     * The second one returns true if the tempomat is present.
     * The third one returns true if the aircondition is present.
     * The fourth one returns true if the navigation is present. 
     */
    private static void subqueryInSelectPart() {
        Database db = Database.connect("jdbc:hsqldb:mem:db1", "SA", "");
        db.executeUpdate("CREATE TABLE car (id_car INTEGER, name VARCHAR(64));");
        db.executeUpdate("CREATE TABLE accessory (id_accessory INTEGER, name VARCHAR(64));");
        db.executeUpdate("CREATE TABLE car_accessory (id_car INTEGER, id_accessory INTEGER);");
        db.executeUpdate("INSERT INTO car VALUES (1, 'Mondeo');");
        db.executeUpdate("INSERT INTO car VALUES (2, 'Pasat');");
        db.executeUpdate("INSERT INTO car VALUES (3, 'Insignia');");
        db.executeUpdate("INSERT INTO accessory VALUES (1, 'tempomat');");   // pasat, insignia
        db.executeUpdate("INSERT INTO accessory VALUES (2, 'klima');");      // pasat, insignia, mondeo
        db.executeUpdate("INSERT INTO accessory VALUES (3, 'navigace');");   // insignia
        db.executeUpdate("INSERT INTO accessory VALUES (4, 'wifi');");       // nobody
        db.executeUpdate("INSERT INTO car_accessory VALUES (1, 2);");
        db.executeUpdate("INSERT INTO car_accessory VALUES (2, 1);");
        db.executeUpdate("INSERT INTO car_accessory VALUES (2, 2);");
        db.executeUpdate("INSERT INTO car_accessory VALUES (3, 1);");
        db.executeUpdate("INSERT INTO car_accessory VALUES (3, 2);");
        db.executeUpdate("INSERT INTO car_accessory VALUES (3, 3);");
        
        StringBuilder q = new StringBuilder();
        q.append("select ");
        q.append("  car1.id_car as CarId, ");
        q.append("  car1.name as CarName, ");
        q.append("  (select count(*) from car_accessory where car_accessory.id_car = car1.id_car) as AccessCount, ");
        q.append("  ( ");
        q.append("    select case when count(*) > 0 then 'true' else 'false' end ");
        q.append("    from car_accessory where car_accessory.id_car = car1.id_car and id_accessory = 1 ");
        q.append("  ) as Tempomat, ");
        q.append("  ( ");
        q.append("    select case when count(*) > 0 then 'true' else 'false' end ");
        q.append("    from car_accessory where car_accessory.id_car = car1.id_car and id_accessory = 2 ");
        q.append("  ) as Klima, ");
        q.append("  ( ");
        q.append("    select case when count(*) > 0 then 'true' else 'false' end ");
        q.append("    from car_accessory where car_accessory.id_car = car1.id_car and id_accessory = 3 ");
        q.append("  ) as Navigace ");
        q.append("from ");
        q.append("  car as car1 ");
        db.executeSelectAndPrint("Subquery in \"SELECT\" part", q.toString());
    }

    public static void main(String[] args) {
        subqueryInSelectPart();
    }

}

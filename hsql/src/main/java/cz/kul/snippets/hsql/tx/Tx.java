package cz.kul.snippets.hsql.tx;

import java.sql.Connection;
import java.util.List;

/**
 * DB transaction isolation level theory
 * ======================================
 * Theory describe three phenomens, which can occur when more transactions
 * run at the same time in DB:
 *   - dirty read - transaction reads uncommited data from another transaction
 *   - non repeatable read - a select query which is executed two times (and between
 *     executions there are no other commands) return different results
 *   - phantoms - 
 *       BEGIN;
 *          UPDATE zaznamy SET deleted = true;
 *          SELECT count(*) FROM zaznamy WHERE NOT deleted;
 *        COMMIT;
 *      If the result of the select is > 0, you have phantom rows (just another
 *      transaction added row into the table)
 *
 * Theory define four isolation levels:
 * READ_UNCOMMITED (dirty read, non repeatable read, phantoms)
 * READ_COMMITED   (-         , non repeatable read, phantoms)
 * REPEATABLE_READ (-         , -                  , phantoms)
 * SERIALIZABLE    (-         , -                  , -       )
 * 
 * 2 phase locking
 * =====================
 * One way how to handle more transactions is locking. Transaction can 
 * lock tables during reading and writing (or it can lock only some rows, 
 * it depends on implementation).
 * It is called 2-phase, because during 1st phase the transaction is creating
 * locks. Actually the 1st phase is whole "work life" of transaction. Locks are
 * only created, they are not released, so their count is increasing.
 * 2nd phase is end of the transaction and in this phase all locks are released.
 * There are two types of locks:
 * Shared locks - or Read Locks. More transactions can lock the same table
 *     by shared locks. This lock is used when transaction read from a table.
 *     It bans other transaction to change what was already read.
 * Exclusive lock - or Write Lock. Only one transaction can lock the same table 
 *     by exclusive lock. It is used when transaction write into the table. 
 *     It deny other transactions to write the same table. Only one transaction
 *     can write a table at the same time.
 * During 2nd phase, at the end of the transaction (on commit or rollback), 
 * it releases all locks.
 * 
 * Locks during particular isolation levels
 * -----------------------------------------
 * READ_UNCOMMITED
 *   respect other transactions locks during reading:
 *       NO (it just read everything regardless it is commited or not)
 *   respect other transactions locks during writing:
 *       YES (of course, it would destroy somebody else's data)
 *   create shared locks: 
 *       NO (data which was read can be changed by other tx)
 *   create exclusive locks:
 *       YES (I'm not sure but I think when I write something, nobody else
 *       can rewrite it)
 * 
 * READ_COMMITED
 *   respect other transactions locks during reading:
 *       YES (it does not read data from exclusively locked tables, there are
 *       uncommited data there)
 *   respect other transactions locks during writing:
 *       YES (of course, it would destroy somebody else's data)
 *   create shared locks: 
 *       NO (data which was read can be changed by other tx)
 *   create exclusive locks:
 *       YES 
 * 
 * REPEATABLE_READ
 *   I thing it is very similar to SERIALIZABLE
 * 
 * SERIALIZABLE
 *   respect other transactions locks during reading:
 *       YES (it does not read data from exclusively locked tables, there are
 *       uncommited data there)
 *   respect other transactions locks during writing:
 *       YES (of course, it would destroy somebody else's data)
 *   create shared locks: 
 *       YES (read data can not be changed by other tx)
 *   create exclusive locks:
 *       YES 
 *   
 * MVCC
 * ============
 * In this architecture more users can read and write at once, there are no locks.
 * The conflict occur only when two transactions want to modify the same row. The
 * conflict can be solved by many ways - waiting, rollback, error, ... *   
 */
public class Tx {

    public static void main(String[] args) {
        _2pl_readCommited_DirtyRead();
        _2pl_readCommited_NonRepeatableRead();
        _2pl_serializable_NonRepeatableRead();
        
        // Im not able to simulate this
        // _2pl_readCommited_Phantoms();
        
        mvcc_readCommited_NonRepeatableRead();
        mvcc_serializable_NonRepeatableRead();
    }
    
    private static void _2pl_readCommited_DirtyRead() {
        System.out.println();
        System.out.println("2PL - READ_COMMITED - Dirty Read");
        System.out.println("=================================");
        System.out.println("It can not occur in READ_COMMITTED so the transaction will be waiting here.");
 
        Table people = new TablePeople();
        
        DbConnection conn1 = new DbConnection("db1", Connection.TRANSACTION_READ_COMMITTED);
        conn1.createTable(people);
        conn1.insertRow(people);
        
        DbConnection conn2 = new DbConnection("db1", Connection.TRANSACTION_READ_COMMITTED);
        try {
            conn2.getRowCount(people);
            throw new RuntimeException("It should not go here. Something is wrong...");
        } catch (Exception e) {
            System.out.println("Transaction was waiting and was rolled backed after the timeout.");
        }
        
        conn1.commit();
        conn1.close();
        conn2.close();
    }
    
    private static void _2pl_readCommited_NonRepeatableRead() {
        System.out.println();
        System.out.println("2PL - READ_COMMITED - Non Repeatable Read");
        System.out.println("=========================================");
 
        Table people = new TablePeople();
        DbConnection conn1 = new DbConnection("db2", Connection.TRANSACTION_READ_COMMITTED);
        DbConnection conn2 = new DbConnection("db2", Connection.TRANSACTION_READ_COMMITTED);

        conn1.createTable(people); // no lock
        System.out.println("Tx2 number of rows: " + conn2.getRowCount(people)); // no shared lock because READ_COMMITTED
        conn1.insertRow(people); // exclusive lock
        conn1.commit(); // exclusive lock released
        System.out.println("Tx2 number of rows: " + conn2.getRowCount(people)); // non-repeatable-read phenomen
        
        conn1.close();
        conn2.close();
    }
    
    private static void _2pl_readCommited_Phantoms() {
        System.out.println();
        System.out.println("2PL - READ_COMMITED - Phantoms");
        System.out.println("=========================================");
 
        Table people = new TablePeople();
        
        DbConnection conn1 = new DbConnection("db3", Connection.TRANSACTION_READ_COMMITTED);
        conn1.createTable(people);
        conn1.insertRow(people);
        conn1.commit();

        DbConnection conn2 = new DbConnection("db3", Connection.TRANSACTION_READ_COMMITTED);
        conn2.update(people, "UPDATE people SET name = NULL");
        
        conn1.insertRow(people);
        conn1.commit();
        
        List<List<String>> result = conn2.select(people, "SELECT * FROM people WHERE name IS NOT NULL");
        System.out.println("Number of rows where name IS NOT NULL: " + result.size());
        
        conn2.commit();
        conn1.close();
        conn2.close();
    }
    
    private static void _2pl_serializable_NonRepeatableRead() {
        System.out.println();
        System.out.println("2PL - SERIALIZABLE - Non Repeatable Read");
        System.out.println("========================================");
 
        Table people = new TablePeople();
        DbConnection conn1 = new DbConnection("db4", Connection.TRANSACTION_SERIALIZABLE);
        conn1.createTable(people);
        DbConnection conn2 = new DbConnection("db4", Connection.TRANSACTION_SERIALIZABLE);
 
        System.out.println("Tx2 number of rows: " + conn2.getRowCount(people)); // shared lock created
        try {
            conn1.insertRow(people); // can not, there is shared lock and it need exclusive lock
            conn1.commit();
        } catch (Exception e) {
            System.out.println("Another transaction can not create write lock, because the table was read already by SERIALIZABLE tx.");
        }
        System.out.println("Tx2 number of rows: " + conn2.getRowCount(people));

        conn1.commit();
        conn1.close();
        conn2.close();
    }
    
    private static void mvcc_readCommited_NonRepeatableRead() {
        System.out.println();
        System.out.println("MVCC - READ_COMMITED - Non Repeatable Read");
        System.out.println("=========================================");
 
        Table people = new TablePeople();
        
        DbConnection conn1 = new DbConnection("db5", Connection.TRANSACTION_READ_COMMITTED);
        conn1.setMVCC(); // It set for whole DB, not the conn only
        conn1.createTable(people);
        DbConnection conn2 = new DbConnection("db5", Connection.TRANSACTION_READ_COMMITTED);

        System.out.println("Tx2 number of rows: " + conn2.getRowCount(people));
        conn1.insertRow(people); // add row. Internal timestamp is set.
        conn1.commit();
        System.out.println("Tx2 number of rows: " + conn2.getRowCount(people)); // returns all commited rows regardless its timestamp
        
        System.out.println("It just read actual commited state.");
        
        conn1.close();
        conn2.close();
    }
    
    private static void mvcc_serializable_NonRepeatableRead() {
        System.out.println();
        System.out.println("MVCC - SERIALIZABLE - Non Repeatable Read");
        System.out.println("=========================================");
 
        Table people = new TablePeople();
        
        DbConnection conn1 = new DbConnection("db6", Connection.TRANSACTION_SERIALIZABLE);
        conn1.setMVCC();
        conn1.createTable(people);
        DbConnection conn2 = new DbConnection("db6", Connection.TRANSACTION_SERIALIZABLE);
        
        System.out.println("Tx2 number of rows: " + conn2.getRowCount(people));
        conn1.insertRow(people);
        conn1.commit();
        System.out.println("Tx1 number of rows: " + conn1.getRowCount(people));
        System.out.println("Tx2 number of rows: " + conn2.getRowCount(people)); // do not see new row, because timestamp > txTimestamp and it is created by other Tx
        
        System.out.println("So 'Non Repeatable Read' does not occur at 'SERIALIZABLE' level.");
        System.out.println("Because of MVCC there is no conflict and waiting.");
        
        conn1.close();
        conn2.close();
    }
    
    
}

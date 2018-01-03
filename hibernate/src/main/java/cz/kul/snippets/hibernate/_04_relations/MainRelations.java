/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations;

import cz.kul.snippets.hibernate._04_relations._01_onetoone_foreignkey.OneToOneForeignKeyTest;
import cz.kul.snippets.hibernate._04_relations._02_onetoone_shared_primary_key.OneToOneSharedPrimaryKey;
import cz.kul.snippets.hibernate._04_relations._03_onetoone_join_table.OneToOneJoinTable;
import cz.kul.snippets.hibernate._04_relations._04_onetomany_foreignkey.OneToManyForeignKeyTest;
import cz.kul.snippets.hibernate._04_relations._05_onetomany_join_table.OneToManyJoinTableTest;
import cz.kul.snippets.hibernate._04_relations._06_manytomany.ManyToManyTest;
import cz.kul.snippets.hibernate._04_relations._07_map.MapTest;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**%
 *
 * @author kulhalad
 */
public class MainRelations {
    
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-relations.xml");
        SessionFactory sessionFactory = ctx.getBean(SessionFactory.class);

        OneToOneForeignKeyTest test1 = new OneToOneForeignKeyTest(sessionFactory);
        test1.run();
        
        OneToOneSharedPrimaryKey test2 = new OneToOneSharedPrimaryKey(sessionFactory);
        test2.run();
       
        OneToOneJoinTable test3 = new OneToOneJoinTable(sessionFactory);
        test3.run();
        
        OneToManyForeignKeyTest test4 = new OneToManyForeignKeyTest(sessionFactory);
        test4.run();
        
        OneToManyJoinTableTest test5 = new OneToManyJoinTableTest(sessionFactory);
        test5.run();
        
        ManyToManyTest test6 = new ManyToManyTest(sessionFactory);
        test6.run();
        
        MapTest test7 = new MapTest(sessionFactory);
        test7.run();
    }
    
}

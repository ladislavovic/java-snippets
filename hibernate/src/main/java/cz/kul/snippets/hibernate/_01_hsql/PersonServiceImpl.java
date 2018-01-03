package cz.kul.snippets.hibernate._01_hsql;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonServiceImpl implements PersonService {
 
    @Autowired
    private SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    @Transactional(readOnly=true)
    public List<Person>getAll() {
        Session s = session();
        Query q = s.createQuery("from " + Person.class.getName());
        List l = q.list();
        return l;
    }
    
    @Override
    @Transactional
    public Person insertPerson(String name) {
        Person person = new Person();
        person.setName(name);
        Serializable id = session().save(person);
        Person result = (Person) session().get(Person.class, id);
        return result;
    }
    
    @Override
    @Transactional
    public PersonDetail insertPersonDetail(Person person, String key, String value) {
        PersonDetail personDetail = new PersonDetail();
        personDetail.setPerson(person);
        personDetail.setKey(key);
        personDetail.setValue(value);
        Serializable id = session().save(personDetail);
        PersonDetail result = (PersonDetail) session().get(PersonDetail.class, id);
        return result;
    }

    @Override
    @Transactional
    public void criteriaTest() {
        System.out.println("Criteria test start");
        
        Query q = session().createSQLQuery("select id from Person").addScalar("id", LongType.INSTANCE);
        List data = q.list();
        System.out.println("data: " + data.get(0).getClass());
        System.out.println("data: " + data.size());
        System.out.println("data: " + data);
        
        
        if(true) return;
        
        
        basic: {
            Criteria crit = session().createCriteria(Person.class);
            System.out.println("\nbasic:" + crit.list());
        }
        ordering: {
            Criteria crit = session().createCriteria(Person.class);
            crit.addOrder(Order.desc("name"));
            System.out.println("\nordering:" + crit.list());
        }
        restrictions: {
            Criteria crit = session().createCriteria(Person.class);
            crit.add(Restrictions.eq("name", "Monica"));
            System.out.println("\nrestrictions:" + crit.list());
        }
        and_ors: {
            Criteria crit = session().createCriteria(Person.class);
            Criterion criterion1 = Restrictions.ilike("name", "monica");
            Criterion criterion2 = Restrictions.ilike("name", "jane");
            crit.add(Restrictions.or(criterion1, criterion2));
            System.out.println("\nrestrictions:" + crit.list());
        }
        sqlRestriction: {
            Criteria crit = session().createCriteria(Person.class);
            crit.add(Restrictions.sqlRestriction("{alias}.name like ? and {alias}.id > 0", "Mo%", StringType.INSTANCE));
            // {alias} is replaced by the table name of queried entity
            System.out.println("\nsql restriction:" + crit.list());
        }
        sqlRestriction2: {
            Criteria crit = session().createCriteria(Person.class);
            crit.add(Restrictions.sqlRestriction("{alias}.id in (select person_id from PersonDetail where value = ?)", "val1", StringType.INSTANCE));
            // {alias} is replaced by the table name of queried entity
            System.out.println("\nsql restriction:" + crit.list());
        }
        associationOneToMany: {
            // It creates Cartesian Product. It returns Person for every
            // PersonDetail, which is in accordance with criteria
            Criteria crit = session().createCriteria(Person.class);
            Criteria critOneToMany = crit.createCriteria("details"); // name of the property
            critOneToMany.add(Restrictions.isNotNull("value"));
            System.out.println("\nAssociations one-to-many: " + critOneToMany.list()); // I must list the "second" criteria
        }
        associationManyToOne: {
            Criteria crit = session().createCriteria(PersonDetail.class);
            Criteria critManyToOne = crit.createCriteria("person");
            critManyToOne.add(Restrictions.eq("name", "Monica"));
            System.out.println("\nAssociations many-to-one: " + critManyToOne.list());
        }
        aliases: {
            // Another way how to filter instances according to related entities
            Criteria crit = session().createCriteria(Person.class);
            crit.createAlias("details", "det");
            crit.add(Restrictions.eq("det.value", "val2"));
            System.out.println("\nAliases: " + crit.list());
        }
        
        System.out.println("Criteria test end");
    }
    
}

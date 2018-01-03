package cz.kul.snippets.hibernate._05_nativesql;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("PersonService")
public class PersonServiceImpl implements PersonService {
 
    @Autowired
    private SessionFactory sessionFactory;

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    @Transactional(readOnly=true)
    public List<Person5>getAll() {
        Session s = session();
        Query q = s.createQuery("from " + Person5.class.getName());
        List l = q.list();
        return l;
    }
    
    @Override
    @Transactional
    public Person5 insertPerson(String name) {
        Person5 person = new Person5();
        person.setName(name);
        Serializable id = session().save(person);
        Person5 result = (Person5) session().get(Person5.class, id);
        return result;
    }
    
    @Override
    @Transactional
    public PersonDetail5 insertPersonDetail(Person5 person, String key, String value) {
        PersonDetail5 personDetail = new PersonDetail5();
        personDetail.setPerson(person);
        personDetail.setKey(key);
        personDetail.setValue(value);
        Serializable id = session().save(personDetail);
        PersonDetail5 result = (PersonDetail5) session().get(PersonDetail5.class, id);
        return result;
    }
    
    @Override
    @Transactional
    public void testIt() {
        simpleQuery();
        queryWithAddScalar();
    }
    
    /**
     * This is the most simple sql query in hibernate. 
     * 
     * Note, that you do not specify what the query returns so it returns 
     * instances of BigInteger. It is a little bit unexpected and probably
     * depends on particular DB scheme.
     */
    private void simpleQuery() {
        String sql = "SELECT id FROM person5";
        SQLQuery query = session().createSQLQuery(sql);
        List<BigInteger> results = query.list();
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(BigInteger.class, results.get(0).getClass());
    }
    
    /**
     * By addScalar() method you can explicitely say what is returned
     * by the query. Then Hibernate do not work with ResultSetMetadata
     * and get metadata information from your specification. Notes:
     *   * it is quicker - no ResultSetMetadata overhead
     *   * you can specify what is to be returned - Integer, Long, BigInteger, ...
     *   * only columns specified by addScalar are returned. Even though there
     *     are more columns in "select part"
     *   * you do not have to specify type
     */
    private void queryWithAddScalar() {
        String sql = "SELECT * FROM person5";
        SQLQuery query = session().createSQLQuery(sql);
        query.addScalar("id", StandardBasicTypes.LONG);
        query.addScalar("name");
        List<Object[]> results = query.list();
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(2, results.get(0).length);
        Assert.assertEquals(Long.class, results.get(0)[0].getClass());
        Assert.assertEquals(String.class, results.get(0)[1].getClass());
    }
    
    

        
}

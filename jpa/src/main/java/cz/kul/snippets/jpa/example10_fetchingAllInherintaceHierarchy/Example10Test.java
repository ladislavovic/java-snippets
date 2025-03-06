package cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy;

import cz.kul.snippets.jpa.common.JPATest;
import cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model.Address;
import cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model.AddressPersonDetail;
import cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model.NickNamePersonDetail;
import cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model.Person;
import cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model.PersonDetail;
import cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model.Salary;
import cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model.SalaryPersonDetail;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class Example10Test extends JPATest {

	private static boolean initialized;
	private final static int noOfPeople = 30;

	@Before
	public void prepareData() {
		if (initialized) {
			return;
		}
		initialized = true;
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			for (int i = 0; i < noOfPeople; i++) {
				createPersonWithDetails("Monica_" + i, entityManager);
			}
			return null;
		});
	}

	@Override
	public String getJpaModelPackages() {
		return PersonDetail.class.getPackage().getName();
	}

	@Test
	public void testFetchOnlySomeDescendants() {
		// get by query with join fetch
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			String queryStr = "select distinct p " +
					"from Person p " +
					"left join fetch p.personDetails pd " +
					"left join fetch pd.address";  // you can do it this way even though "address" is property of one particular subtype
			TypedQuery<Person> query = entityManager.createQuery(queryStr, Person.class);
			System.out.println("Just befor the join fetch query...");
			List<Person> people = query.getResultList();

			Assert.assertEquals(noOfPeople, people.size());

			AddressPersonDetail addressPd = people.get(0).findPersonDetail(AddressPersonDetail.class);
			Assert.assertTrue(Hibernate.isInitialized(addressPd));
			Assert.assertTrue(Hibernate.isInitialized(addressPd.getDetail()));

			SalaryPersonDetail salaryPd = people.get(0).findPersonDetail(SalaryPersonDetail.class);
			Assert.assertTrue(Hibernate.isInitialized(salaryPd));
			Assert.assertFalse(Hibernate.isInitialized(salaryPd.getDetail()));

			return null;
		});

	}

	@Test
	public void testFetchAllDescendants() {
		// get by query with join fetch
		jpaService().doInTransactionAndFreshEM(entityManager -> {
			String queryStr = "select distinct p " +
					"from Person p " +
					"left join fetch p.personDetails pd " +
					"left join fetch pd.address " +
					"left join fetch pd.salary";
			TypedQuery<Person> query = entityManager.createQuery(queryStr, Person.class);
			System.out.println("Just befor the join fetch query...");
			List<Person> people = query.getResultList();

			Assert.assertEquals(noOfPeople, people.size());

			AddressPersonDetail addressPd = people.get(0).findPersonDetail(AddressPersonDetail.class);
			Assert.assertTrue(Hibernate.isInitialized(addressPd));
			Assert.assertTrue(Hibernate.isInitialized(addressPd.getDetail()));

			SalaryPersonDetail salaryPd = people.get(0).findPersonDetail(SalaryPersonDetail.class);
			Assert.assertTrue(Hibernate.isInitialized(salaryPd));
			Assert.assertTrue(Hibernate.isInitialized(salaryPd.getDetail()));

			return null;
		});

	}

	@Test
	public void testInitializeSalaryByFetchProfile() {

		// Generally it is not good idea to use fetch profiles to initialize
		// rest of properties. It cause N + 1 problem, there is not optimization.
		//
		// so use "join fetch" and join all you need.

		jpaService().doInTransactionAndFreshEM(entityManager -> {

			Session session = entityManager.unwrap(Session.class);
			session.enableFetchProfile(SalaryPersonDetail.FP_SALARY);

			String queryStr = "select distinct p " +
					"from Person p " +
					"left join fetch p.personDetails pd " +
					"left join fetch pd.address";

			TypedQuery<Person> query = entityManager.createQuery(queryStr, Person.class);
			System.out.println("Just befor the join fetch query...");
			List<Person> people = query.getResultList();

			Assert.assertEquals(noOfPeople, people.size());

			AddressPersonDetail addressPd = people.get(0).findPersonDetail(AddressPersonDetail.class);
			Assert.assertTrue(Hibernate.isInitialized(addressPd));
			Assert.assertTrue(Hibernate.isInitialized(addressPd.getDetail()));

			SalaryPersonDetail salaryPd = people.get(0).findPersonDetail(SalaryPersonDetail.class);
			Assert.assertTrue(Hibernate.isInitialized(salaryPd));
			Assert.assertTrue(Hibernate.isInitialized(salaryPd.getDetail()));

			return null;
		});

	}

	public Person createPersonWithDetails(String personName, EntityManager entityManager) {
		Person person = new Person();
		person.setName(personName);
		entityManager.persist(person);

		Address address = new Address();
		address.setAddress("Manhatten");
		entityManager.persist(address);

		Salary salary = new Salary();
		salary.setSalary(3000);
		entityManager.persist(salary);

		AddressPersonDetail addressPersonDetail = new AddressPersonDetail();
		addressPersonDetail.setDetail(address);
		addressPersonDetail.setPerson(person);
		entityManager.persist(addressPersonDetail);

		SalaryPersonDetail salaryPersonDetail = new SalaryPersonDetail();
		salaryPersonDetail.setDetail(salary);
		salaryPersonDetail.setPerson(person);
		entityManager.persist(salaryPersonDetail);

		NickNamePersonDetail nickNamePersonDetail = new NickNamePersonDetail();
		nickNamePersonDetail.setDetail("Beauty");
		nickNamePersonDetail.setPerson(person);
		entityManager.persist(nickNamePersonDetail);

		return person;
	}

}

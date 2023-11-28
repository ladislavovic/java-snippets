package cz.kul.snippets.java.creating_comparator_by_comparing_method_and_the_like;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreatingComparatorByComparingMethodAndTheLike {

	@Test
	public void createComparatorBy_comparing() {
		Employee r = new Employee("rachel", 30, 10000, "111111111");
		Employee m = new Employee("monica", 31, 20000, "222222222");
		Employee p = new Employee("phoeboe", 29, 5000, null);
		var list = new ArrayList<>(List.of(r, m, p));

		// Here you can compare element by name. The name can not be null!
		list.sort(Comparator.comparing(Employee::getName));
		Assert.assertEquals(List.of(m, p, r), list);

		// Here is the same but reversed order
		list.sort(Comparator.comparing(Employee::getName).reversed());
		Assert.assertEquals(List.of(r, p, m), list);

		// You can also easily sort null elements as first/last
		var list2 = new ArrayList();
		list2.add(r);
		list2.add(null);
		list2.sort(Comparator.nullsFirst(Comparator.comparing(Employee::getName)));
		Assert.assertNull(list2.get(0));

		// You can also create a comparator, when the attribute you are comparing according to
		// have sometimes null value
		Comparator<Employee> comp = Comparator.comparing(
				  Employee::getMobile,
				  Comparator.nullsFirst(Comparator.naturalOrder()));
		list.sort(comp);
		Assert.assertEquals(List.of(p, r, m), list);
	}




	private static class Employee {
		String name;
		int age;
		int salary;
		String mobile;

		public Employee(String name, int age, int salary, String mobile) {
			this.name = name;
			this.age = age;
			this.salary = salary;
			this.mobile = mobile;
		}

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}

		public int getSalary() {
			return salary;
		}

		public String getMobile() {
			return mobile;
		}
	}

}

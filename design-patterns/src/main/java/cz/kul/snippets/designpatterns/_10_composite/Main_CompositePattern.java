package cz.kul.snippets.designpatterns._10_composite;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * It is for situations, when an application needs to manipulate tree data structure in a
 * unified way. Then it is good to create a common interface for all types of nodes
 * (leafs, branches, ...).
 */
public class Main_CompositePattern {

    public static void main(String[] args) {
        // In this example you can get people from any level of Organisation unit
        WorkGroup workGroup = new WorkGroup();
        workGroup.addPerson("Pepa");
        workGroup.addPerson("Franta");
        WorkGroup workGroup2 = new WorkGroup();
        workGroup2.addPerson("Jana");
        workGroup2.addPerson("Nikola");

        List<WorkGroup> workGroups = Arrays.asList(workGroup, workGroup2);
        List<Department> departments = Arrays.asList(new Department(workGroups));
        Company company = new Company(departments);
        assertEquals(Arrays.asList("Pepa", "Franta", "Jana", "Nikola"), company.getPeople());
    }

}

interface OrganisationUnit {
    List<String> getPeople();
}

class Company implements OrganisationUnit {

    List<Department> departments = new ArrayList<>();

    public Company(List<Department> departments) {
        super();
        this.departments = departments;
    }

    @Override
    public List<String> getPeople() {
        List<String> result = new ArrayList<>();
        departments.forEach(x -> result.addAll(x.getPeople()));
        return result;
    }

}

class Department implements OrganisationUnit {

    List<WorkGroup> workGroups = new ArrayList<>();

    public Department(List<WorkGroup> workGroups) {
        super();
        this.workGroups = workGroups;
    }

    @Override
    public List<String> getPeople() {
        List<String> result = new ArrayList<>();
        workGroups.forEach(x -> result.addAll(x.getPeople()));
        return result;
    }

}

class WorkGroup implements OrganisationUnit {

    List<String> people = new ArrayList<>();

    @Override
    public List<String> getPeople() {
        return people;
    }

    public void addPerson(String person) {
        people.add(person);
    }

}

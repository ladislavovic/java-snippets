package cz.kul.snippets.mockito._5_arguments;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;

/**
 * How to specify return value for particular argument.
 * 
 * @author kulhalad
 */
public class Main {

    public static void main(String[] args) {
    	
    	// For "normal" argument mockito use .equals() method
    	{
	        Person m = Mockito.mock(Person.class);
	        doReturn(true).when(m).isDtoFilled(new PersonDto("Jane", 20));
	        System.out.println("by equals: " + m.isDtoFilled(new PersonDto("Jane", 20)));
    	}
    	
    	// You can use argument matchers
    	// TODO
    }
    
    public static class Person {
        String name;
        Integer age;
     
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
		}
		public boolean isDtoFilled(PersonDto dto) {
			return dto.name != null && dto.age != null;
		}
    }
    
    public static class PersonDto {
    	String name;
    	Integer age;
    	    	
		public PersonDto(String name, Integer age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((age == null) ? 0 : age.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PersonDto other = (PersonDto) obj;
			if (age == null) {
				if (other.age != null)
					return false;
			} else if (!age.equals(other.age))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}    	
    }

}

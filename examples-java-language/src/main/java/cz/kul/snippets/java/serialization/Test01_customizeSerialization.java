package cz.kul.snippets.java.serialization;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Test01_customizeSerialization {
    
    public static class Person implements Serializable {
        private String name;
        private Date serializationTime;

        public Person(String name) {
            this.name = name;
        }
        
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject(); // this run standard serialization process
            out.writeObject(new Date());
        }
        
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject(); // this run standard deserialization process
            serializationTime = (Date) in.readObject();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getSerializationTime() {
            return serializationTime;
        }
    }
    
    @Test
    public void customizeSerialization_SaveDate() {
        byte[] bytes = SerializationUtils.serialize(new Person("monica"));
        Person person = SerializationUtils.deserialize(bytes);
        assertEquals("monica", person.getName());
        assertNotNull(person.getSerializationTime());
    }
    
}

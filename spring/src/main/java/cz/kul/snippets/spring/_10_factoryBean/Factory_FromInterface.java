package cz.kul.snippets.spring._10_factoryBean;

import org.springframework.beans.factory.FactoryBean;

public class Factory_FromInterface implements FactoryBean<Bean1> {

    private String factoryId;
    private String fooId;
 
    @Override
    public Bean1 getObject() throws Exception {
        return new Bean1(fooId);
    }
 
    @Override
    public Class<?> getObjectType() {
        return Bean1.class;
    }
 
    @Override
    public boolean isSingleton() {
        return false;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFooId() {
        return fooId;
    }

    public void setFooId(String fooId) {
        this.fooId = fooId;
    }
 
}

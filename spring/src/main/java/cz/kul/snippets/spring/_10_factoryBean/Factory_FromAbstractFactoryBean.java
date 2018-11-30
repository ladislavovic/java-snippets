package cz.kul.snippets.spring._10_factoryBean;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public class Factory_FromAbstractFactoryBean extends AbstractFactoryBean<Bean1> {

    public Factory_FromAbstractFactoryBean() {
        setSingleton(false);
    }
    
    @Override
    public Class<?> getObjectType() {
        return Bean1.class;
    }

    @Override
    protected Bean1 createInstance() throws Exception {
        return new Bean1("fooId");
    }

}

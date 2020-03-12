package cz.kul.snippets.jpa.example09_envers.case02_missingRevisionOnOptionalRelation.hibernatecustomization;

import org.hibernate.bytecode.internal.bytebuddy.BytecodeProviderImpl;
import org.hibernate.cfg.Environment;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.tuple.entity.PojoEntityTuplizer;

public class EnversEmptyEntityIfNotFoundTuplizer extends PojoEntityTuplizer {
    
    public EnversEmptyEntityIfNotFoundTuplizer(EntityMetamodel entityMetamodel, PersistentClass mappedEntity) {
        super(entityMetamodel, mappedEntity);
    }


    @Override
    protected ProxyFactory buildProxyFactoryInternal(PersistentClass persistentClass, Getter idGetter, Setter idSetter) {

        BytecodeProviderImpl bytecodeProvider = (BytecodeProviderImpl) Environment.getBytecodeProvider();

        return new EnversEmptyEntityIfNotFoundProxyFactory(bytecodeProvider.getByteBuddyProxyHelper());
    }

}

package org.hibernate.envers.internal.entities.mapper.relation;

import org.hibernate.envers.boot.internal.EnversService;
import org.hibernate.envers.internal.entities.mapper.relation.lazy.ToOneDelegateSessionImplementor;
import org.hibernate.envers.internal.reader.AuditReaderImplementor;
import org.hibernate.persister.entity.EntityPersister;

import java.io.Serializable;

public final class ToOneEntityLoader2 {
    private ToOneEntityLoader2() {
    }

    /**
     * Immediately loads historical entity or its current state when excluded from audit process. Returns {@code null}
     * reference if entity has not been found in the database.
     */
    public static Object loadImmediate(
            AuditReaderImplementor versionsReader,
            Class<?> entityClass,
            String entityName,
            Object entityId,
            Number revision,
            boolean removed,
            EnversService enversService) {
        Object entity;
        if ( enversService.getEntitiesConfigurations().getNotVersionEntityConfiguration( entityName ) == null ) {
            // Audited relation, look up entity with Envers.
            // When user traverses removed entities graph, do not restrict revision type of referencing objects
            // to ADD or MOD (DEL possible). See HHH-5845.
            entity = versionsReader.find( entityClass, entityName, entityId, revision, removed );
        }
        else {
            // Not audited relation, look up entity with Hibernate.
            entity = versionsReader.getSessionImplementor().immediateLoad( entityName, (Serializable) entityId );
        }
        if (entity == null) {
            entity = createEmptyInstance(entityClass);
        }
        return entity;
    }
    
    private static Object createEmptyInstance(Class<?> entityClass) {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("An error while creating empty entity of class " + entityClass, e);
        }
    }

    /**
     * Creates proxy of referenced *-to-one entity.
     */
    public static Object createProxy(
            AuditReaderImplementor versionsReader,
            Class<?> entityClass,
            String entityName,
            Object entityId,
            Number revision,
            boolean removed,
            EnversService enversService) {
        final EntityPersister persister = versionsReader.getSessionImplementor()
                .getFactory()
                .getMetamodel()
                .entityPersister( entityName );
        return persister.createProxy(
                (Serializable) entityId,
                new ToOneDelegateSessionImplementor( versionsReader, entityClass, entityId, revision, removed, enversService )
        );
    }

    /**
     * Creates Hibernate proxy or retrieves the complete object of an entity if proxy is not
     * allowed (e.g. @Proxy(lazy=false), final class).
     */
    public static Object createProxyOrLoadImmediate(
            AuditReaderImplementor versionsReader,
            Class<?> entityClass,
            String entityName,
            Object entityId,
            Number revision,
            boolean removed,
            EnversService enversService) {
        final EntityPersister persister = versionsReader.getSessionImplementor()
                .getFactory()
                .getMetamodel()
                .entityPersister( entityName );
        if ( persister.hasProxy() ) {
            return createProxy( versionsReader, entityClass, entityName, entityId, revision, removed, enversService );
        }
        return loadImmediate( versionsReader, entityClass, entityName, entityId, revision, removed, enversService );
    }
}

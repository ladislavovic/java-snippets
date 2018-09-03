TODO comb it

/**
 * <h1>JPA</h1>
 * 
 * <h2>Persostence Context</h2>
 * <p>
 * A persistence context is like a cache which contains a set of persistent
 * entities, So once the transaction is finished, all persistent objects are
 * detached from the EntityManager's persistence context and are no longer
 * managed
 * </p>
 *
 * <h2>Entity Manager</h2>
 * <p>
 * It is an interface which allows you to interact with persitence contex, that
 * is persist entities, find entities, remove entities, ...
 * </p>
 * 
 * <h2>Persistence Unit</h2>
 * <p>Set of entity classes and some additional config information</p>
 *
 */
 
 
 if you save whole structure starting from parent, you have to set
  link to parent in child entity manually. Hibernate does not do it
  automatically. The methods which manage it are called "link management
  methods"
 
 
 TODO describe states: persisted, detached, ...
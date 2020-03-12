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
  
### Envers
* revisions data are stored whenever you commit a transaction. Changes on the entity during the transactions
  are ignored.
* the last revision is the same as the current entity state.
* when you get an entity with the specified revision number, you does not have to obtain the entity exactly
  with the specified revision. You obtain max available revision, which is less or equal as the specified 
  revision.
  Because the rivision numbers are common for the whole db. We can say one commit is one revision number.
  For example your Person entity with ID 1 can be in revisons 1, 6 and 12. When you want version in revision
  10, you get the revision 6, because this is tha state which was at the "commit 10".
* The main limitation of the current queries implementation is that you cannot traverse relations.



 TODO describe states: persisted, detached, ...
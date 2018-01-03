package cz.kul.snippets.jpa._1transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cz.kul.snippets.jpa.model.Foo;

@Service
public class FooService {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void createFoo() {
		Foo foo = new Foo();
		foo.setEmail("ab@b.c");
		entityManager.persist(foo);
		entityManager.flush();

		if (true)
			throw new RuntimeException("e");
	}

}

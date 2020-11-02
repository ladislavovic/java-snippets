package cz.kul.snippets.bytebuddy.example01_hw;

import cz.kul.snippets.SnippetsTest;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Assert;
import org.junit.Test;

public class ByteBuddyTest extends SnippetsTest {

	@Test
	public void dynamicClassFromNormalClass() throws Exception {
		DynamicType.Unloaded unloadedType = new ByteBuddy()
				.subclass(Object.class)
				.method(ElementMatchers.isToString())
				.intercept(FixedValue.value("Hello World ByteBuddy!"))
				.make();
		Class<?> loadedType = unloadedType.load(getClass().getClassLoader()).getLoaded();
		Assert.assertEquals("Hello World ByteBuddy!", loadedType.newInstance().toString());
	}

	public static abstract class AbstractClass {
	}

	@Test
	public void dynamicClassFromAbstractClass() throws Exception {
		Class<? extends AbstractClass> clazz = new ByteBuddy()
				.subclass(AbstractClass.class)
				.method(ElementMatchers.isToString())
				.intercept(FixedValue.value("from abstract class"))
				.make()
				.load(getClass().getClassLoader())
				.getLoaded();
		Assert.assertEquals("from abstract class", clazz.newInstance().toString());
	}

	public static final class FinalClass {
	}

	@Test
	public void youCanNotSubclassFinalClass() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new ByteBuddy().subclass(FinalClass.class).make());
	}


	public interface Interface1 {

	}

	public interface Interface2 {

	}

	@Test
	public void implementInterface() throws Exception {
		Object o = new ByteBuddy()
				.subclass(Object.class)
				.implement(Interface1.class)
				.implement(Interface2.class)
				.make()
				.load(getClass().getClassLoader())
				.getLoaded()
				.newInstance();
		Assert.assertTrue(o instanceof Interface1);
		Assert.assertTrue(o instanceof Interface2);
	}

	public static class ClassWithoutDefaultConstructor {
		public ClassWithoutDefaultConstructor(String str) { }
	}

	@Test
	public void dynamicClassWithNoDefaultConstructor() throws Exception {
		Object o = new ByteBuddy()
				.subclass(ClassWithoutDefaultConstructor.class)
			  .make()
				.load(getClass().getClassLoader())
				.getLoaded()
				.newInstance();
		Assert.assertTrue(o instanceof ClassWithoutDefaultConstructor);
	}


}

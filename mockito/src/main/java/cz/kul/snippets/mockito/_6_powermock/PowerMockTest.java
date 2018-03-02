package cz.kul.snippets.mockito._6_powermock;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * // TODO move it back to SRC directory. Maven could not execute test there...
 * 
 * Power mock is mock library, which can modify bytecode. It
 * allows you to mock static methods, private methods, final classes, ...
 * 
 * It has an API for junit and Mockito so you can use it in similar way
 * as normal Mockito mocking.
 * 
 * There is an table on the project wabpage which particular version
 * of PowerMock you need for particular version of JUnit. You
 * can not combine versions freely.
 * 
 * I had problems with running it as "normal" java class so I modify
 * it to Junit test.
 */


// You must use this runner if you want to use
// PowerMock features. It is probably because
// of classloading things ...
@RunWith(PowerMockRunner.class)
// You must use this annotation for classes, which
// you want mock by PowerMock
@PrepareForTest(Foo.class)
public class PowerMockTest {

	@Test
	public void test() throws Exception {
		//
		// (1) Mock static method
		//
		                               
		// mock all the static methods in a class called "Static"
		PowerMockito.mockStatic(Foo.class);
                
                // mock behaviour
		Mockito.when(Foo.staticMethod()).thenReturn("Mocked");
                Assert.assertEquals("Mocked", Foo.staticMethod());
                
                // mock behaviour for methods with params
                Mockito.when(Foo.staticMethod2("str1", new Date(0))).thenReturn("mocked1");
		Mockito.when(Foo.staticMethod2("str2", new Date(0))).thenReturn("mocked2");
                Assert.assertEquals("mocked1", Foo.staticMethod2("str1", new Date(0)));
		Assert.assertEquals("mocked2", Foo.staticMethod2("str2", new Date(0)));
                
                // mock behaviour another way
                PowerMockito.doReturn("MockerAnotherWay").when(Foo.class, "staticMethod2", null, null);
                Assert.assertEquals("MockerAnotherWay", Foo.staticMethod2(null, null));		
	}
	
}

class Foo {
	
	public static String staticMethod() {
		return "static";
	}
	
	public static String staticMethod2(String arg1, Date arg2) {
		return "static2";
	}
		
}

package cz.kul.snippets.mvel.example01_hw;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.mvel2.MVEL;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import java.util.Arrays;
import java.util.Date;

public class TestMvelHw {

	@Test
	public void test() {

		ImmutableMap<String, Object> variables = ImmutableMap.of(
				"name", "Monica",
				"age", 29,
				"timestamp", new Date(0),
				"friends", Arrays.asList("Rachel", "Phoeboe"));

		MapVariableResolverFactory vs = new MapVariableResolverFactory(variables);
		Assert.assertEquals("Monica", MVEL.eval("name", vs));
		Assert.assertEquals("Monica_aaa", MVEL.eval("name + \"_aaa\"", vs));
		Assert.assertEquals("MONICA", MVEL.eval("name.toUpperCase()", vs));
		Assert.assertEquals(29, MVEL.eval("age", vs));
		Assert.assertEquals(30, MVEL.eval("age+1", vs));
		Assert.assertEquals(Lists.newArrayList("Rachel", "Phoeboe"), MVEL.eval("friends", vs));

		Assert.assertEquals(
				"Rachel, Phoeboe",
				MVEL.eval("org.apache.commons.lang.StringUtils.join(friends, \", \")", vs));

		Assert.assertEquals(
				"Rachel, Phoeboe",
				MVEL.eval("friends.stream().collect(java.util.stream.Collectors.joining(\", \"))", vs));

		Assert.assertEquals(
				29,
				MVEL.eval("if (age < 30) {return age;} else {return 0}", vs));
	}

}

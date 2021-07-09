package cz.kul.snippets.spring.example_24_spel;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

public class SpELTest {

	@Test
	public void test() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("T(cz.kul.snippets.spring.example_24_spel.ForbiddenClass).staticDangerousOperation()");
		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		Object value = (String) exp.getValue(evaluationContext);
		System.out.println(value);
	}

	@Test
	public void test2() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("T(java.lang.System).exit(1)");

//		Object value = (String) exp.getValue(new StandardEvaluationContext());

		Object value = exp.getValue(SimpleEvaluationContext
				.forReadOnlyDataBinding()
				.build());

		System.out.println(value);
	}


	@Test
	public void test_root() {
		Map<String, Object> variables = ImmutableMap.of(
				"var1", "StringValue",
				"var2", 10,
				"var3", true);

		StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
		evaluationContext.setVariables(variables);
		ExpressionParser parser = new SpelExpressionParser();

		Expression exp = parser.parseExpression("#var1 + ' ' + #var2");
		Object value = (String) exp.getValue(evaluationContext);
		System.out.println(value);
	}


}

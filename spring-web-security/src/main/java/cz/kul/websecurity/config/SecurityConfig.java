package cz.kul.websecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("mkyong").password("123456").roles("USER");
		auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.authorizeRequests()
//		    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//		    .antMatchers("/dba/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')")
//		    .and().formLogin();
//
//	}

	@Configuration
	@Order(1)
	public static class Security1 extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.antMatcher("/admin/**")
			.authorizeRequests()
			.anyRequest().hasRole("ADMIN")
			.and()
			.httpBasic();
		}
		
	}
	
	@Configuration
	@Order(2)
	public static class Security2 extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			  .antMatcher("/dba/**")
			  .authorizeRequests()
			    .anyRequest().hasRole("DBA")
			    .and()
			  .formLogin();
		}
		
	}

//	@Configuration
//	public static class Security3 extends WebSecurityConfigurerAdapter {
//
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			http
//			  .authorizeRequests()
//			    .anyRequest().authenticated()
//			    .and()
//			  .formLogin();
//		}
//
//	}

}

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {
//
// @Autowired
// public void configureGlobal(AuthenticationManagerBuilder auth) throws
// Exception {
// auth.inMemoryAuthentication().withUser("mkyong").password("123456").roles("USER");
// auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
// auth.inMemoryAuthentication().withUser("dba").password("123456").roles("DBA");
// }
//
//// @Configuration
//// @Order(1)
//// public static class Security1 extends WebSecurityConfigurerAdapter {
////
//// @Override
//// protected void configure(HttpSecurity http) throws Exception {
//// http
//// .antMatcher("/admin/**")
//// .authorizeRequests()
//// .anyRequest().hasRole("ADMIN")
//// .and()
//// .httpBasic();
//// }
////
//// }
//
// @Configuration
//// @Order(2)
// public static class Security2 extends WebSecurityConfigurerAdapter {
//
// @Override
// protected void configure(HttpSecurity http) throws Exception {
// http
// .antMatcher("/dba/**")
// .authorizeRequests()
// .anyRequest().hasRole("DBA")
// .and()
// .formLogin();
// }
//
// }
//
// }
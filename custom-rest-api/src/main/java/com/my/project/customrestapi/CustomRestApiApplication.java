package com.my.project.customrestapi;

import com.cross_ni.cross.app_logic.configuration.ApplicationLogicContextConfiguration;
import com.cross_ni.cross.db.config.DbContextConfiguration;
import com.cross_ni.cross.exchange_format.config.ExchangeFormatContextConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(
		exclude = {
			FreeMarkerAutoConfiguration.class,
			DataSourceAutoConfiguration.class,
			DataSourceTransactionManagerAutoConfiguration.class,
			HibernateJpaAutoConfiguration.class})
@Import(ApplicationLogicContextConfiguration.class)
public class CustomRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomRestApiApplication.class, args);
	}

}

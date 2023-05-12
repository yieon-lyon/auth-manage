package com.yieon.practice.auth.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-14
 * <PRE>
 * com.yieon.practice.auth.service
 *     |DataSourceConfiguration.java
 * ------------------------
 * summary : 공용 라이브러리의 Domain 및 Repository 접근을 위한 DataSource Configuration
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.yieon.practice.auth.lib.repository.auth", "com.yieon.practice.auth.lib.repository.account"})
public class DataSourceConfiguration {

	@Autowired
	private Environment env;

	@Primary
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Primary
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("dataSource") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean bean = builder
				.dataSource(dataSource)
				.packages("com.yieon.practice.auth.lib.model.domain.auth", "com.yieon.practice.auth.lib.model.domain.account")
				.persistenceUnit("auth")
				.build();

		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.implicit_naming_strategy", env.getProperty("spring.jpa.hibernate.naming.implicit-strategy"));
		properties.put("hibernate.physical_naming_strategy", env.getProperty("spring.jpa.hibernate.naming.physical-strategy"));
		bean.setJpaPropertyMap(properties);

		return bean;
	}

	@Primary
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}

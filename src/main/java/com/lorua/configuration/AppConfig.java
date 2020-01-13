package com.lorua.configuration;

import java.util.Properties;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("com.fergus.lorua.domain")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@EnableNeo4jRepositories(basePackages = "com.lorua.repository")
public class AppConfig {

	@Value("${spring.data.neo4j.username}")
	String neo4jUser;

	@Value("${spring.data.neo4j.password}")
	String neo4jPass;

	@Value("${spring.data.neo4j.uri}")
	String neo4jUri;

	@Value("${spring.mail.host}")
	String smtpHost;

	@Value("${spring.mail.username}")
	String emailUser;

	@Value("${spring.mail.password}")
	String emailPass;

	@Value("${spring.mail.port}")
	int smtpPort;

	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
		return (new org.neo4j.ogm.config.Configuration.Builder()).uri(neo4jUri).credentials(neo4jUser, neo4jPass)
				.build();
	}

	@Bean
	public SessionFactory getSessionFactory() {
		return new SessionFactory(getConfiguration(), "com.lorua.domain");
	}

	@Bean
	public Neo4jTransactionManager transactionManager() {
		return new Neo4jTransactionManager(getSessionFactory());
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(smtpHost);
		mailSender.setPort(smtpPort);

		mailSender.setUsername(emailUser);
		mailSender.setPassword(emailPass);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}
}

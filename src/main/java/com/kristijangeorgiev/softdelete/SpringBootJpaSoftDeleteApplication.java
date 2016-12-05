package com.kristijangeorgiev.softdelete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.kristijangeorgiev.softdelete.util.CustomJpaRepositoryFactoryBean;

/**
 * 
 * @author Kristijan Georgiev
 * 
 *         Main Spring Boot class where we enable the usage of our custom
 *         JpaRepositoryFactoryBean that allows us to use the soft delete
 *         functionality
 *
 */

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
public class SpringBootJpaSoftDeleteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaSoftDeleteApplication.class, args);
	}
}

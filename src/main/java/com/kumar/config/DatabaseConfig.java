package com.kumar.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//@Configuration
public class DatabaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.universitydatasource")
    public  DataSource universitydataSource(){
        return DataSourceBuilder.create().build();
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.postgresdatasource")
    public  DataSource postgresdataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public EntityManagerFactory postgressqlEntityManagerFactory(){
        LocalContainerEntityManagerFactoryBean lem = new LocalContainerEntityManagerFactoryBean();

        lem.setDataSource(postgresdataSource());
        lem.setPackagesToScan("package com.kumar.entity");
        lem.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        lem.setPersistenceProviderClass(HibernatePersistenceProvider.class);

        return lem.getObject();
    }

    @Bean
    @Primary
    public JpaTransactionManager jpaTransactionManager(){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();

        jpaTransactionManager.setDataSource(universitydataSource());
        jpaTransactionManager.setEntityManagerFactory(postgressqlEntityManagerFactory());
        return  jpaTransactionManager;
    }
}

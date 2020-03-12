package com.roche.products.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.roche.products.repository")
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${products.mongodb.database}")
    private String databaseName;

    @Value("${products.mongodb.host}")
    private String hostname;

    @Value("${products.mongodb.port}")
    private int port;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient() {
        String uri = String.format("mongodb://%s:%d", hostname, port);
        return MongoClients.create(uri);
    }
}
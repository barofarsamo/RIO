package com.riyobox.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.riyobox.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        // Extract database name from URI or use default
        try {
            ConnectionString connectionString = new ConnectionString(mongoUri);
            String database = connectionString.getDatabase();
            return database != null ? database : "riyobox";
        } catch (Exception e) {
            return "riyobox";
        }
    }

    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.riyobox");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate template = new MongoTemplate(mongoClient(), getDatabaseName());

        // Create indexes on startup
        MongoMappingContext mappingContext = (MongoMappingContext) template.getConverter().getMappingContext();
        mappingContext.setAutoIndexCreation(true);

        MongoPersistentEntityIndexResolver indexResolver = new MongoPersistentEntityIndexResolver(mappingContext);

        // Create indexes for all domain classes
        mappingContext.getPersistentEntities()
                .forEach(entity -> {
                    IndexOperations indexOps = template.indexOps(entity.getType());
                    indexResolver.resolveIndexFor(entity.getType()).forEach(indexOps::ensureIndex);
                });

        return template;
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}

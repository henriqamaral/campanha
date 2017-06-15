package br.com.torcedor.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	@Autowired
	private Environment env;
	
	@Override
	protected String getDatabaseName() {
		return env.getProperty("MONGODB_DATABASE");
	}

	@Override
	@Bean
	public MongoClient mongo() throws Exception {
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.maxConnectionIdleTime(60000);
		MongoClientURI mongoClientURI = new MongoClientURI(env.getProperty("MONGODB_URI"), builder);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		return mongoClient;
	}
	
	@Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }

}
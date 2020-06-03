package com.kalaha.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;

/**
 * The Class for Configuring mongo db.
 * @author Praveen
 */
@Configuration
public class MongoDbConfiguration {

	/** The Constant MONGO_DB_URL. */
	private static final String MONGO_DB_URL = "localhost";

	/** The Constant MONGO_DB_NAME. */
	private static final String MONGO_DB_NAME = "kalaha_embedded_db";

	/**
	 * Mongo template for embedded mongo DB.
	 * 
	 *
	 * @return the mongo template
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("deprecation")
	@Bean
	public MongoTemplate mongoTemplate() throws IOException {
		EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
		mongo.setBindIp(MONGO_DB_URL);
		MongoClient mongoClient = mongo.getObject();
		return new MongoTemplate(mongoClient, MONGO_DB_NAME);
	}
}

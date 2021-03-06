package it.cs.contact.tracing.be.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import it.cs.contact.tracing.be.entity.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositiveContactsRepository {

	private static final Logger logger = LoggerFactory.getLogger(PositiveContactsRepository.class);

	final DynamoDBMapper dbMapper;

	public PositiveContactsRepository() {

		this.dbMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().build());
	}

	public List<Contact> getPositiveContactByDeviceKey(final String deviceKey) {

		logger.info("Get Positive contact By DeviceKey : {}", deviceKey);

		try {

			final Map<String, AttributeValue> filters = new HashMap<>();
			filters.put(":f1", new AttributeValue().withS(deviceKey));

			final Map<String, String> expressionAttributeNames = new HashMap<>();
			expressionAttributeNames.put("#n1", "device-key");

			final DynamoDBQueryExpression<Contact> queryExpression = new DynamoDBQueryExpression<Contact>()
					.withKeyConditionExpression("#n1 = :f1")
					.withExpressionAttributeValues(filters)
					.withExpressionAttributeNames(expressionAttributeNames);

			return dbMapper.query(Contact.class, queryExpression);
		}
		catch (final Exception e) {
			logger.error("Error extracting data", e);
			return Collections.emptyList();
		}
	}

	public Contact create(final Contact contactEntity) {

		logger.info("Create contactEntity: {}", contactEntity.getDeviceKey());

		try {

			if (contactEntity.isValid()) {

				return dbMapper.save(contactEntity);
			}
		}
		catch (final Exception e) {
			logger.error("Error creating contactEntity.", e);
		}
		return new Contact();
	}
}
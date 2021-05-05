package it.cs.contact.tracing.be.dao;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import it.cs.contact.tracing.be.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.cs.contact.tracing.be.utils.Util.dateToString;

public class PositiveContactsDao {

	private static final Logger logger = LoggerFactory.getLogger(PositiveContactsDao.class);

	private static final String DEVICE_KEY_FIELD_NAME = "device-key";


	private static final String POSITIVE_CONTACTS_TABLE_NAME = "";

	final DynamoDBMapper dbMapper;

	public PositiveContactsDao() {

		this.dbMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().build());
	}

	public List<Contact> getPositiveContactByDeviceKey(final String deviceKey, final LocalDate date) {

		logger.info("Get Positive contact By DeviceKey : {}", deviceKey);

		try {

			final Map<String, AttributeValue> filters = new HashMap<>();
			filters.put(":f1", new AttributeValue().withS(deviceKey));
			filters.put(":f2", new AttributeValue().withN(dateToString(date)));

			final DynamoDBQueryExpression<Contact> queryExpression = new DynamoDBQueryExpression<Contact>()
					.withKeyConditionExpression("device-key = :f1 and communicated-on >= :f2")
					.withExpressionAttributeValues(filters);

			return dbMapper.query(Contact.class, queryExpression);

		}
		catch (final Exception e) {
			logger.error("Error extracting data for device-key: {}", deviceKey);
			return Collections.emptyList();
		}
	}

	public boolean create(final Contact contact) {

		logger.info("Create contact: {}", contact.getDevicekey());

		try {

			dbMapper.save(contact);
			return true;
		}
		catch (final Exception e) {
			logger.error("Error creating contact.", e);
			return false;
		}
	}
}
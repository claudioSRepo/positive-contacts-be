package it.cs.contact.tracing.be.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "positive-contacts")
@Builder
@JsonInclude
public class Contact {

	@DynamoDBHashKey(attributeName = "device-key")
	@JsonProperty
	private String devicekey;

	@DynamoDBAttribute(attributeName = "communicated-on")
	@JsonProperty
	private int communicatedOn;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Contact{");
		sb.append("devicekey='").append(devicekey).append('\'');
		sb.append(", communicatedOn=").append(communicatedOn);
		sb.append('}');
		return sb.toString();
	}
}

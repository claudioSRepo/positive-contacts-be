package it.cs.contact.tracing.be.model;

import lombok.Builder;

import java.util.List;

@Builder
public class LambdaResponse {

	private final List<Contact> contacts;

	private final boolean processed;
}

package it.cs.contact.tracing.be;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.cs.contact.tracing.be.dao.PositiveContactsDao;
import it.cs.contact.tracing.be.model.Contact;
import it.cs.contact.tracing.be.model.LambdaResponse;
import it.cs.contact.tracing.be.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static it.cs.contact.tracing.be.utils.Util.isValidDate;
import static it.cs.contact.tracing.be.utils.Util.stringToDate;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, LambdaResponse> {

  private static final Logger logger = LoggerFactory.getLogger(Handler.class);

  final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  final PositiveContactsDao positiveContactsDao = new PositiveContactsDao();


  @Override
  public LambdaResponse handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {

    Util.logEnvironment(event, context, gson);

    switch (event.getHttpMethod()) {

      case "GET":
        return LambdaResponse.builder().contacts(handleGet(event, context)).build();

      case "POST":
        return LambdaResponse.builder().processed(handlePost(event, context)).build();

      default:
        return LambdaResponse.builder().processed(false).build();
    }
  }

  private boolean handlePost(final APIGatewayProxyRequestEvent event, final Context context) {

    final Contact body = gson.fromJson(event.getBody(), Contact.class);

    logger.info("Requested POST with body : {}", body);

    return positiveContactsDao.create(body);
  }

  private List<Contact> handleGet(final APIGatewayProxyRequestEvent event, final Context context) {

    logger.info("Requested GET with Path params : {} \n and Query Params: {}", event.getPathParameters(),
            event.getQueryStringParameters());


    final String key = event.getPathParameters().getOrDefault("device-key", "");
    final String from = event.getQueryStringParameters().getOrDefault("from", "");

    return isValidDate(from) ? positiveContactsDao.getPositiveContactByDeviceKey(key, stringToDate(from)) : Collections.emptyList();
  }
}
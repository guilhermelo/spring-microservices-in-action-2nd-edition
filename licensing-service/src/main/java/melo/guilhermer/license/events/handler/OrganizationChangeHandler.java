package melo.guilhermer.license.events.handler;

import melo.guilhermer.license.messaging.OrganizationChangeModel;
import melo.guilhermer.license.repository.OrganizationRedisRepository;
import melo.guilhermer.license.service.client.CustomChannels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//
//@EnableBinding(CustomChannels.class)
public class OrganizationChangeHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeHandler.class);

    @Autowired
    private OrganizationRedisRepository organizationRedisRepository;

//    @StreamListener("inboundOrgChanges")
//    public void loggerSink(OrganizationChangeModel organization) {
//        logger.debug("Received a message of type " + organization.getType());
//        logger.debug("Received a message with an event {} from the organization service for the organization id {} ", organization.getType(), organization.getType());
//    }
}

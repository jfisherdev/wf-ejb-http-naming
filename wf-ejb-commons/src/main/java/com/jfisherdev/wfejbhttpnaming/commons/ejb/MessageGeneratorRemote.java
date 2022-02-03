package com.jfisherdev.wfejbhttpnaming.commons.ejb;

/**
 * @author Josh Fisher
 */
public interface MessageGeneratorRemote {

    String BEAN_NAME = "MessageGenerator";

    String V2_BEAN_NAME = "example/" + BEAN_NAME;

    int generateRandomNumber(int min, int max);

    String generateRandomMessage(int length);

}

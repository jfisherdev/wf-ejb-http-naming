package com.jfisherdev.wfejbhttpnaming.ejbclient;

import com.jfisherdev.wfejbhttpnaming.commons.ejb.EjbConstants;
import com.jfisherdev.wfejbhttpnaming.commons.ejb.MessageGeneratorRemote;

import javax.naming.Context;
import javax.naming.NamingException;

/**
 * @author Josh Fisher
 */
public class Main {


    public static void main(String[] args) {
        final String providerUrl = System.getProperty(Context.PROVIDER_URL);
        try(EjbClient ejbClient = new EjbClient(providerUrl)) {
            System.out.println("Using " + Context.PROVIDER_URL + ": " + providerUrl);
            doLookupAndTest(ejbClient, MessageGeneratorRemote.BEAN_NAME);
            doLookupAndTest(ejbClient, MessageGeneratorRemote.V2_BEAN_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doLookupAndTest(EjbClient ejbClient, String beanName) throws NamingException {
        final String lookupName = ejbClient.getLookupName(EjbConstants.APP_NAME, EjbConstants.MODULE_NAME, beanName, MessageGeneratorRemote.class);
        System.out.println("Looking up bean '" + beanName + "' with lookup name of: " + lookupName);
        final MessageGeneratorRemote messageGeneratorRemote = ejbClient.lookup(EjbConstants.APP_NAME, EjbConstants.MODULE_NAME, beanName, MessageGeneratorRemote.class);
        final int messageLength = messageGeneratorRemote.generateRandomNumber(8, 32);
        System.out.println("Message will be " + messageLength + " characters long");
        final String generatedMessage = messageGeneratorRemote.generateRandomMessage(messageLength);
        System.out.println("Generated message: " + generatedMessage);
    }
}

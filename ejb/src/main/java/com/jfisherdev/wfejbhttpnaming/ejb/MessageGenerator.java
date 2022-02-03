package com.jfisherdev.wfejbhttpnaming.ejb;

import com.jfisherdev.wfejbhttpnaming.commons.ejb.MessageGeneratorRemote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.Random;

/**
 * Example EJB with NO "/" its bean name
 *
 * @author Josh Fisher
 */
@Stateless(name = MessageGeneratorRemote.BEAN_NAME)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Remote(MessageGeneratorRemote.class)
public class MessageGenerator implements MessageGeneratorRemote {

    private static final int MIN_CHAR = 32;
    private static final int MAX_CHAR = 126;

    private final Random rng = new Random();

    @Override
    public int generateRandomNumber(int min, int max) {
        return rng.nextInt(max - min) + min;
    }

    @Override
    public String generateRandomMessage(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final char nextChar = (char) (rng.nextInt(MAX_CHAR - MIN_CHAR) + MIN_CHAR);
            stringBuilder.append(nextChar);
        }
        return stringBuilder.toString();
    }

}

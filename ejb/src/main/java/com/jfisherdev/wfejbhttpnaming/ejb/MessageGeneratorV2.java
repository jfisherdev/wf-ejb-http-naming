package com.jfisherdev.wfejbhttpnaming.ejb;

import com.jfisherdev.wfejbhttpnaming.commons.ejb.MessageGeneratorRemote;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * Example EJB with a "/" in its bean name
 *
 * @author Josh Fisher
 */
@Stateless(name = MessageGeneratorRemote.V2_BEAN_NAME)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Remote(MessageGeneratorRemote.class)
public class MessageGeneratorV2 extends MessageGenerator {
}

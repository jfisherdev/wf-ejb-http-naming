# wf-ejb-http-naming
Example for demonstrating an issue with WildFly EJB client lookups over http when a bean name contains a "/"

# The Issue

This requires an EJB whose bean name contains a `/` character, such as `ejbs/MyBean`.

When looking up with a remote client using the remote+http protocol this works; however, if using pure http/s for ejb over http/s
invocation this will result in a `NoSuchEJBException`.

The main Gradle build will produce the `wf-ejb-http-naming.ear` application, which can also be deployed using the `deployStandalone`
task. This application contains two EJBs with identical behavior and the same remote interface, but one has a bean name with a `/` 
while the other does not. The `ejbclient` module can be assembled to run the [com.jfisherdev.wfejbhttpnaming.Main](ejbclient/src/main/java/com/jfisherdev/wfejbhttpnaming/ejbclient/Main.java) 
class, which will attempt to look up each EJB and invoke methods on it. The caller is expected to set the `java.naming.provider.url` 
for simplicity, although in practice many may prefer to use a wildfly-config.xml or other approach, which will 
dictate what protocol is used.

# The Results

Below are the program outputs for each protocol tested:

## remote+http

```
Using java.naming.provider.url: remote+http://localhost:8080
Looking up bean 'MessageGenerator' with lookup name of: wf-ejb-http-naming/ejb//MessageGenerator!com.jfisherdev.wfejbhttpnaming.commons.ejb.MessageGeneratorRemote
Message will be 16 characters long
Generated message: I#JXA2Rzm`?yOJ#<
Looking up bean 'example/MessageGenerator' with lookup name of: wf-ejb-http-naming/ejb//"example/MessageGenerator"!com.jfisherdev.wfejbhttpnaming.commons.ejb.MessageGeneratorRemote
Message will be 21 characters long
Generated message: %dkdL2>m#>,O>S2t`0yM)
```

**Notice that both bean names work**

## http/s

```
Using java.naming.provider.url: http://localhost:35000/wildfly-services
Looking up bean 'MessageGenerator' with lookup name of: wf-ejb-http-naming/ejb//MessageGenerator!com.jfisherdev.wfejbhttpnaming.commons.ejb.MessageGeneratorRemote
Message will be 15 characters long
Generated message: #%W1+Mq9k2e}C7]
Looking up bean 'example/MessageGenerator' with lookup name of: wf-ejb-http-naming/ejb//"example/MessageGenerator"!com.jfisherdev.wfejbhttpnaming.commons.ejb.MessageGeneratorRemote
org.jboss.ejb.client.RequestSendFailedException: EJBCLIENT000409: No more destinations are available
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:620)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:551)
	at org.jboss.ejb.protocol.remote.RemotingEJBClientInterceptor.handleInvocationResult(RemotingEJBClientInterceptor.java:57)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:622)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:551)
	at org.jboss.ejb.client.TransactionPostDiscoveryInterceptor.handleInvocationResult(TransactionPostDiscoveryInterceptor.java:148)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:622)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:551)
	at org.jboss.ejb.client.DiscoveryEJBClientInterceptor.handleInvocationResult(DiscoveryEJBClientInterceptor.java:130)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:622)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:551)
	at org.jboss.ejb.client.NamingEJBClientInterceptor.handleInvocationResult(NamingEJBClientInterceptor.java:87)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:622)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:551)
	at org.jboss.ejb.client.AuthenticationContextEJBClientInterceptor.call(AuthenticationContextEJBClientInterceptor.java:59)
	at org.jboss.ejb.client.AuthenticationContextEJBClientInterceptor.handleInvocationResult(AuthenticationContextEJBClientInterceptor.java:52)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:622)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:551)
	at org.jboss.ejb.client.TransactionInterceptor.handleInvocationResult(TransactionInterceptor.java:212)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:622)
	at org.jboss.ejb.client.EJBClientInvocationContext.getResult(EJBClientInvocationContext.java:551)
	at org.jboss.ejb.client.EJBClientInvocationContext.awaitResponse(EJBClientInvocationContext.java:1003)
	at org.jboss.ejb.client.EJBInvocationHandler.invoke(EJBInvocationHandler.java:182)
	at org.jboss.ejb.client.EJBInvocationHandler.invoke(EJBInvocationHandler.java:116)
	at com.sun.proxy.$Proxy2.generateRandomNumber(Unknown Source)
	at com.jfisherdev.wfejbhttpnaming.ejbclient.Main.doLookupAndTest(Main.java:30)
	at com.jfisherdev.wfejbhttpnaming.ejbclient.Main.main(Main.java:20)
	Suppressed: javax.ejb.NoSuchEJBException
		at org.wildfly.http-client.ejb@1.1.4.Final//org.wildfly.httpclient.ejb.HttpInvocationHandler$1.writeNoSuchEJB(HttpInvocationHandler.java:278)
		at org.jboss.as.ejb3@22.0.1.Final//org.jboss.as.ejb3.remote.AssociationImpl.receiveInvocationRequest(AssociationImpl.java:137)
		at org.wildfly.http-client.ejb@1.1.4.Final//org.wildfly.httpclient.ejb.HttpInvocationHandler.lambda$handleInternal$0(HttpInvocationHandler.java:135)
		at org.jboss.threads@2.4.0.Final//org.jboss.threads.ContextClassLoaderSavingRunnable.run(ContextClassLoaderSavingRunnable.java:35)
		at org.jboss.threads@2.4.0.Final//org.jboss.threads.EnhancedQueueExecutor.safeRun(EnhancedQueueExecutor.java:1990)
		at org.jboss.threads@2.4.0.Final//org.jboss.threads.EnhancedQueueExecutor$ThreadBody.doRunTask(EnhancedQueueExecutor.java:1486)
		at org.jboss.threads@2.4.0.Final//org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1377)
		at org.jboss.xnio@3.8.4.Final//org.xnio.XnioWorker$WorkerThreadFactory$1$1.run(XnioWorker.java:1280)
		at java.base/java.lang.Thread.run(Thread.java:834)
```
**Notice that only the bean name WITHOUT a `/` works**


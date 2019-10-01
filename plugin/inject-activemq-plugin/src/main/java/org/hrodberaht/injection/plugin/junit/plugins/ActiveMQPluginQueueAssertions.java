package org.hrodberaht.injection.plugin.junit.plugins;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Enumeration;

public class ActiveMQPluginQueueAssertions implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ActiveMQPluginQueueAssertions.class);

    protected final EmbeddedActiveMQBroker activeMQBroker;
    private final Connection connection;
    protected final String name;


    public ActiveMQPluginQueueAssertions(String name, EmbeddedActiveMQBroker activeMQBroker) {
        try {
            this.name = name;
            this.activeMQBroker = activeMQBroker;
            this.connection = activeMQBroker.createConnectionFactory().createConnection();
            this.connection.start();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public int countMessages() {

        return doWithSession(Integer.class, session -> {
            QueueBrowser queueBrowser = session.createBrowser(new ActiveMQQueue(name));
            Enumeration e = queueBrowser.getEnumeration();
            int numMsgs = 0;

            // count number of messages
            while (e.hasMoreElements()) {
                Message message = (Message) e.nextElement();
                numMsgs++;
            }
            return numMsgs;
        });
    }

    public <T> T getTextMessage(Class<T> clazz, JsonTextTransformer<T> jsonTextTransformer) {
        try (QueueReceiverForMessage queueReceiver = new QueueReceiverForMessage(name, connection)) {
            Message message = queueReceiver.receive(600);
            if (message instanceof TextMessage) {
                return jsonTextTransformer.transform(((TextMessage) message).getText());
            } else if (message == null) {
                throw new IllegalAccessError("message was null");
            }
            throw new IllegalAccessError("message was not text message");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public interface JsonTextTransformer<T> {
        T transform(String text);
    }

    public String putToQueue(String textMessageStr) {
        return doWithSession(String.class, session -> {
            TextMessage textMessage = session.createTextMessage(textMessageStr);
            LOG.info("Putting message {} to {} - content: {}", textMessage.getJMSMessageID(), name, textMessage.getText());
            MessageProducer messageProducer = session.createProducer(session.createQueue(name));
            try {
                messageProducer.send(textMessage);
            } finally {
                messageProducer.close();
            }
            return textMessage.getJMSMessageID();
        });
    }


    public void waitForMessageConsumption() {
        int i = 0;
        while (true) {
            if (countMessages() == 0) {
                return;
            } else {
                try {
                    Thread.sleep(100 + (i * 100));
                    i++;
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void cleanupMessages() {
        try (QueueReceiverForMessage queueReceiver = new QueueReceiverForMessage(name, connection)) {
            while (true) {
                Message message = queueReceiver.receive(60);
                if (message == null) {
                    return;
                }
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

    }

    public static class QueueReceiverForMessage implements AutoCloseable {
        private final Session session;
        private final MessageConsumer consumer;

        public QueueReceiverForMessage(String name, Connection connection) throws JMSException {

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(name);

            // Create a MessageConsumer from the Session to the Topic or Queue
            consumer = session.createConsumer(destination);
        }

        public Message receive(long timeout) throws JMSException {
            return consumer.receive(timeout);
        }

        @Override
        public void close() {
            closeConsumer();
            closeSession();
        }

        private void closeSession() {
            try {
                session.close();
            } catch (Exception e) {

            }
        }

        private void closeConsumer() {
            try {
                consumer.close();
            } catch (Exception e) {

            }
        }
    }

    public interface WithSession<T> {
        T doWith(Session session) throws JMSException;
    }

    protected <T> T doWithSession(Class<T> type, WithSession<T> withSession) {
        Session session = null;
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            return withSession.doWith(session);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                }
            }

        }
    }


    @Override
    public void close() {
        try {
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}

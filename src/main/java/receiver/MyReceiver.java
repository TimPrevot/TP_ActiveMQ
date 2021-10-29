package receiver;

import javax.jms.*;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyReceiver {

    public static void main(String[] args) {
        try {

            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContextJMS.xml");
            QueueConnectionFactory factory = (QueueConnectionFactory) applicationContext.getBean("connectionFactory");

            Queue queue = (Queue) applicationContext.getBean("queue");

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create a connection. See https://docs.oracle.com/javaee/7/api/javax/jms/package-summary.html
            Connection connection = connectionFactory.createConnection();
            // Open a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Create a destination
            Destination destination = session.createQueue("DQueue");
            // Create a receive
            MessageConsumer consumer = session.createConsumer(destination);
            // start the connection
            connection.start();
            // Receive the message
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if (message instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage) message;
                            String text = textMessage.getText();
                            System.out.println("Received: " + text);
                        } else {
                            System.out.println("Received: " + message);
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

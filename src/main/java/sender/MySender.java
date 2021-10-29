package sender;

import javax.jms.*;
import javax.jms.QueueConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Text;

public class MySender {

    public static void main(String[] args) {

        try {

            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContextJMS.xml");
            QueueConnectionFactory factory = (QueueConnectionFactory) applicationContext.getBean("connectionFactory");

            Queue queue = (Queue) applicationContext.getBean("queue");

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create a connection. See https://docs.oracle.com/javaee/7/api/javax/jms/package-summary.html
            Connection connection = connectionFactory.createConnection();
            // Open a session without transaction and acknowledge automatic
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Create a destination
            Destination destination = session.createQueue("DQueue");
            // Create a sender
            MessageProducer producer = session.createProducer(destination);
            // Set timer before losing message
            //producer.setTimeToLive(10000);
            producer.setPriority(6);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // Start the connection
            connection.start();
            // Create a message
            String text = "This is from sender1 !";
            TextMessage message = session.createTextMessage(text);
            // Send the message
            producer.send(message);
            System.out.println("Sent: " + message);
            // Close the session
            session.close();
            // Close the connection
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

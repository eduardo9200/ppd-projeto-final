package midware;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumidor {

    /*
     * URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor est� em localhost
     * 
     */
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String queueName;
    
    public Consumidor(String queueName1) {
        queueName = queueName1;
    }

    public Message execute() throws JMSException
    {
        /*
         * Estabelecendo conex�o com o Servidor JMS
         */
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        /*
         * Criando Session 
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        /*
         * Criando Queue
         */
        Destination destination = session.createQueue(queueName);

        /*
         * Criando Consumidor		 
         */
        MessageConsumer consumer = session.createConsumer(destination);


        /*
         * Recebendo Mensagem
         */
        Message message = consumer.receive();


        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println("Recebido: " + text);
        } else {
            System.out.println("Recebido: " + message);
        }

        consumer.close();
        session.close();
        connection.close();
        
        return message;
    }
}

package midware;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {

	/*
     * URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor está em localhost
     */	
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String topicName = "TOPICO_ESPIAO";
    
    private String messageText;

    public Publisher(String messageText) {
        this.messageText = messageText;
    }

    public void execute() {
    	try {
	        /*
	         * Estabelecendo conexão com o Servidor JMS
	         */		
	        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
	        Connection connection;
			connection = connectionFactory.createConnection();
	        connection.start();
	
	        /*
	         * Criando Session 
	         */		
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
	        /*
	         * Criando Topic
	         */     
	        Destination dest = session.createTopic(topicName);
	
	        /*
	         * Criando Produtor
	         */
	        MessageProducer publisher = session.createProducer(dest);
	
	        TextMessage message = session.createTextMessage();
	        message.setText(messageText);
	
	
	        /*
	         * Publicando Mensagem
	         */
	        publisher.send(message);
	
	        publisher.close();
	        session.close();
	        connection.close();

    	} catch (JMSException e) {
    		e.printStackTrace();
    	}
    }
}

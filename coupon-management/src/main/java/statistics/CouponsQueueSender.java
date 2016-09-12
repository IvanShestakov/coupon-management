package statistics;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

public class CouponsQueueSender {
	private QueueConnectionFactory qconFactory;
	  private QueueConnection qcon;
	  private QueueSession qsession;
	  private QueueSender qsender;
	  private Queue queue;

		// Connecting to the queue
		public CouponsQueueSender(){
			try{
				// Get a reference to the JNDI
				InitialContext ctx = new InitialContext(); 
				qconFactory = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");		//create Factory
				qcon = qconFactory.createQueueConnection();							//create Connection
				qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);//create Session
				queue = (Queue) ctx.lookup("queue/statistics");								//create Queue
				qsender = qsession.createSender(queue);								//create Sender
				qcon.start();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		public void send(String message){
			
		}
}

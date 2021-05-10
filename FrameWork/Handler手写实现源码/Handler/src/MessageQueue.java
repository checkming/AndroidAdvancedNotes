import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageQueue {

	//阻塞队列，模拟MessageQueue 的阻塞机制
	BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
	//往队列添加消息
	//根据时间排序，当队列满的时候，阻塞，直 
 //   到用户通过next取出消息。
    //当next方法被调用，通知MessagQueue可以
    //进行消息的入队。
	public void enqueueMessage(Message msg) {
		try {
			queue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//从消息队列取消息
	//当消息达到执行时间就取出来。
   // 当message queue为空的时候，队列阻塞，
   // 等消息队列调用enqueuer Message的时候，
   // 通知队列，可以取出消息，停止阻塞。

	public Message next() {
		Message msg = null;
		try {
			msg = queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}
}

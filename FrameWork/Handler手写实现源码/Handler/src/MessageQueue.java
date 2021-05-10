import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageQueue {

	//�������У�ģ��MessageQueue ����������
	BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
	//�����������Ϣ
	//����ʱ�����򣬵���������ʱ��������ֱ 
 //   ���û�ͨ��nextȡ����Ϣ��
    //��next���������ã�֪ͨMessagQueue����
    //������Ϣ����ӡ�
	public void enqueueMessage(Message msg) {
		try {
			queue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//����Ϣ����ȡ��Ϣ
	//����Ϣ�ﵽִ��ʱ���ȡ������
   // ��message queueΪ�յ�ʱ�򣬶���������
   // ����Ϣ���е���enqueuer Message��ʱ��
   // ֪ͨ���У�����ȡ����Ϣ��ֹͣ������

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

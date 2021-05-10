
public class Handler {
	
	final MessageQueue mQueue;
    final Looper mLooper;
    
    public Handler() {
    	mLooper = Looper.myLooper();
    	mQueue = mLooper.mQueue;
    }
	
	public void sendMessage(Message msg) {
		enqueueMessage(msg);
	}
	
	
	// 网消息队列添加消息
	private void enqueueMessage(Message msg) {
		msg.target = this;
		mQueue.enqueueMessage(msg);
	}
	
	
	public void dispatchMessage(Message msg) {
		handleMessage(msg);
	}

	//最少知识原则
	public void handleMessage(Message msg) {
	
	}
}

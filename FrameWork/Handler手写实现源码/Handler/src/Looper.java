
public class Looper {

	MessageQueue mQueue;
	static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
	
	private Looper() {
		mQueue = new MessageQueue();
	}
	
	//looper 初始化// 为什么？
	public static void prepare() {
		 if (sThreadLocal.get() != null) {
	            throw new RuntimeException("Only one Looper may be created per thread");
	        }
	        sThreadLocal.set(new Looper());
	}
	
	//
	public static Looper myLooper() {
		return sThreadLocal.get();
	}
	
	public static void loop() {
		final Looper me = myLooper();
		final MessageQueue queue = me.mQueue;
		for(;;) {
			Message msg = queue.next();
			if(msg != null) {
				msg.target.dispatchMessage(msg);
			}
			
		}
	}
}

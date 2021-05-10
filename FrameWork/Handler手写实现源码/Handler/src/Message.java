
public class Message {

	String obj;
	Handler target;
	
	public Message(String object){
		obj = object;
	}
	
	public String toString(){
		return obj.toString();
	}
}

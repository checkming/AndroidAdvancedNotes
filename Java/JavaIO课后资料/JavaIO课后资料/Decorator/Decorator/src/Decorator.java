

public class Decorator extends Person {

	Person person;
	public Decorator(Person person) {
		this.person = person;
	}
	@Override
	public void show() {
		person.show();
	}



}

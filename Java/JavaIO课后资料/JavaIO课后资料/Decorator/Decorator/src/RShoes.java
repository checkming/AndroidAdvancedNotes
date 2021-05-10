

public class RShoes extends Decorator{

	public RShoes(Person person) {
		super(person);
	}

	@Override
	public void show() {
		super.show();
		System.out.print("  ´© Rshoes");
	}

}

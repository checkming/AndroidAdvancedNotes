package decorator;

public class HamDecorator extends PancakeDecorator{

	public HamDecorator(IPancake pancake) {
		super(pancake);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void cook() {
		System.out.println("加一根火腿的");
		super.cook();
	}
}

package decorator;

public class EggDecorator extends PancakeDecorator{

	public EggDecorator(IPancake pancake) {
		super(pancake);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cook() {
		System.out.println("加一个鸡蛋的");
		super.cook();
	}
}

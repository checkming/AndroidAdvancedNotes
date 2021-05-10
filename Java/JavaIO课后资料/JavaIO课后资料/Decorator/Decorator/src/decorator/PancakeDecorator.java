package decorator;

public abstract class PancakeDecorator implements IPancake {
	
	private IPancake pancake;
	
	public PancakeDecorator(IPancake pancake) {
		this.pancake = pancake;
	}

	public void cook(){
		if (this.pancake != null) {
			pancake.cook();
		}
	}
}

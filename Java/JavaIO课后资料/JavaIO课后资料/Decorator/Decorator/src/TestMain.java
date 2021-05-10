

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SinglePerson pFinery = new SinglePerson("AV");
//		pFinery.show();
		RShoes rShoes = new RShoes(pFinery);
		TShirts shirts = new TShirts(rShoes);
		shirts.show();
		

//		
	}

}

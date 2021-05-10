package classTop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.sound.midi.Soundbank;

public class InputStreamReaderTest {

	public static void testISRDefaultEncoder(InputStream is){
		try{
			
		
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String string;
			while ((string = br.readLine()) != null) {
				System.out.println("code: " + isr.getEncoding());
				System.out.println(string);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void testISRGBK(InputStream is){
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(is,"GBK");
			BufferedReader gbkBr = new BufferedReader(inputStreamReader);
			String string;
			while ((string = gbkBr.readLine()) != null) {
				System.out.println("code: " + inputStreamReader.getEncoding());
				System.out.println(string);
			}
			gbkBr.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	
	public static void testISRUTF8(InputStream is){
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(is,"UTF-8");
			BufferedReader gbkBr = new BufferedReader(inputStreamReader);
			String string;
			while ((string = gbkBr.readLine()) != null) {
				System.out.println("code: " + inputStreamReader.getEncoding());
				System.out.println(string);
			}
			gbkBr.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	public static void main(String[] args) throws IOException {
		//？？？？？？？？？？？？？？
		//创建过程是否过于麻烦？？？？？
		testISRDefaultEncoder(
				new FileInputStream(
						new File("src/testtxt/OutputStreamWriter.txt")));
		testISRGBK(
				new FileInputStream(
						new File("src/testtxt/OutputStreamWriter.txt")));
		testISRUTF8(
				new FileInputStream(
						new File("src/testtxt/OutputStreamWriter.txt")));
	}

}

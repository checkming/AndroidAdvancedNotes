package classTop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class OutputStreamWriterTest {

	private final static String STRING = "I like AV";
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		testOutputStreamWriter();
		
	}
	
	public static void testOutputStreamWriter() throws IOException {
		File file = new File("src/testtxt/OutputStreamWriter.txt");
		
		// true, 设置内容可以追加
		FileOutputStream fos = new FileOutputStream(file, true);
		//todo 是否有一个封装好的writer？
		OutputStreamWriter oswDef = new OutputStreamWriter(fos);
		BufferedWriter bwdef = new BufferedWriter(oswDef);
		
		bwdef.write(STRING);
		bwdef.newLine();
		bwdef.flush();
//		bwdef.close();  //为什么不能在这写
		System.out.println("oswDef encoding: " + oswDef.getEncoding());
		
		OutputStreamWriter oswGBK = new OutputStreamWriter(fos, "GBK");
		BufferedWriter bwGBK = new BufferedWriter(oswGBK);
		bwGBK.write(STRING + "GBK");
		bwGBK.newLine();
		bwGBK.flush();
//		bwGBK.close();
		System.out.println("oswDef encoding: " + oswGBK.getEncoding());
		
		OutputStreamWriter oswUTF8 = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter bwUTF8 = new BufferedWriter(oswUTF8);
		bwUTF8.write(STRING + "UTF-8");
		bwUTF8.newLine();
		bwUTF8.flush();
//		bwUTF8.close();
		System.out.println("oswDef encoding: " + oswUTF8.getEncoding());
		
		bwdef.close();
		bwGBK.close();
		bwUTF8.close();
		
	}

}

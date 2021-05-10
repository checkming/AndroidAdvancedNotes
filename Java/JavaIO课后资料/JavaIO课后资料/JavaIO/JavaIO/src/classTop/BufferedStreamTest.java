package classTop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;

public class BufferedStreamTest {
	private static final byte[] byteArray = {
	        0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F,
	        0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A
	    };
		

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		bufferedOutPutStream();
		bufferedInputStream();
	}
	
	private static void bufferedOutPutStream() {
		try {
			File file = new File("src/testtxt/BufferedStreamTest.txt");
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(byteArray[0]);
			bos.write(byteArray, 1, byteArray.length - 1);
			bos.flush();
			bos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void bufferedInputStream() {
		try {
			File file = new File("src/testtxt/BufferedStreamTest.txt");
			BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
			for(int i = 0; i < 10; i++) {
				if (bin.available() >= 0) {
					System.out.println(byteToString((byte)bin.read()));
				}
			}
			
			bin.mark(6666);
			bin.skip(10);
			
			byte[] b = new byte[1024];
			int n1 = bin.read(b, 0, b.length);
			System.out.println("剩余的有效字节数 ： " + n1);
			printByteValue(b);
			
			bin.reset();
			int n2 = bin.read(b,0, b.length);
			System.out.println("剩余的有效字节数 ： " + n2);
			printByteValue(b);
			
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static String byteToString(byte b) {
		byte[] barray = {b};
		return new String(barray);
	}
	
	private static void printByteValue(byte[] buf) {
		for(byte b: buf) {
			if (b != 0) {
				System.out.print(byteToString(b) + " ");
			}
		}
	}

}

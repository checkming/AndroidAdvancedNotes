package classTop;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTests {

private static final File file = new File("src\\testtxt\\raf.txt");
	
	/**
	 * 向文件中写入内容
	 */
	public static void testRandomAccessFileWriter() throws IOException{
		//要先将已有文件删除、避免干扰。
		if(file.exists()){
			file.delete();
		}
		
		RandomAccessFile rsfWriter = new RandomAccessFile(file, "rw");
		
		//不会改变文件大小、但是他会将下一个字符的写入位置标识为10000、
		//也就是说此后只要写入内容、就是从10001开始存、
		rsfWriter.seek(10000);
		printFileLength(rsfWriter);		//result: 0
		
		//会改变文件大小、只是把文件的size改变、
		//并没有改变下一个要写入的内容的位置、
		//这里注释掉是为了验证上面的seek方法的说明内容
		rsfWriter.setLength(10000);
		System.out.println("oo");
		printFileLength(rsfWriter);		//result: 0
		System.out.println("xx");
		//每个汉子占3个字节、写入字符串的时候会有一个记录写入字符串长度的两个字节
		rsfWriter.writeUTF("享学课堂");	
		printFileLength(rsfWriter);		//result: 10014 
		
		//每个字符占两个字节
		rsfWriter.writeChar('a');
		rsfWriter.writeChars("abcde");
		printFileLength(rsfWriter);		//result: 10026
		
		//再从“文件指针”为5000的地方插一个长度为100、内容全是'a'的字符数组
		//这里file长依然是10026、因为他是从“文件指针”为5000的地方覆盖后面
		//的200个字节、下标并没有超过文件长度
		rsfWriter.seek(5000);
		char[] cbuf = new char[100];
		for(int i=0; i<cbuf.length; i++){
			cbuf[i] = 'a';
			rsfWriter.writeChar(cbuf[i]);
		}
		
		
		printFileLength(rsfWriter);	//result:  10026
		
		//再从“文件指针”为1000的地方插入一个长度为100、内容全是a的字节数组
		//这里file长依然是10026、因为他是从“文件指针”为5000的地方覆盖后面
		//的200个字节、下标并没有超过文件长度
		byte[] bbuf = new byte[100];
		for (int i = 0; i < bbuf.length; i++) {
			bbuf[i] = 1;
		}
		rsfWriter.seek(1000);
		rsfWriter.writeBytes(new String(bbuf));
		printFileLength(rsfWriter);
	}
	
	/**
	 * 从文件中读取内容
	 * 这里我们要清楚现在文件中有什么内容、而且还要清楚这些内容起始字节下标、长度
	 * 
	 * @throws IOException
	 */
	public static void testRandomAccessFileRead() throws IOException{
		/*
		 * 对文件中内容简单说明：
		 * 1、从0到1000	为空
		 * 2、从1001到1100是100个1
		 * 3、从1101到5000是空
		 * 4、从5001到5200是字符'a'
		 * 5、从5201到10000是空
		 * 6、从10001到10011是字符串"陈华应"
		 * 7、从10012到10023是"aabcde"
		 */
		RandomAccessFile rsfReader = new RandomAccessFile(file, "r");
		//可按照自己想读取的东西所在的位置、长度来读取
		
		//读取"享学课堂"
		rsfReader.seek(10000);
		System.out.println(rsfReader.readUTF());
		
		//读取100个字符'a'
		rsfReader.seek(5000);
		byte[] bbuf = new byte[200];
		rsfReader.read(bbuf);
		System.out.println(new String(bbuf));
		
		//读取100个1
		byte[] bbuf2 = new byte[100];
		rsfReader.seek(1000);
		rsfReader.read(bbuf2, 0, 100);
		for(byte b : bbuf2){
			System.out.print(b);
		}
		
		//读取字符'aabcde'
		byte[] bbuf3 = new byte[12];
		rsfReader.seek(10014);
		rsfReader.read(bbuf3);
		System.out.println(new String(bbuf3));
	}
	/**
	 * 打印文件长度
	 * @param rsfWriter 指向文件的随机文件流
	 * @throws IOException
	 */
	private static void printFileLength(RandomAccessFile rsfWriter)
			throws IOException {
		System.out.println("file length: " + rsfWriter.length() + "  file pointer: " + rsfWriter.getFilePointer());
	}
	
	public static void main(String[] args) throws IOException {
		testRandomAccessFileWriter();
		testRandomAccessFileRead();
	}


}

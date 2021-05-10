

import java.io.File;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

/**
 * Created by xiang on 2017/5/17.
 */

public class Utils {

//  public static byte[] int2Bytes(int number) {
//      byte[] b = new byte[4];
//      for (int i = 3; i >= 0; i--) {
//          b[i] = (byte) (number % 256);
//          number >>= 8;
//      }
//      return b;
//  }

  public static byte[] int2Bytes(int value) {
      byte[] src = new byte[4];
      src[3] = (byte) ((value >> 24) & 0xFF);
      src[2] = (byte) ((value >> 16) & 0xFF);
      src[1] = (byte) ((value >> 8) & 0xFF);
      src[0] = (byte) (value & 0xFF);
      return src;
  }

  public static int bytes2Int(byte[] src) {
      int value;
      value = (int) ((src[0] & 0xFF)
              | ((src[1] & 0xFF)<<8)
              | ((src[2] & 0xFF)<<16)
              | ((src[3] & 0xFF)<<24));
      return value;
  }

  public static void changeSignature(byte[] newDex) throws NoSuchAlgorithmException {
      System.out.println("更换dex文件 签名信息...");
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      //从32个字节开始 计算sha1值
      md.update(newDex, 32, newDex.length - 32);
      byte[] sha1 = md.digest();
      //从第12位开始拷贝20字节内容
      //替换signature
      System.arraycopy(sha1, 0, newDex, 12, 20);
      System.out.println("更换dex文件 checksum...");
  }

  public static void changeCheckSum(byte[] newDex) {
      Adler32 adler = new Adler32();
      adler.update(newDex, 12, newDex.length - 12);
      int value = (int) adler.getValue();
      byte[] checkSum = Utils.int2Bytes(value);
      System.arraycopy(checkSum, 0, newDex, 8, 4);
  }

  public static byte[] getBytes(File dexFile) throws Exception {
      RandomAccessFile fis = new RandomAccessFile(dexFile, "r");
      byte[] buffer = new byte[(int)fis.length()];
      fis.readFully(buffer);
      fis.close();
      return buffer;
  }

  public static void changeFileSize(byte[] mainDexData, byte[] newDex, byte[] aarData ) {
      byte[] bytes = Utils.int2Bytes(mainDexData.length);
      System.out.println("拷贝原来dex长度到新的dex:" + Utils.bytes2Int(bytes));

      //更该文件头长度信息
      //修改
      System.out.println("更换dex 文件头长度信息...");
      byte[] file_size = Utils.int2Bytes(newDex.length);
      System.arraycopy(file_size, 0, newDex, 32, 4);
  }
//  public static void main(String[] args) throws Exception {
//      ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
//      byteBuf.writeInt(241241143);
//      byte[] a = new byte[4];
//      byteBuf.markReaderIndex();
//      byteBuf.readBytes(a);
//      byteBuf.resetReaderIndex();
//
//      System.out.println(Arrays.toString(a));
//      System.out.println(Arrays.toString(int2Bytes(241241143)));
//      System.out.println(Arrays.toString(intToBytes(241241143)));
//  }

}

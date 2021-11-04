package com.enjoy.memory;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        /**
         * 软引用
         */
        Object softObject =new Object();
        SoftReference<Object> objectSoftReference = new SoftReference<>(softObject);
        System.out.println("soft:"+objectSoftReference.get());
        softObject = null;


        System.gc();
        System.out.println("soft:"+ objectSoftReference.get());

        System.out.println("======================================");
        /**
         * 弱引用
         */
        Object weakObject =new Object();
        WeakReference<Object> objectWeakReference = new WeakReference<>(weakObject);
        System.out.println("weak:"+objectWeakReference.get());
        weakObject = null;


        System.gc();
        System.out.println("weak:"+objectWeakReference.get());


    }



    void a() {
        FileOutputStream fos = null;
        try {
             fos = new FileOutputStream(new File(""));
            fos.write(1);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
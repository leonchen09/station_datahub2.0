package com.walletech.datahub;

import java.io.*;
import java.net.Socket;

/**
 * Unit test for simple App.
 */
public class AppTest
{
int i=1;
    public static void main(String[] args) {

//        assertTrue( true );
        Socket socket = null;
        OutputStream out=null;
        try {
            AppTest appTest = new AppTest();
            socket = new Socket("localhost",56123);
            InputStream in = socket.getInputStream();
            out = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(out);
           // out.write(appTest.send2());
            out.write(appTest.sendWarning());
            out.flush();
            System.out.println("已发送");
//            byte[] re = new byte[1024];
//            while(in.read(re)!=0){
//                System.out.print(re);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public byte[] send1() {
        byte[] sendBytes=new byte[] {(byte)0x7F,(byte)0xF7 ,(byte)0x08 ,(byte)0xF0
                ,(byte)0x53 ,(byte)0x53 ,(byte)0x53 ,(byte)0x00 ,(byte)0x00 ,(byte)0x1A ,(byte)0x00};
        sendId(sendBytes);
        sendBytes[sendBytes.length-1]=bbc(sendBytes);
//		System.out.println("传输数据： ");
//		for (byte b : sendBytes) {
//			System.out.print(b+" ");
//		}
//		System.out.println();
        return sendBytes;
    }

    private byte[] send2() {
        byte[] sendBytes=new byte[] {(byte)0x7F,(byte)0xF7,(byte)0x66,(byte)0x10,(byte)0x53,(byte)0x53,//5
                (byte)0x52,(byte)0x00,(byte)0x00,(byte)0x17,(byte)0x0E,(byte)0x01,(byte)0x01,(byte)0x5C,//13
                (byte)0x50,(byte)0x2F,(byte)0xEA,(byte)0x2F,(byte)0xCF,(byte)0x2F,(byte)0xF1,(byte)0x2F,//21
                (byte)0xEB,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,//29
                (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,//37
                (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,//45
                (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,//53
                (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,//61
                (byte)0x00,(byte)0x45,(byte)0x45,(byte)0x45,(byte)0x45,(byte)0x32,(byte)0x32,(byte)0x32,//69
                (byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,//77
                (byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,(byte)0x32,//85
                (byte)0x32,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xFF,//93
                (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x01,//99
                (byte)0x04,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};//107
        sendId(sendBytes);
        sendBytes[sendBytes.length-1]=bbc(sendBytes);
//		System.out.println("传输数据： ");
//		for (byte b : sendBytes) {
//			System.out.print(b+" ");
//		}
//		System.out.println();
        return sendBytes;
    }

    private byte[] sendWarning() {
        byte[] sendBytes=new byte[] {(byte)0x7F,(byte)0xF7,(byte)0x0a,(byte)0xf5,(byte)0x53,(byte)0x53,//5
                (byte)0x52,(byte)0x00,(byte)0x00,(byte)0x17,(byte)0x01,(byte)0x05,(byte)0x00};//12

        sendId(sendBytes);
        sendBytes[sendBytes.length-1]=bbc(sendBytes);
//		System.out.println("传输数据： ");
//		for (byte b : sendBytes) {
//			System.out.print(b+" ");
//		}
//		System.out.println();
        return sendBytes;
    }
    public byte bbc(byte[] x) {
        byte ans=0x00;
        for (int i = 0; i < x.length; i++) {
            ans^=x[i];
        }
        return ans;
    }

    public void sendId(byte[] x) {
        byte m=(byte) (i>>16);
        byte m2=(byte) (i>>8);
        byte m3=(byte) i;
        x[7]=m;
        x[8]=m2;
        x[9]=m3;
    }

}

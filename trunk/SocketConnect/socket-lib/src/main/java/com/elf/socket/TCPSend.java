package com.elf.socket;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * tcp 发送消息
 * Created by Lidong on 2017/10/8.
 */
public class TCPSend {

    public static final String TAG = "TCPSend";

    /**
     * 发送数据的客户端Socket
     */
    private Socket socket;

    private OutputStream out = null;

    /**
     * @param ip   接收方的ip地址
     * @param port 接收方的端口号
     */
    public TCPSend(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     */
    public void sendMessage(String msg) {
        try {
            out.write(msg.getBytes());
            out.flush();
            Log.i(TAG, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket.isInputShutdown()) { //判断输入流是否为打开状态
            try {
                socket.shutdownInput();  //关闭输入流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket.isOutputShutdown()) {  //判断输出流是否为打开状态
            try {
                socket.shutdownOutput(); //关闭输出流（如果是在给对方发送数据，发送完毕之后需要关闭输出，否则对方的InputStream可能会一直在等待状态）
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket.isConnected()) {  //判断是否为连接状态
            try {
                socket.close();  //关闭socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

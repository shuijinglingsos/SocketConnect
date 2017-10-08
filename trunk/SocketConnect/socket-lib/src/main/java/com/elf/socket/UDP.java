package com.elf.socket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * UDP
 * Created by Lidong on 2017/10/8.
 */
public class UDP {

    private Context mContext;
    private int mPort;

    public UDP(Context context, int port) {
        mContext = context;
        mPort = port;
    }

    public void sendBroadCast(String data) {
        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();  //这里获取了IP地址，获取到的IP地址还是int类型的

        /*这里就是将int类型的IP地址通过工具转化成String类型的，便于阅读
        String ips = Formatter.formatIpAddress(ip);
        */

        int broadCastIP = ip | 0xFF000000;   //这一步就是将本机的IP地址转换成xxx.xxx.xxx.255

        DatagramSocket theSocket = null;
        try {
            InetAddress server = InetAddress.getByName(Formatter.formatIpAddress(broadCastIP));
            theSocket = new DatagramSocket();
            DatagramPacket theOutput = new DatagramPacket(data.getBytes(), data.length(), server, mPort);
                /*这一句就是发送广播了，其实255就代表所有的该网段的IP地址，是由路由器完成的工作*/
            theSocket.send(theOutput);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (theSocket != null)
                theSocket.close();
        }
    }

    public void sendBroadCast1(String data) {
        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();  //这里获取了IP地址，获取到的IP地址还是int类型的

        /*这里就是将int类型的IP地址通过工具转化成String类型的，便于阅读
        String ips = Formatter.formatIpAddress(ip);
        */

        int broadCastIP = ip | 0xFF000000;   //这一步就是将本机的IP地址转换成xxx.xxx.xxx.255

        for (int i = 0; i < 256; i++) {

            DatagramSocket theSocket = null;
            try {
                //通过这个循环就将所有的本段中的所有IP地址都发送一遍了
                InetAddress server = InetAddress.getByName(Formatter.formatIpAddress(broadCastIP | ((0xFF - i) << 24)));
                theSocket = new DatagramSocket();
                DatagramPacket theOutput = new DatagramPacket(data.getBytes(), data.length(), server, mPort);

                theSocket.send(theOutput);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (theSocket != null)
                    theSocket.close();
            }
        }
    }

    public UDPData receive() {


        byte[] buffer = new byte[1024];
        DatagramSocket server = null;
        try {
            server = new DatagramSocket(mPort);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (true) {
                try {
                    server.receive(packet);
                    String s = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                    System.out.println("address : " + packet.getAddress() + ", port : " + packet.getPort() + ", data : " + s);

                    UDPData udpData = new UDPData();
                    udpData.address = packet.getAddress().toString();
                    udpData.port = packet.getPort();
                    udpData.data = s;
                    return udpData;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            if (server != null)
                server.close();
        }

        return null;
    }


    public static class UDPData {
        public String address;
        public int port;
        public String data;

        @Override
        public String toString() {
            return "address:" + address + ", port" + port + ", data:" + data;
        }
    }
}

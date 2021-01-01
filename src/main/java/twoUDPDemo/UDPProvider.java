package twoUDPDemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPProvider {
    public static void main(String[] args) throws IOException {
        System.out.println("twoUDPDemo.UDPProvider start...");

        //构建一个UDPSocket类,并且指定监听20000端口
        DatagramSocket datagramSocket = new DatagramSocket(20000);

        //定义一个512大小 的字节数组用于接收发送的数据
        final byte[] bytes = new byte[512];

        //创建一个DatagramPacket类,用于接收发送的数据
        DatagramPacket receiveDatagramPacket = new DatagramPacket(bytes,bytes.length);
        //接收发送的数据
        datagramSocket.receive(receiveDatagramPacket);

        //获取发送数据的IP地址
        String ip = receiveDatagramPacket.getAddress().getHostAddress();
        //获取发送数据的端口
        int port = receiveDatagramPacket.getPort();
        //获取发送数据的长度
        int dataLen = receiveDatagramPacket.getLength();
        //获取发送的数据
        String receiveData = new String(receiveDatagramPacket.getData(), 0, dataLen);

        //输出接收到的数据
        System.out.println("IP:" + ip + "/t PORT:" + port + "/t Length:" + dataLen + "/t DaATA:" + receiveData);

        System.out.println("twoUDPDemo.UDPProvider receive message!");

        //构建回送数据回送回去
        String str = "收到的消息和长度：" + dataLen;
        byte[] strBytes = str.getBytes();
        DatagramPacket sendDatagramPacket = new DatagramPacket(strBytes, strBytes.length);
        //设置发送的目的地址
        sendDatagramPacket.setAddress(receiveDatagramPacket.getAddress());
        //设置发送的目的端口
        sendDatagramPacket.setPort(port);

        //发送消息
        datagramSocket.send(sendDatagramPacket);

        System.out.println("twoUDPDemo.UDPProvider send message!");

        //释放资源
        datagramSocket.close();
    }
}

package twoUDPDemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP学习案列
 */
public class UDPSearcher {
    public static void main(String[] args) throws IOException {
        System.out.println("twoUDPDemo.UDPSearcher start...");

        //构建一个UDPSocket类用于发送数据,端口由系统分配
        DatagramSocket datagramSocket = new DatagramSocket();

        //需要发送数据的字符数组
        byte[] bytes = "hello word!".getBytes();
        //构建一个DatagramPacket，用于发送数据
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
        //设置发送数据的目的地址
        datagramPacket.setAddress(InetAddress.getLocalHost());
        //设置发送数据的目的端口
        datagramPacket.setPort(20000);

        //发送数据
        datagramSocket.send(datagramPacket);

        System.out.println("twoUDPDemo.UDPSearcher send message!");

        //定义一个512大小 的字节数组用于接收发送的数据
        final byte[] receiveBytes = new byte[512];

        //创建一个DatagramPacket类,用于接收发送的数据
        DatagramPacket receiveDatagramPacket = new DatagramPacket(receiveBytes,receiveBytes.length);
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

        System.out.println("twoUDPDemo.UDPSearcher receive mesage!");

        //释放资源
        datagramSocket.close();
    }
}

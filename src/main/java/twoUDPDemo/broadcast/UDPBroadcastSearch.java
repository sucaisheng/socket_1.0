package twoUDPDemo.broadcast;


import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UDPBroadcastSearch {
    private static final int LISTEN_PORT = 30000;
    public static void main(String[] args) throws IOException {
        System.out.println("twoUDPDemo.broadcast.UDPBroadcastSearch start....");
        Listener listener = listener();
        sendBroadcast();

        //按任意键结束
        System.in.read();
        List<Device> listenerDevicesAndExit = listener.getDevicesAndExit();
        for (Device device : listenerDevicesAndExit){
            System.out.println("Device:" + device.toString());
        }

        System.out.println("twoUDPDemo.broadcast.UDPBroadcastSearch finish!");
    }

    /**
     * 监听方法
     * @return
     */
    private static Listener listener(){
        System.out.println("twoUDPDemo.broadcast.UDPBroadcastSearch start listen....");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, countDownLatch);
        listener.start();
        return listener;
    }

    /**
     * 广播方法
     * @throws IOException
     */
    private static void sendBroadcast() throws IOException {
        //获取DatagramSocket用于广播发送数据
        DatagramSocket datagramSocket = new DatagramSocket();
        //广播的数据
        String data = MessageCreate.buildWithPort(LISTEN_PORT);
        byte[] dataBytes = data.getBytes();
        //创建DatagramPacket用于发送数据
        DatagramPacket datagramPacket = new DatagramPacket(dataBytes, dataBytes.length);
        //发送一个受限的广播
        datagramPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        datagramPacket.setPort(20000);
        datagramSocket.send(datagramPacket);
        datagramSocket.close();
    }

    private static class Device{
        private String ip;
        private int port;
        private String sn;

        public Device(String ip, int port, String sn) {
            this.ip = ip;
            this.port = port;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "ip='" + ip + '\'' +
                    ", port=" + port +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    /**
     * 实现线程类
     */
    private static class Listener extends Thread{
        private final int LISTEN_PORT;
        //用于记录UDPBroadcastSearch运行了
        private final CountDownLatch countDownLatch;
        //用于保存设备的信息
        private List<Device> devices = new ArrayList<>();
        private Boolean flag = true;
        private DatagramSocket datagramSocket = null;

        public Listener(int port, CountDownLatch countDownLatch){
            super();
            this.LISTEN_PORT = port;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            super.run();
            //通知已经启动
            countDownLatch.countDown();

            while (flag){

                System.out.println("twoUDPDemo.broadcast.UDPBroadcastSearch start....");

                try {
                    //创建一个DatagramSocket用于监听LISTEN_PORT端口的数据
                    datagramSocket = new DatagramSocket(LISTEN_PORT);
                    byte[] bytes = new byte[512];
                    //创建DatagramPacket用于接收数据
                    DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
                    datagramSocket.receive(datagramPacket);
                    //获取数据长度
                    int length = datagramPacket.getLength();
                    //获取IP地址
                    String ip = datagramPacket.getAddress().getHostAddress();
                    //获取port
                    int port = datagramPacket.getPort();
                    //获取数据
                    String data = new String(datagramPacket.getData(),0, length);
                    System.out.println("收到数据：" + "IP:" + ip + "/t PORT:" + port + "/t Length:" + length + "DATA:" + data);
                    if (data != null){
                        String parseSN = MessageCreate.parseSN(data);
                        Device device = new Device(ip, port, parseSN);
                        devices.add(device);
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    close();
                }

                System.out.println("twoUDPDemo.broadcast.UDPBroadcastSearch listener finish!");
            }
        }

        private void close(){
            if (datagramSocket != null){
                datagramSocket.close();
                datagramSocket = null;
            }
        }

        public List<Device> getDevicesAndExit(){
            flag = false;
            close();
            return devices;
        }
    }
}

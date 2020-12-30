import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;

/**
 * UDP广播
 */
public class UDPBroadcastProvider {
    public static void main(String[] args) {
        String  sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn);
        provider.start();

        //从键盘读取任意关闭UDPBroadcastProvider
        try {
            System.in.read();
            provider.exit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Provider extends Thread{
        private final String sn;
        private Boolean flag = true;
        private DatagramSocket datagramSocket = null;

        public Provider(String sn){
            super();
            this.sn = sn;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("UDPBroadcastProvider start....");
            //让其一直处于监听状态
            while (flag){
                try {
                    //监听20000端口
                    datagramSocket = new DatagramSocket(20000);
                    //定义一个字节数组接收数据
                    byte[] bytes = new byte[512];
                    DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
                    //接收数据
                    datagramSocket.receive(datagramPacket);
                    //获取发送方的IP
                    String ip = datagramPacket.getAddress().getHostAddress();
                    //获取数据长度
                    int length = datagramPacket.getLength();
                    //获取数据
                    String data = new String(datagramPacket.getData(), 0, length);
                    //解析端口号
                    Integer port = MessageCreate.parsePort(data);
                    System.out.println("收到数据：" + "IP:" + ip + "/t PORT:" + port + "/t Length:" + length + "DATA:" + data);

                    if (port != -1){
                        //回传消息
                        String responseStr = MessageCreate.buildWithSN(sn);
                        byte[] responseStrBytes = responseStr.getBytes();
                        DatagramPacket responseDatagramPacket = new DatagramPacket(responseStrBytes, responseStrBytes.length, datagramPacket.getAddress(), port);
                        datagramSocket.send(responseDatagramPacket);
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    close();
                }
                System.out.println("UDPBroadcastProvider finish...");
            }
        }

        /**
         * 关闭方法
         */
        private void close(){
            if (datagramSocket != null){
                datagramSocket.close();
                datagramSocket = null;
            }
        }

        private void exit(){
            flag = false;
            close();
        }
    }
}

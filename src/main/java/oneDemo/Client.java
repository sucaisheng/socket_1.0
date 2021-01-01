package oneDemo;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //获取socket
        Socket socket = new Socket();
        //设置连接时间3000ms
        socket.setSoTimeout(3000);

        //设置连接本地地址，并且设置端口号为2000，超时时间为3000
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000),3000);

        //连接成功，并进行后续流程
        System.out.println("连接成功，并进行后续流程！");
        System.out.println("连接成功" + socket.getLocalAddress() + "P:" + socket.getLocalPort());
        System.out.println("连接成功" + socket.getInetAddress() + "P:" + socket.getPort());

        //调用传输数据方法进行会话
        try{
            todo(socket);
        }catch (Exception e){
            System.out.println(e);
        }finally {
            //会话结束后释放连接资源
            socket.close();
            System.out.println("客户端已退出！");
        }

    }

    //发送数据方法
    public static void todo(Socket socket) throws IOException {
        //获取键盘输入流
        InputStream in = System.in;
        //将 键盘输入流转换成BufferedReader流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        //获取客户端输出流
        OutputStream outputStream = socket.getOutputStream();
        //并将其转换成 打印流
        PrintStream socketPrintStream = new PrintStream(outputStream);

        //获取客户端输入流
        InputStream inputStream = socket.getInputStream();
        //并将其转换成BufferedReader流
        BufferedReader socketBufferReader = new BufferedReader(new InputStreamReader(inputStream));

        Boolean flag = true;

        do {
            //读取键盘数据
            String str = bufferedReader.readLine();
            //发送数据
            socketPrintStream.println(str);

            //读取服务器发送回来的数据
            String echo = socketBufferReader.readLine();
            if ("bye".equalsIgnoreCase(echo)){
                //判断如果是bye，则断开连接，结束会话
                flag = false;
            }else {
                System.out.println(echo);
            }
        }while (flag);

        //资源释放
        socketPrintStream.close();
        socketBufferReader.close();
    }
}

package oneDemo;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //��ȡsocket
        Socket socket = new Socket();
        //��������ʱ��3000ms
        socket.setSoTimeout(3000);

        //�������ӱ��ص�ַ���������ö˿ں�Ϊ2000����ʱʱ��Ϊ3000
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000),3000);

        //���ӳɹ��������к�������
        System.out.println("���ӳɹ��������к������̣�");
        System.out.println("���ӳɹ�" + socket.getLocalAddress() + "P:" + socket.getLocalPort());
        System.out.println("���ӳɹ�" + socket.getInetAddress() + "P:" + socket.getPort());

        //���ô������ݷ������лỰ
        try{
            todo(socket);
        }catch (Exception e){
            System.out.println(e);
        }finally {
            //�Ự�������ͷ�������Դ
            socket.close();
            System.out.println("�ͻ������˳���");
        }

    }

    //�������ݷ���
    public static void todo(Socket socket) throws IOException {
        //��ȡ����������
        InputStream in = System.in;
        //�� ����������ת����BufferedReader��
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        //��ȡ�ͻ��������
        OutputStream outputStream = socket.getOutputStream();
        //������ת���� ��ӡ��
        PrintStream socketPrintStream = new PrintStream(outputStream);

        //��ȡ�ͻ���������
        InputStream inputStream = socket.getInputStream();
        //������ת����BufferedReader��
        BufferedReader socketBufferReader = new BufferedReader(new InputStreamReader(inputStream));

        Boolean flag = true;

        do {
            //��ȡ��������
            String str = bufferedReader.readLine();
            //��������
            socketPrintStream.println(str);

            //��ȡ���������ͻ���������
            String echo = socketBufferReader.readLine();
            if ("bye".equalsIgnoreCase(echo)){
                //�ж������bye����Ͽ����ӣ������Ự
                flag = false;
            }else {
                System.out.println(echo);
            }
        }while (flag);

        //��Դ�ͷ�
        socketPrintStream.close();
        socketBufferReader.close();
    }
}

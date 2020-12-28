import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        //获取服务器端端口号
        ServerSocket serverSocket = new ServerSocket(2000);

        //连接成功，并进行后续流程
        System.out.println("连接成功，并进行后续流程！");
        System.out.println("连接成功" + serverSocket.getInetAddress() + "P:" + serverSocket.getLocalPort());

        //等待客户端连接
        for (;;){
           //拿到客户端
            Socket socket = serverSocket.accept();
            //构建客户端异步线程
            ClientHandle clientHandle = new ClientHandle(socket);
            //启动线程
            clientHandle.start();
        }
    }

    /**
     * 客户端消息处理
     */
    private static class ClientHandle extends Thread{
        private Socket socket;
        private Boolean flag = true;

        ClientHandle(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("连接成功" + socket.getInetAddress() + "P:" + socket.getPort());

            try{
                //获取客户端输出流，用于服务器回传数据
                PrintStream printStream = new PrintStream(socket.getOutputStream());
                //获取输入流，用于读取数据
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                do {
                    //获取客户端发送的数据
                    String str = bufferedReader.readLine();
                    if ("bye".equalsIgnoreCase(str)){
                        flag = false;
                        printStream.println("bye");
                    }else {
                        System.out.println(str);
                        printStream.println("成功收到数据！");
                    }
                }while (flag);

                //关闭数据流释放资源
                printStream.close();
                bufferedReader.close();

            }catch (Exception e){
                System.out.println("异常断开！");
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端已关闭! "+ socket.getInetAddress() + "P:" + socket.getPort());
        }
    }
}

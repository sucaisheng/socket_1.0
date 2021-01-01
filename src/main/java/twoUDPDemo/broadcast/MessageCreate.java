package twoUDPDemo.broadcast;

/**
 * 定义消息格式的类
 */
public class MessageCreate {
    private static final String SN_HEADER = "This is a SN:";
    private static final String PORT_HEADER = "This is Searcher:";

    /**
     * searcher消息格式
     * @param port
     * @return
     */
    public static String buildWithPort(int port){
        return PORT_HEADER + port;
    }

    /**
     * 解析port
     * @param PORT
     * @return
     */
    public static Integer parsePort(String PORT){
        if (PORT.startsWith(PORT_HEADER)){
            return Integer.parseInt(PORT.substring(PORT_HEADER.length()));
        }
        return -1;
    }

    /**
     * 广播接收者的消息格式
     * @param sn
     * @return
     */
    public static String buildWithSN(String sn){
        return SN_HEADER + sn;
    }

    /**
     * 解析SN
     * @param sn
     * @return
     */
    public static String parseSN(String sn){
        if (sn.startsWith(SN_HEADER)){
            return sn.substring(SN_HEADER.length());
        }
        return null;
    }
}

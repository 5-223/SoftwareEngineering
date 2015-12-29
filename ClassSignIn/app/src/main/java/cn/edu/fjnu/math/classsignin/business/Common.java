package cn.edu.fjnu.math.classsignin.business;

import java.io.IOException;

public class Common {

    /**
     * 连接服务器获取所有学号
     * @return
     * @throws IOException
     */
    public static String[] getIds() throws IOException {
        String resStr = Server.connect("?action=getids");
        String[] result = resStr.split(",");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }
}

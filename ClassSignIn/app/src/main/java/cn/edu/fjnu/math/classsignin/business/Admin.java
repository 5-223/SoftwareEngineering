package cn.edu.fjnu.math.classsignin.business;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * http://localhost:8080/ClassSignIn/?action=getpwd
 * http://localhost:8080/ClassSignIn/?action=getstatus
 * http://localhost:8080/ClassSignIn/?action=initsignin
 * http://localhost:8080/ClassSignIn/?action=setaddress&address=11-05-5D-E8-0F-A3
 */
public class Admin {
    /**
     * 发送URL，服务器返回true或者false。
     * @param address
     * @throws IOException
     * 如果无法连接服务器就会抛出异常。
     */
    public static boolean setMacAddress(String address) throws IOException {
        //todo: 设置蓝牙mac地址
        String serverHost = Server.mServerHost;

        URL url = new URL(serverHost + "?action=setaddress&address=" + address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(reader);
        String resultStr = br.readLine();
        reader.close();
        return true;
    }

    /**
     * 登陆验证，URL：?action=getpwd
     * 返回：学委的密码
     * @param pwd
     * @return
     * @throws IOException
     * 无法连接服务器的异常
     */
    public static boolean check(String pwd) throws IOException {
        String serverHost = Server.mServerHost;

        //连接服务器查询密码
        URL url = new URL(serverHost + "?action=getpwd");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(reader);
        String correctPwd = br.readLine();
        reader.close();

        //检查密码是否正确
        if (!pwd.equals(correctPwd)) {
            return false;
        }
        return true;
    }

    /**
     * 查询目前签到的情况。
     * 服务器返回的是JSON数据。
     * @return
     */
    public static Map<String, Boolean> getSigninStatus() throws IOException, JSONException {
        Map<String, Boolean> map = new TreeMap<>();
        String resultStr;

        //从服务器加载json
        String serverHost = Server.mServerHost;
        URL url = new URL(serverHost + "?action=getstatus");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(reader);
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        resultStr = sb.toString();

        //解析json
        JSONObject jsonObject = new JSONObject(resultStr);
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        for (int i = 0; i < jsonArray.length(); i++) {
            String s = jsonArray.getString(i);
            String[] student = s.split(":");
            if (student[1].equals("True")) {
                map.put(student[0], true);
            } else {
                map.put(student[0], false);
            }
        }
        return map;
    }

    public static void initSignin() throws IOException {
        Server.connect("?action=initsignin");
    }
}

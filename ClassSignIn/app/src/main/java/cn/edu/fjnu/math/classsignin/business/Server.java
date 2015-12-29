package cn.edu.fjnu.math.classsignin.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Server {
    public static String mServerHost;

    public static String connect(String urlStr) throws IOException {
        URL url = new URL(mServerHost + urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(reader);
        String resultStr = br.readLine();
        br.close();
        return resultStr;
    }
}

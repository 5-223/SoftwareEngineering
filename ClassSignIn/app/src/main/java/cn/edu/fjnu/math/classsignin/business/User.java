package cn.edu.fjnu.math.classsignin.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * http://localhost:8080/ClassSignIn/?action=getaddress
 * http://localhost:8080/ClassSignIn/?action=signin&id=105032013096
 * http://localhost:8080/ClassSignIn/?action=getmystatus&id=105032013095
 */
public class User {
    public static String getAddress() throws IOException {
        return Server.connect("?action=getaddress");
    }

    public static void signIn(String id) throws IOException {
        Server.connect("?action=signin&id=" + id);
    }

    public static boolean getMyStatus(String id) throws IOException {
        String resStr = Server.connect("?action=getmystatus&id=" + id);
        if (resStr.equals("True")) {
            return true;
        }
        return false;
    }

}

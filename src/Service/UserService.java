/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Entity.User;
import Util.Util;
import com.codename1.capture.Capture;
import com.codename1.io.rest.Rest;
import com.mycompany.myapp.MyApplication;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ding
 */
public class UserService {

    public final static String API_URL = "http://192.168.43.65/PiWeb/web/app_dev.php";
    public final static String API_PATH = API_URL + "/api/";

    public static boolean login(String username, String password) {
        String loginUrl = API_URL + "/oauth/v2/token";
        String responseData = Rest.post(loginUrl)
                .queryParam("grant_type", "password")
                .queryParam("client_id", "1_3bcbxd9e24g0gk4swg0kwgcwg4o8k8g4g888kwc44gcc0gwwk4")
                .queryParam("client_secret", "4ok2x70rlfokc8g0wws8c8kwcokw80k44sg48goc0ok4w0so0k")
                .queryParam("username", username)
                .queryParam("password", password)
                .getAsString()
                .getResponseData();
        boolean loggedIn = !responseData.contains("error");
        if (loggedIn) {
            MyApplication.currentUser = get(username);
            return true;
        } else {
            return false;
        }

    }

    public static void update(User u) {
        String loginUrl = API_URL + "/users";
        String responseData = Rest.post(loginUrl)
                .queryParam("id", "" + u.getId())
                .queryParam("username", u.getUsername())
                .queryParam("phone", u.getEmail())
                .queryParam("photo", u.getEmail())
                .queryParam("email", u.getEmail())
                .getAsString()
                .getResponseData();
        System.out.println(responseData);
    }

    public static User get(String username) {
        String url = API_PATH + "users/" + username;
        Map responseData = Rest.get(url)
                .getAsJsonMap()
                .getResponseData();
        return mapToUser(responseData);
    }

    void changePicture() {
        String fileName = Capture.capturePhoto();
        if (fileName != null) {
            String url = Util.uploadProfilePicture(fileName);
        }
    }

    public static User mapToUser(Map responseData) {
        User u = new User();
        u.setId(((Double) responseData.get("id")).intValue());
        u.setUsername((String) responseData.get("username"));
        u.setEmail((String) responseData.get("email"));
        u.setEnabled(true);
        u.setPassword((String) responseData.get("password"));
        u.setPhone((String) responseData.get("phone"));

        u.setPhotoprofil(responseData.containsKey("profile_picture")
                ? (String) responseData.get("profile_picture") : "");
        return u;

    }

    public static List<User> getAll() {
        List u = new ArrayList<>();
        String url = API_PATH + "users";
        Map responseData = Rest.get(url)
                .getAsJsonMap()
                .getResponseData();
        List l = (ArrayList) responseData.get("root");
        for (Object x : l) {
            u.add(mapToUser((Map) x));
        }
        System.out.println(u);
        return u;
    }
    /*
    public User get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void add(User a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void (User a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(User a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteId(int a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     */
}

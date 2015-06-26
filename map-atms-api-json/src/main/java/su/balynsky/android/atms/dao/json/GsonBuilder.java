package su.balynsky.android.atms.dao.json;

import com.google.gson.Gson;

/**
 * @author Sergey Balynsky
 *         on 17.04.2015 22:51
 */
public class GsonBuilder {
    public static Gson createGsonFactory() {
        com.google.gson.GsonBuilder gson = new com.google.gson.GsonBuilder();
        return gson.create();
    }

}

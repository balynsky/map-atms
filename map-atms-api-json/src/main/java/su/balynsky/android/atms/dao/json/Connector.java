package su.balynsky.android.atms.dao.json;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import su.balynsky.android.atms.DEBUG;
import su.balynsky.android.atms.dao.json.base.IRequest;
import su.balynsky.android.atms.dao.json.base.ResponseBase;
import su.balynsky.android.atms.exceptions.ConnectorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Синглтон для выполнения http-запросов к ПУМБ онлайн. Хранит переменные
 * сессии, логин, хеш пароля. Используется всеми классами пакета для
 * непосредственного выполнения запросов
 *
 * @author Sergey Balynsky
 */
public final class Connector {

    private static volatile Connector instance;
    private static final String TAG = Connector.class.getCanonicalName();

    public static final String ANDROID_LANG_UA = "uk";
    public static final String ANDROID_LANG_EN = "en";
    public static final int IPUMB_LANG_UA = 0;
    public static final int IPUMB_LANG_RU = 1;
    public static final int IPUMB_LANG_EN = 2;

    private static final int MAX_ATTEMPTS_TO_RECONNECT = 3;

    private HttpClient client = null;
    private Gson gson = null;

    private static Logger logger = Logger.getLogger("org.apache.http");

    private HttpPost httpRequest;

    private Connector() {
        // Включаем логирование HTTP-запросов
        OutputStream outStream = System.out;
        StreamHandler handler = new StreamHandler(outStream, new SimpleFormatter());
        handler.setLevel(Level.OFF);
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.OFF);

        int language = -1;
        if (ANDROID_LANG_UA.equalsIgnoreCase(Locale.getDefault().getLanguage()))
            language = IPUMB_LANG_UA;
        else if (ANDROID_LANG_EN.equalsIgnoreCase(Locale.getDefault().getLanguage().substring(0, 2)))
            language = IPUMB_LANG_EN;
        else
            language = IPUMB_LANG_RU;

        //Log.d(TAG, "Language code: " + language);
        logger.fine(TAG + ": " + "Language code: " + language);
    }

    public static Connector getInstance() {
        if (instance == null)
            synchronized (Connector.class) {
                if (instance == null)
                    instance = new Connector();
            }
        return instance;
    }

    /**
     * Вызвать REST-сервис iPUMB
     *
     * @param baseURL - базовый URL запроса
     * @param request - запрос к REST-сервису iPUMB
     * @return Ответ REST-сервиса iPUMB
     */
    public synchronized ResponseBase call(String baseURL, IRequest request) throws ConnectorException {

        if (baseURL == null || baseURL.length() == 0 || request == null) {
            return null;
        }

        BufferedReader in = null;
        ResponseBase apiResponse = null;

        try {
            //request.setLanguage(language);
            if (client == null) {
                client = HttpClientBuilder.createHttpClient();
            }

            if (gson == null) {
                gson = su.balynsky.android.atms.dao.json.GsonBuilder.createGsonFactory();
            }

            //Log.d(TAG, "JSON service: " + request.getRestUrl());
            logger.fine(TAG + ": " + "JSON service: " + request.getRestUrl());

            httpRequest = new HttpPost();
            httpRequest.setURI(new URI(baseURL + request.getRestUrl()));
            String json = gson.toJson(request);

            logger.fine(TAG + ": " + "JSON request: " + json);

            httpRequest.setEntity(new StringEntity(json, "UTF-8"));
            httpRequest.setHeader("content-type", "application/json");
            httpRequest.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

            boolean succeed = false;
            int retries = 0;

            while (!succeed) {
                try {
                    HttpResponse response = client.execute(httpRequest);
                    httpRequest = null;
                    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

                    StringBuilder sb = new StringBuilder("");
                    String line;
                    String NL = System.getProperty("line.separator");

                    //Log.d(TAG, "Web page retrived. Sending content to a console...");
                    logger.fine(TAG + ": " + "Web page retrived. Sending content to a console...");

                    while ((line = in.readLine()) != null) {
                        sb.append(line).append(NL);
                        //Log.d(TAG, line);
                        logger.fine(TAG + ": " + line);
                    }

                    //Log.d(TAG, "End of the Web-page");
                    logger.fine(TAG + ": " + "End of the Web-page");

                    in.close();
                    String page = sb.toString();
                    if (DEBUG.DEBUG_MODE) {
                        System.out.println("Request [" + request.getRestUrl() + "]:\n" + json);
                        System.out.println("Response:\n" + page);
                    }
                    apiResponse = gson.fromJson(page, request.getResponseClass());
                    succeed = true;
                } catch (Exception e) {
                    retries++;
                    e.printStackTrace();
                    if (retries >= MAX_ATTEMPTS_TO_RECONNECT) {
                        throw e;
                    }
                }
            }

        } catch (Exception e) {
            throw new ConnectorException("Connection error: " + e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new ConnectorException("Error while closing input stream", e);
                }
            }
        }

        if (!apiResponse.getSuccess()) {
            throw new ConnectorException(apiResponse.getError());
        }
        return apiResponse;
    }

}

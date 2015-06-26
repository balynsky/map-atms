package su.balynsky.android.atms.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.util.List;

/**
 * @author Sergey Balynsky
 */
public class BaseMapFragment extends BaseFragment {
    public static final String EMPTY_STRING = "";
    public static final String STRING_COMMA = ",";
    public static final String ENDL = "<\b>";

    public static final String PREF_NAME = BaseMapFragment.class.getPackage().getName();
    public static final String KEY_MAP_TYPE = "MapType";

    protected LocationManager locationManager;

    private Boolean isGPSEnabled;
    private Boolean isNetworkEnabled;

    public enum MapType {
        MAP_TYPE_GOOGLE, MAP_TYPE_YANDEX
    }

    // Киев, ул. Андреевская, 4
    public static final String DEFAULT_LONGITUDE = "30.521375";
    public static final String DEFAULT_LATITUDE = "50.460737";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    protected boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = (connManager == null) ? null : connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (mWifi != null && mWifi.isConnected());
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public Boolean getIsGPSEnabled() {
        return isGPSEnabled;
    }

    public Boolean getIsNetworkEnabled() {
        return isNetworkEnabled;
    }

    public Location getLocation() {
        if (locationManager == null) {
            return null;
        }

        long minTime = Long.MIN_VALUE;
        float bestAccuracy = Float.MAX_VALUE;
        Location bestResult = null;
        long bestTime = 0;

        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider : matchingProviders) {

            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                float accuracy = location.getAccuracy();

                long time = location.getTime();

                if ((time > minTime && accuracy < bestAccuracy)) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestTime = time;
                } else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
                    bestResult = location;
                    bestTime = time;
                }
            }
        }

        return bestResult;
    }

    public String generateMessageForBallon(String name, String address, String phone, boolean reverse) {
        StringBuilder result = new StringBuilder();
        result.append(name).append(STRING_COMMA);
        if (address != null) {
            if (reverse) {
                result.append(reverseAddress(address)).append(STRING_COMMA);
            } else {
                result.append(address).append(STRING_COMMA);
            }
        }
        if (phone != null) {
            result.append("  " + phone);
        }
        return result.toString();
    }

    private String reverseAddress(String address) {
        StringBuilder result;
        if (address == null) {
            return null;
        }

        String[] addr = address.split(STRING_COMMA);
        if (addr == null || addr.length == 0) {
            return address;
        }
        result = new StringBuilder();
        for (int i = 1; i < addr.length + 1; i++) {
            result.append(addr[addr.length - i]);
            if (i != addr.length) {
                result.append(STRING_COMMA);
            }
        }

        return result.toString();
    }

}

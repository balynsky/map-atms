package su.balynsky.android.atms.activity.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import su.balynsky.android.atms.model.atm.AtmInfo;

/**
 * @author Sergey Balynsky
 *         on 22.01.2015.
 */
public class GoogleMapClusterItem implements ClusterItem {
    private final LatLng position;
    private final AtmInfo atmInfo;

    public GoogleMapClusterItem(double lat, double lng, AtmInfo atmInfo) {
        position = new LatLng(lat, lng);
        this.atmInfo = atmInfo;
    }

    public GoogleMapClusterItem(double lat, double lng) {
        position = new LatLng(lat, lng);
        this.atmInfo = null;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public AtmInfo getAtmInfo() {
        return atmInfo;
    }
}

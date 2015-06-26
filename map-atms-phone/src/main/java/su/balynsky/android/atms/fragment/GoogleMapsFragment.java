package su.balynsky.android.atms.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import su.balynsky.android.atms.activity.map.GoogleMapClusterItem;
import su.balynsky.android.atms.content.helper.AtmsProviderHelper;
import su.balynsky.android.atms.content.helper.CursorToAtmHelper;
import su.balynsky.android.atms.content.provider.BaseContentProvider;
import su.balynsky.android.atms.model.atm.AtmInfo;
import su.balynsky.android.atms.phone.R;
import su.balynsky.android.atms.utils.PackagesUtils;

import java.util.Locale;

/**
 * @author Sergey Balynsky
 *         on 28.01.2015.
 */
public class GoogleMapsFragment extends BaseMapFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static String TAG = GoogleMapsFragment.class.getCanonicalName();
    private static final float ZOOM_AT_STARTUP = 15;
    private static final long TIME_TO_PAUSE_AFTER_TOAST = 5000;
    private static final int MAX_DESCR_COUNT_IN_SNIPPET = 5;
    private static final String ENDL = "\r\n";
    private static final String EMPTY_STRING = "";
    private static final String AND_MORE = "...";

    private static final String GOOGLE_NAVIGATOR_URI_FORMAT = "google.navigation:q=%f,%f";
    private static final String GOOGLE_MAPS_URI_FORMAT = "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f";
    private static final String GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps";
    private static final String GOOGLE_MAPS_ACTIVITY = "com.google.android.maps.MapsActivity";
    public static final int LOADER_ATMS_ID = 0;


    private GoogleMap googleMap;
    private ClusterManager<GoogleMapClusterItem> clusterManager;

    private boolean isCameraPositioned = false;
    private boolean isActivityCreatedCorrectly = false;


    private class ClusterRenderer extends DefaultClusterRenderer<GoogleMapClusterItem> {

        public ClusterRenderer() {
            super(getActivity().getApplicationContext(), getMap(), clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(GoogleMapClusterItem item, MarkerOptions markerOptions) {

            if (item.getAtmInfo() != null) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_atm));
                markerOptions.title(item.getAtmInfo().getName());
                markerOptions.snippet(generateMessageForBallon(EMPTY_STRING, item.getAtmInfo().getAddressL1(), item
                        .getAtmInfo().getTelephone(), false));

            }
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 3;
        }
    }


    private GoogleMap getMap() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_google_fragment)).getMap();
        }
        return googleMap;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader");
        switch (id) {
            case LOADER_ATMS_ID: {
                return AtmsProviderHelper.getLoader(getActivity().getApplicationContext(), args);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        if (loader.getId() == LOADER_ATMS_ID && data != null && BaseContentProvider.isCursorReady(data)) {
            Log.d(TAG, loader.toString());
            for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
                AtmInfo info = CursorToAtmHelper.getAtmInfo(data);
                Double latitude = Double.parseDouble(info.getLatitude());
                Double longitude = Double.parseDouble(info.getLongitude());
                clusterManager.addItem(new GoogleMapClusterItem(latitude, longitude, info));
            }
        }
        positioningCameraForFirstTime();
    }

    private void positioningCameraForFirstTime() {
        if (isCameraPositioned) {
            return;
        }
        Location location = getLocation();
        Double latitude = 50.449710;
        Double longtitude = 30.594832;
        float zoom = ZOOM_AT_STARTUP; // + 1;
        if (location != null) {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
        } else {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longtitude), zoom));
        }
        isCameraPositioned = true;
    }


    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_google_map, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            // Установки для карты
            getMap().setBuildingsEnabled(true);
            getMap().setTrafficEnabled(true);
            getMap().setMyLocationEnabled(true);
            getMap().getUiSettings().setMyLocationButtonEnabled(true);
            getMap().setTrafficEnabled(false);

            // Создаем менеджер кластеров
            clusterManager = new ClusterManager<GoogleMapClusterItem>(getActivity().getApplicationContext(), getMap());

            clusterManager.setRenderer(new ClusterRenderer());

            // Вешаем слушателей на карту
            getMap().setOnCameraChangeListener(clusterManager);
            getMap().setOnMarkerClickListener(clusterManager);

            getMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                public void onInfoWindowClick(Marker marker) {
                    navigateTo(marker.getPosition());
                }
            });

            // Вешаем слушателей на менеджер кластеров
            clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<GoogleMapClusterItem>() {

                public boolean onClusterClick(Cluster<GoogleMapClusterItem> cluster) {
                    int atmsCount = 0;
                    for (GoogleMapClusterItem item : cluster.getItems()) {
                        if (item.getAtmInfo() != null)
                            atmsCount++;
                    }

                    StringBuilder sbSummaryText = new StringBuilder();
                    sbSummaryText.append(getString(R.string.google_map_cluster_info_atms_count))
                            .append(Integer.toString(atmsCount)).append(ENDL);
                    Toast.makeText(getActivity().getApplicationContext(), sbSummaryText.toString(), Toast.LENGTH_LONG).show();

                    return true;
                }
            });

            isActivityCreatedCorrectly = true;
        } catch (Exception e) {
            // Это значит, что карты Google не доступны
            // например, не установлено приложение Google Maps
            // К сожалению, более красивого способа это проверить я не нашел
            e.printStackTrace();
            isActivityCreatedCorrectly = false;
        }

    }

    private void navigateTo(LatLng position) {
        Context context = getActivity().getApplicationContext();

        // Проверяем, установлены ли Google Maps
        PackagesUtils packagesUtils = new PackagesUtils();
        if (packagesUtils.isPackageInstalled(context, GOOGLE_MAPS_PACKAGE, false)) {

            // Определяем координаты начальной и конечной точек маршрута
            //Location currentLocation = mapUtil.getLocation(context);
            Location currentLocation = getMap().getMyLocation();
            String uri = String.format(Locale.ENGLISH, GOOGLE_MAPS_URI_FORMAT, currentLocation.getLatitude(),
                    currentLocation.getLongitude(), position.latitude, position.longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            // Явно указываем приложение и активити
            intent.setClassName(GOOGLE_MAPS_PACKAGE, GOOGLE_MAPS_ACTIVITY);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                // Такого не должно быть никогда, т.к. выше дважды убедились, что карты установлены.
                // Но если мы дошли сюда, то intent все равно будет обработан и карта с маршрутом должна открыться в браузере
                try {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(unrestrictedIntent);
                } catch (ActivityNotFoundException innerEx) {
                    //
                    Toast.makeText(getActivity().getApplicationContext(), R.string.google_map_not_available, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(context, R.string.google_map_not_available, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(LOADER_ATMS_ID, new Bundle(), this);
    }


}

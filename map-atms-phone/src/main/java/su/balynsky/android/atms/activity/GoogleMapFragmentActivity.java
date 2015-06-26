package su.balynsky.android.atms.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import su.balynsky.android.atms.activity.common.DrawerMenuItemIdentifier;
import su.balynsky.android.atms.content.provider.PlaceProvider;
import su.balynsky.android.atms.phone.R;

/**
 * @author Sergey Balynsky
 *         04.02.2015 16:11
 */
public class GoogleMapFragmentActivity extends NavigationDrawerBaseActivity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = GoogleMapFragmentActivity.class.getCanonicalName();
    public static final int PLACE_LOADER = 0;

    private GoogleMap map;

    @Override
    protected Integer getSelectedItem() {
        return DrawerMenuItemIdentifier.GOOGLE_ATM_MENU;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.google_map);
        SupportMapFragment googleMap = (SupportMapFragment) fragment.getChildFragmentManager().findFragmentById(R.id.map_google_fragment);
        map = googleMap.getMap();
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_VIEW)) {
                getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void getPlace(String query) {
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(PLACE_LOADER, data, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        Log.d(TAG, searchItem.toString());
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchView != null) {
            searchItem.setVisible(true);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        } else {
            Log.d(TAG, "SearchView is null");
        }
        return true;
    }

    public boolean onQueryTextChange(String text_new) {
        Log.d(TAG, "New text is " + text_new);
        return true;
    }

    public boolean onQueryTextSubmit(String text) {
        Log.d(TAG, "Search text is " + text);
        getPlace(text);
        return true;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        Log.d(TAG, "onCreateLoader ");
        CursorLoader cLoader = null;
        if (arg0 == PLACE_LOADER) {
            Log.d(TAG, "Loader = PlaceProvider.DETAILS_URI ");
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{query.getString("query")}, null);
        }
        return cLoader;
    }


    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> arg0, Cursor c) {
        showLocations(c);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> arg0) {
    }

    private void showLocations(Cursor c) {
        LatLng position = null;
        if (c.moveToNext()) {
            position = new LatLng(Double.parseDouble(c.getString(1)), Double.parseDouble(c.getString(2)));
        }
        if (position != null && c.isLast()) {
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
            map.animateCamera(cameraPosition);
        }
    }

}

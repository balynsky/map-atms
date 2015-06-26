package su.balynsky.android.atms.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import su.balynsky.android.atms.activity.common.DrawerMenuItemIdentifier;
import su.balynsky.android.atms.listeners.AboutAppOnClickListener;
import su.balynsky.android.atms.listeners.ExitClickListener;
import su.balynsky.android.atms.listeners.LogcatOnClickListener;
import su.balynsky.android.atms.listeners.RunGoogleMapOnClickListener;
import su.balynsky.android.atms.phone.R;


/**
 * @author Sergey Balynsky
 *         23.02.2015 13:18
 */
public abstract class NavigationDrawerBaseActivity extends BaseActivity {
    private static final String TAG = NavigationDrawerBaseActivity.class.getCanonicalName();
    private Drawer.Result drawerResult = null;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onCreateNavigationDrawer(getSelectedItem());
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onCreateNavigationDrawer(getSelectedItem());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onCreateNavigationDrawer(getSelectedItem());
    }

    protected abstract Integer getSelectedItem();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected Integer getToolbarTitle() {
        return R.string.app_name;
    }

    protected void onCreateNavigationDrawer(Integer selectedItem) {
        Drawer drawer = new Drawer();
        // Инициализируем Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            drawer.withToolbar(toolbar);
            getSupportActionBar().setTitle(getToolbarTitle());
        }


        AccountHeader.Result headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(
                        new ProfileDrawerItem().withIcon(getResources().getDrawable(R.drawable.icon))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public void onProfileChanged(View view, IProfile profile) {
                    }
                })
                .withCompactStyle(true)
                .withSelectionListEnabled(false)
                .build();


        // Инициализируем Navigation Drawer
        drawerResult = drawer
                .withActivity(this)
                        //.withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                        //.withHeader(R.layout.drawer_header)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(DrawerMenuItemIdentifier.GOOGLE_ATM_MENU).withName(R.string.main_menu_deposits_item).withIcon(FontAwesome.Icon.faw_map_marker),
                        new PrimaryDrawerItem().withIdentifier(DrawerMenuItemIdentifier.ABOUT_APP_MENU).withName(R.string.main_menu_aboutapp_item).withIcon(FontAwesome.Icon.faw_question_circle),
                        //TODO only for debug
                        //new PrimaryDrawerItem().withIdentifier(DrawerMenuItemIdentifier.LOGCAT_MENU).withName(R.string.main_menu_logcat_button).withIcon(FontAwesome.Icon.faw_github),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(DrawerMenuItemIdentifier.EXIT_MENU).withName(R.string.main_menu_exit_button).withIcon(FontAwesome.Icon.faw_sign_out)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Скрываем клавиатуру при открытии Navigation Drawer
                        InputMethodManager inputMethodManager = (InputMethodManager) NavigationDrawerBaseActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(NavigationDrawerBaseActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // Обработка клика
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            //Toast.makeText(NavigationDrawerBaseActivity.this, NavigationDrawerBaseActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Menu identifier: " + drawerItem.getIdentifier());
                            switch (drawerItem.getIdentifier()) {
                                case DrawerMenuItemIdentifier.GOOGLE_ATM_MENU: {
                                    (new RunGoogleMapOnClickListener(NavigationDrawerBaseActivity.this)).onClick(null);
                                    break;
                                }

                                case DrawerMenuItemIdentifier.ABOUT_APP_MENU: {
                                    (new AboutAppOnClickListener(NavigationDrawerBaseActivity.this)).onClick(null);
                                    break;
                                }

                                case DrawerMenuItemIdentifier.LOGCAT_MENU: {
                                    (new LogcatOnClickListener(NavigationDrawerBaseActivity.this)).onClick(null);
                                    break;
                                }

                                case DrawerMenuItemIdentifier.EXIT_MENU: {
                                    (new ExitClickListener(NavigationDrawerBaseActivity.this)).onClick(null);
                                    break;
                                }
                            }

                        }
                        if (drawerItem instanceof Badgeable) {
                            Badgeable badgeable = (Badgeable) drawerItem;
                            if (badgeable.getBadge() != null) {
                                // учтите, не делайте так, если ваш бейдж содержит символ "+"
                                try {
                                    int badge = Integer.valueOf(badgeable.getBadge());
                                    if (badge > 0) {
                                        drawerResult.updateBadge(String.valueOf(badge - 1), position);
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "Не нажимайте на бейдж, содержащий плюс! :)");
                                }
                            }
                        }
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    // Обработка длинного клика, например, только для SecondaryDrawerItem
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof SecondaryDrawerItem) {
                            Toast.makeText(NavigationDrawerBaseActivity.this, NavigationDrawerBaseActivity.this.getString(((SecondaryDrawerItem) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .build();

        Log.d(TAG, "Selected Item " + selectedItem);
        drawerResult.setSelectionByIdentifier(selectedItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Cliced MenuItem is " + item.getTitle());
        return super.onOptionsItemSelected(item);
    }

}

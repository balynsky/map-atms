package su.balynsky.android.atms.provider;

import com.google.inject.Provider;
import com.squareup.otto.Bus;

/**
 * @author Balynsky Sergey
 *         on 20.01.2015.
 */
public class BusProvider implements Provider<Bus> {

    private static Bus bus;

    public static void setBus(Bus bus) {
        BusProvider.bus = bus;
    }

    public Bus get() {
        return bus;
    }
}
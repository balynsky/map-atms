package su.balynsky.android.atms.bus;

import android.content.Context;
import android.os.Handler;
import com.google.inject.Inject;
import com.squareup.otto.Bus;

/**
 * @author Balynsky Sergey
 *         on 19.01.2015.
 */
public class BusClient {
    private Context context;
    private Bus     bus;

    @Inject
    private BusClient(Context context, Bus bus) {
        this.context = context;
        this.bus = bus;
    }

    public void post(final Object message) {
        new Handler(context.getMainLooper()).post(new Runnable() {
            public void run() {
                bus.post(message);
            }
        });
    }

    public Context getContext() {
        return context;
    }

    public Bus getBus() {
        return bus;
    }


}

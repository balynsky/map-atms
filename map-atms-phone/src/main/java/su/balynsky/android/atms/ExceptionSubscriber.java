package su.balynsky.android.atms;

import android.content.Context;
import android.widget.Toast;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;
import su.balynsky.android.atms.exceptions.APIException;

/**
 * @author Sergey Balynsky
 *         on 20.01.2015.
 */
public class ExceptionSubscriber {

    private Context context;

    @Inject
    public ExceptionSubscriber(Context context) {
        this.context = context;
    }

    @Subscribe
    public void onException(APIException exception) {
        exception.printStackTrace();
        Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

}

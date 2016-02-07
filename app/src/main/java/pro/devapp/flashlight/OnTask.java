package pro.devapp.flashlight;

import android.os.AsyncTask;

/**
 * Created by arseniy on 04/02/16.
 */
public class OnTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        CameraHolder.start();
        return null;
    }
}

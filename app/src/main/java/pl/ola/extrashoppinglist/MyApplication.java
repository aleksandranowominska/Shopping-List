package pl.ola.extrashoppinglist;

import android.app.Application;
import io.objectbox.BoxStore;

/**
 * Created by Aleksandra Kusiak on 22.11.2017.
 */

public class MyApplication extends Application {
    BoxStore boxStore;
    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}

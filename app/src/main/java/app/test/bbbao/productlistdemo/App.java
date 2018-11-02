package app.test.bbbao.productlistdemo;

import android.app.Application;

import app.test.bbbao.productlistdemo.Utils.Density;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Density.setDensity(this,360);
    }
}

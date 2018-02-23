package no.stonehill.preppers;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import no.stonehill.preppers.di.AppComponent;
import no.stonehill.preppers.di.DaggerAppComponent;

public class PrepperApplication extends DaggerApplication {

    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        return appComponent;
    }
}
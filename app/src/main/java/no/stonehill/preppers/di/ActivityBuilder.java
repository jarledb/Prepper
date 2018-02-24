package no.stonehill.preppers.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;
import javax.inject.Singleton;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.stonehill.preppers.mapping.MappingActivity;
import no.stonehill.preppers.mapping.di.MappingActivityModule;

@Module
public abstract class ActivityBuilder {

    @Scope
    @Retention(RetentionPolicy.RUNTIME) public @interface ActivityScope {}

    @Singleton
    @ContributesAndroidInjector(modules = MappingActivityModule.class)
    abstract MappingActivity bindMainActivity();
}
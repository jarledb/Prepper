package no.stonehill.preppers.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.stonehill.preppers.mapping.MappingActivity;
import no.stonehill.preppers.mapping.di.MappingActivityModule;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MappingActivityModule.class)
    abstract MappingActivity bindMainActivity();
}
package no.stonehill.preppers.mapping.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.stonehill.preppers.mapping.OwnPositionRenderer;
import no.stonehill.preppers.mapping.GraphicsHelper;


@Module
public class MappingActivityModule {

    @Provides
    @Singleton
    static OwnPositionRenderer provideOwnPositionRenderer() {
        return new OwnPositionRenderer();
    }

    @Provides
    static GraphicsHelper provideGraphicsHelper() {
        return new GraphicsHelper();
    }
}

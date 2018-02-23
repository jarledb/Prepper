package no.stonehill.preppers.mapping.di;

import dagger.Module;
import dagger.Provides;
import no.stonehill.preppers.mapping.GraphicsHelper;
import no.stonehill.preppers.mapping.OwnPositionRenderer;

@Module
public class MappingActivityModule {

    @Provides
    static OwnPositionRenderer provideOwnPositionRenderer() {
        return new OwnPositionRenderer();
    }

    @Provides
    static GraphicsHelper provideGraphicsHelper() {
        return new GraphicsHelper();
    }
}

package no.stonehill.preppers.mapping.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.stonehill.preppers.geo.LocationProvider;
import no.stonehill.preppers.mapping.GraphicsHelper;
import no.stonehill.preppers.mapping.MapController;
import no.stonehill.preppers.mapping.OverlayManager;
import no.stonehill.preppers.mapping.OwnPositionRenderer;

@Module
public class MappingActivityModule {

    @Provides
    @Singleton
    OwnPositionRenderer provideOwnPositionRenderer(GraphicsHelper graphicsHelper, OverlayManager overlayManager, LocationProvider locationProvider) {
        return new OwnPositionRenderer(graphicsHelper, overlayManager, locationProvider);
    }

    @Provides
    @Singleton
    GraphicsHelper provideGraphicsHelper(Context context) {
        return new GraphicsHelper(context);
    }

    @Provides
    @Singleton
    OverlayManager provideOverlayManager() {
        return new OverlayManager();
    }

    @Provides
    @Singleton
    LocationProvider provideLocationProvider(Context context) {
        return new LocationProvider(context);
    }

    @Provides
    @Singleton
    MapController provideMapController(LocationProvider locationProvider, GraphicsHelper graphicsHelper) {
        return new MapController(locationProvider, graphicsHelper);
    }

}

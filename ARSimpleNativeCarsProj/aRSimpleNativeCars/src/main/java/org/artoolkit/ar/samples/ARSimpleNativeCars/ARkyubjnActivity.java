package org.artoolkit.ar.samples.ARSimpleNativeCars;

import android.widget.FrameLayout;

import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tuananh on 20/04/17.
 */

public abstract class ARkyubjnActivity extends ARActivity {
    private static final String DUMMY_TEXTURE = "--dummy--";

    @Override
    protected ARRenderer supplyRenderer() {
        return new SimpleNativeRenderer(this);
    }

    @Override
    protected FrameLayout supplyFrameLayout() {
        return null;
    }

    public final List<TrackableObject3d> getTrackableObject3DList() {
        List<TrackableObject3d> list = new ArrayList<TrackableObject3d>();
        populateTrackableObjects(list);
        return list;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove all the textures from the texture managet
        unloadTextures();
    }

    private void unloadTextures() {
        HashSet<String> names = TextureManager.getInstance().getNames();
        for (String name : names) {
            // DO NOT remove the dummy texture
            if (! name.equals(DUMMY_TEXTURE)) {
                TextureManager.getInstance().removeTexture(name);
            }
        }
    }

    /**
     * Override this method to provide the list of objects that are to be tracked
     * @param list
     */
    protected abstract void populateTrackableObjects(List<TrackableObject3d> list);

    /**
     * Override this method to add extra configuration to the world such as ambient light, etc
     * @param world
     */
    public abstract void configureWorld(World world);
}

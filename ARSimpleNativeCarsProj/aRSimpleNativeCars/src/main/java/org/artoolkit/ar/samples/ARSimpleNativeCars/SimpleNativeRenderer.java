


package org.artoolkit.ar.samples.ARSimpleNativeCars;

import android.util.Log;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Matrix;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.FPSCounter;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SimpleNativeRenderer extends ARRenderer {

   // private final ARkyubjnActivity mActivity;
    private List<TrackableObject3d> mTrackableObjects;

    protected World mWorld;
    protected Camera mCamera;
    protected FrameBuffer mBuffer;
    private Matrix projMatrix = new Matrix();
    private boolean mFovSet;

 /*   public SimpleNativeRenderer(ARkyubjnActivity arJpctActivity) {
        mActivity = arJpctActivity;
    }*/

 /*   *//**
     * Configuration of the AR Scene
     * Initializes jPCT and gets the trackable objects list
     * Registers the markers and adds the objects to the world
     *//*
    @Override
    public boolean configureARScene() {
        // Initialize the game world and the camera
        mWorld = new World();
        mCamera = mWorld.getCamera();
        // Set the FOV based on the camera parameters
        // This FOW is correct when using
        android.hardware.Camera.Parameters params = mActivity.getCameraPreview().getCameraParameters();
        // Setting the FOV based on the camera params, this seems to work fine with 640x480
        float fov = params.getHorizontalViewAngle();
        float yfov = params.getVerticalViewAngle();
        mCamera.setFOV(mCamera.convertDEGAngleIntoFOV(fov));
        mCamera.setYFOV(mCamera.convertDEGAngleIntoFOV(yfov));

        mActivity.configureWorld(mWorld);

        // Get the activity list of trackable objects
        mTrackableObjects = mActivity.getTrackableObject3DList();

        // Load all the markers and add the objects to the world
        for (int i=0; i<mTrackableObjects.size(); i++) {
            TrackableObject3d trackableObject = mTrackableObjects.get(i);
            // Load the marker
            if (! trackableObject.registerMarker() ) {
                // If there was a problem, return false
                return false;
            }
            // Add the object to the world, note that mWorld.addObject is not recursive
            trackableObject.addToWorld(mWorld);
        }

        mWorld.buildAllObjects();

        mFovSet = false;

        return true;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int w, int h) {
        super.onSurfaceChanged(unused, w, h);
        mBuffer = new FrameBuffer(unused,w,h);
    }

    *//**
     * Override the draw function from ARRenderer
     * This one sets the correct position and orientation from the camera
     * For each trackable object, checks if the marker is visible and if so, updates the object
     *//*
    @Override
    public final void draw(GL10 gl) {
        mBuffer.clear();

        float[] projection = ARToolKit.getInstance().getProjectionMatrix();
        projMatrix.setDump(projection);
        SimpleVector translation = projMatrix.getTranslation();

        if (!mFovSet) {
            // Calculate FOV based on projection values, but do it only once
            float value1 = projection[5];
            float vFov = (float) Math.atan2(1, value1)*2;
            mCamera.setYFovAngle(vFov);
            float aspect = projection[5] / projection[0];
            float fov = (float) (2 * Math.atan2(mCamera.getYFOV() , 2 ) * aspect);
            mCamera.setFovAngle(fov);
            mFovSet = true;
        }

        SimpleVector dir = projMatrix.getZAxis();
        SimpleVector up = projMatrix.getYAxis();
        mCamera.setPosition(translation);
        mCamera.setOrientation(dir, up);

        for (int i=0; i<mTrackableObjects.size(); i++) {
            TrackableObject3d trackableObject = mTrackableObjects.get(i);
            trackableObject.updateMarkerTransformation();
        }

        mWorld.renderScene(mBuffer);
        mWorld.draw(mBuffer);
        mBuffer.display();
    }

    *//**
     * Get the world instance used by the renderer.
     * May be null if configureARScene has not yet been called.
     * @return the world instance used by the renderer.
     *//*
    public World getWorld() {
        return mWorld;
    }*/
    // Load the native libraries.
    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("ARWrapper");
        System.loadLibrary("ARWrapperNativeCarsExample");
    }

    private FPSCounter counter = new FPSCounter();

    public SimpleNativeRenderer(ARSimpleNativeCarsActivity arSimpleNativeCarsActivity) {

    }

    public SimpleNativeRenderer(ARkyubjnActivity aRkyubjnActivity) {
        super();
    }

   /* public SimpleNativeRenderer(ARkyubjnActivity aRkyubjnActivity) {

    }*/

    public static native void demoInitialise();

    public static native void demoShutdown();

    public static native void demoSurfaceCreated();

    public static native void demoSurfaceChanged(int w, int h);

    public static native void demoDrawFrame();



    @Override
    public boolean configureARScene() {
        SimpleNativeRenderer.demoInitialise();
        return true;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        super.onSurfaceChanged(gl, w, h);
        SimpleNativeRenderer.demoSurfaceChanged(w, h);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        SimpleNativeRenderer.demoSurfaceCreated();
    }

    @Override
    public void draw(GL10 gl) {
        SimpleNativeRenderer.demoDrawFrame();

        if (counter.frame()) Log.i("demo", counter.toString());

    }

}


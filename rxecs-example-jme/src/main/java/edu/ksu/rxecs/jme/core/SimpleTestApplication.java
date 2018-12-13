package edu.ksu.rxecs.jme.core;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class SimpleTestApplication extends SimpleApplication {

    // use -XstartOnFirstThread as VM arg on mac

    @Override
    public void simpleInitApp() {
        final Box box = new Box(1, 1, 1);
        final Geometry geom = new Geometry("Box", box);
        final Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }

    public static void main(String[] args) {
        final SimpleApplication app = new SimpleTestApplication();
//        fixMacOSXRenderingThreadIssue(app);
        applySettings(app);
//        java.awt.Toolkit.getDefaultToolkit();
//        Configuration.GLFW_CHECK_THREAD0.set(false);
        app.start(false);
    }

    public static void applySettings(SimpleApplication app) {
        final AppSettings settings = new AppSettings(true);
        settings.setFullscreen(true);
        settings.setResolution(1680, 1050);
//        settings.setResolution(1440, 990);
//        settings.setResolution(1024, 660);
        app.setSettings(settings);
    }

//    public static void fixMacOSXRenderingThreadIssue(SimpleApplication app) {
//        final AppSettings settings = new AppSettings(true);
////        settings.setRenderer(AppSettings.JOGL_OPENGL_BACKWARD_COMPATIBLE);
//        settings.setRenderer(AppSettings.LWJGL_OPENGL33);
////        settings.setAudioRenderer(AppSettings.JOAL);
//        app.setSettings(settings);
//    }
}

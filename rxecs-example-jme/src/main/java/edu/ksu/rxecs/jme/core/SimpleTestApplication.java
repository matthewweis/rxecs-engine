package edu.ksu.rxecs.jme.core;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class SimpleTestApplication extends SimpleApplication {

    // IMPORTANT: IF ON MAC, RUN WITH VM ARG: -XstartOnFirstThread

    public static void main(String[] args) {
        final SimpleApplication app = new SimpleTestApplication();
        applySettings(app);
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

    @Override
    public void simpleInitApp() {
        final Box box = new Box(1, 1, 1);
        final Geometry geom = new Geometry("Box", box);
        final Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }
}

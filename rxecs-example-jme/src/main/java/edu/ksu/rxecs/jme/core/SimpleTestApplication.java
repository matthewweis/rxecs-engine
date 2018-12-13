package edu.ksu.rxecs.jme.core;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class SimpleTestApplication extends SimpleApplication {

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
        app.start();
    }
}

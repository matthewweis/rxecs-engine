package edu.ksu.rxecs.jme.core;

import com.gs.collections.api.list.ImmutableList;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.factory.Lists;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.GeometryGroupNode;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import edu.ksu.rxecs.core.ecs.Engine;
import edu.ksu.rxecs.core.ecs.MutableEntity;
import edu.ksu.rxecs.jme.core.components.PositionComponent;
import edu.ksu.rxecs.jme.core.components.ScaleComponent;
import edu.ksu.rxecs.jme.core.systems.MovementSystem;
import edu.ksu.rxecs.jme.core.systems.SizeSystem;
import jme3tools.optimize.GeometryBatchFactory;

import java.util.function.Consumer;

public class BatchedMovingObjectTestApplication extends SimpleApplication {

    // use -XstartOnFirstThread as VM arg on mac

    final MovementSystem movementSystem = new MovementSystem();
    final SizeSystem sizeSystem = new SizeSystem();
    final Engine engine = Engine.builder().addSystem(movementSystem).addSystem(sizeSystem).build();

    final MutableList<Geometry> geoms = Lists.mutable.empty();
    final MutableList<PositionComponent> positionComponents = Lists.mutable.empty();
    final MutableList<ScaleComponent> scaleComponents = Lists.mutable.empty();

    @Override
    public void simpleInitApp() {
        final Box box = new Box(0.75f, 0.75f, 0.75f);





        final Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);

        final Node base = new Node("Base") {
        };
        base.setMaterial(mat);
        rootNode.attachChild(base);

        final MutableList<Geometry> geoms = Lists.mutable.empty();
        for (int i=0; i < 100; i++) {
            geoms.add(new Geometry("Box" + i, box));
        }

        for (int i=0; i < geoms.size(); i++) {
            final Geometry geom = geoms.get(i);
            geom.setMaterial(mat);
            base.attachChild(geom);
            final float x = -10 + (2.0f * (i / 10));
            final float y = -10 + (2.0f * (i % 10));
            final float z = -10.0f;

            geom.move(x, y, z);

            final MutableEntity entity = new MutableEntity();
            final PositionComponent positionComponent = new PositionComponent();
            positionComponent.x = x;
            positionComponent.y = y;
            positionComponent.z = z;
            final ScaleComponent scaleComponent = new ScaleComponent();
            entity.addComponent(positionComponent, engine);
            entity.addComponent(scaleComponent, engine);

            positionComponents.add(positionComponent);
            scaleComponents.add(scaleComponent);

            engine.observe(positionComponent).subscribe(changed -> {
                final PositionComponent c = (PositionComponent) changed;
                geom.move(c.x, c.y, c.z);
            });
        }




    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
//        engine.update(tpf * 0.00000000000001f);

    }

    @Override
    public void simpleRender(RenderManager rm) {
        super.simpleRender(rm);
    }

    public static void main(String[] args) {
        final SimpleApplication app = new BatchedMovingObjectTestApplication();
        applySettings(app);
        app.start(false);
    }

    public static void applySettings(SimpleApplication app) {
        final AppSettings settings = new AppSettings(true);
        settings.setFullscreen(true);
        settings.setResolution(1680, 1050);
        app.setSettings(settings);
    }

}

package edu.ksu.rxecs.jme.core.components;

import edu.ksu.rxecs.core.ecs.Component;

public class PositionComponent extends Component {
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;
    public final float dx = (float) Math.random();
    public final float dy = (float) Math.random();
    public final float dz = (float) Math.random();

    @Override
    public String toString() {
        return "PositionComponent_this{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", dx=" + dx +
                ", dy=" + dy +
                ", dz=" + dz +
                '}';
    }
}

package edu.ksu.rxecs.jme.core.components;

import edu.ksu.rxecs.core.ecs.Component;

public class PositionComponent extends Component {
    public final float dx = 0.5f - (float) Math.random();
    public final float dy = 0.5f - (float) Math.random();
    public final float dz = 0.5f - (float) Math.random();
    public float x = 0.0f;
    public float y = 0.0f;
    public float z = 0.0f;

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

package edu.ksu.rxecs.jme.core.components;

import edu.ksu.rxecs.core.ecs.Component;

public class ScaleComponent extends Component {
    public float scale = 1.0f;
    public final float minScale = 0.6f;
    public final float maxScale = 1.4f;
    public final float delta = (float) Math.random() * 0.1f;
    public boolean invertDirection = false;
}

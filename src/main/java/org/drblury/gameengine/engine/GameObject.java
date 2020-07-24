package org.drblury.gameengine.engine;

import org.drblury.gameengine.engine.graph.Mesh;
import org.joml.Vector3f;

public class GameObject {
    private final Mesh mesh;
    private Vector3f position;
    private float scale;
    private Vector3f rotation;

    public Mesh getMesh() {
        return mesh;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        this.position = new Vector3f(0, 0, 0);
        this.scale = 1;
        this.rotation = new Vector3f(0, 0, 0);;
    }
}

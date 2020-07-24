package org.drblury.gameengine.engine;

import org.drblury.gameengine.engine.graph.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    private final Matrix4f projectionMatrix;

    private final Matrix4f viewMatrix;

    private final Matrix4f modelViewMatrix;

    public Transform() {
        modelViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
    }

    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRation = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRation, zNear, zFar);
        return projectionMatrix;
    }

    public Matrix4f getModelViewMatrix(GameObject gameObject, Matrix4f viewMatrix) {
        Vector3f rotation = gameObject.getRotation();
        modelViewMatrix.identity().translate(gameObject.getPosition())
                .rotateX((float) Math.toRadians(-rotation.x))
                .rotateY((float) Math.toRadians(-rotation.y))
                .rotateZ((float) Math.toRadians(-rotation.z))
                .scale(gameObject.getScale());

        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(modelViewMatrix);
    }

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.getPosition();
        Vector3f cameraRot = camera.getRotation();

        viewMatrix.identity();

        viewMatrix.rotate((float) Math.toRadians(cameraRot.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(cameraRot.y), new Vector3f(0, 1, 0));

        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }
}

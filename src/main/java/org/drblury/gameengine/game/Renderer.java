package org.drblury.gameengine.game;

import org.drblury.gameengine.engine.GameObject;
import org.drblury.gameengine.engine.Transform;
import org.drblury.gameengine.engine.graph.Mesh;
import org.drblury.gameengine.engine.Utils;
import org.drblury.gameengine.engine.Window;
import org.drblury.gameengine.engine.graph.ShaderProgram;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private ShaderProgram shaderProgram;

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private Matrix4f projectionMatrix;

    private Transform transform;


    public Renderer() {
        transform = new Transform();
    }

    public void init(Window window) throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();

        // Create projection matrix
        float aspectRatio = (float) window.getWidth() / window.getHeight();
        projectionMatrix = new Matrix4f().perspective(Renderer.FOV, aspectRatio, Renderer.Z_NEAR, Renderer.Z_FAR);
        shaderProgram.createUniform("projectionMatrix");

        shaderProgram.createUniform("worldMatrix");
    }

    public void render(Window window, GameObject[] gameObjects) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Render each GameObject
        for (GameObject gameObject: gameObjects) {
            // Set world matrix
            Matrix4f worldMatrix = transform.getWorldMatrix(gameObject.getPosition(), gameObject.getRotation(), gameObject.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);

            // Render the Mesh of this GameObject
            gameObject.getMesh().render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

}
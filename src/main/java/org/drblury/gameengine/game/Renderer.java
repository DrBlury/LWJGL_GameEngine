package org.drblury.gameengine.game;

import org.drblury.gameengine.engine.GameObject;
import org.drblury.gameengine.engine.Transform;
import org.drblury.gameengine.engine.Utils;
import org.drblury.gameengine.engine.Window;
import org.drblury.gameengine.engine.graph.Camera;
import org.drblury.gameengine.engine.graph.Mesh;
import org.drblury.gameengine.engine.graph.ShaderProgram;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transform transform;

    private ShaderProgram shaderProgram;

    public Renderer() {
        transform = new Transform();
    }

    public void init(Window window) throws Exception {
        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        // Create uniform for default colour and the flag that controls it
        shaderProgram.createUniform("colour");
        shaderProgram.createUniform("useColour");
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameObject[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transform.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transform.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
        for (GameObject gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transform.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            // Render the mesh for this game item
            shaderProgram.setUniform("colour", mesh.getColour());
            shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
            mesh.render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
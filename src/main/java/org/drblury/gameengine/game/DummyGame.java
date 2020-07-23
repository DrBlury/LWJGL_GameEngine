package org.drblury.gameengine.game;

import org.drblury.gameengine.engine.IGameLogic;
import org.drblury.gameengine.engine.Mesh;
import org.drblury.gameengine.engine.Window;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;


    private Mesh mesh;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
        float[] positions = new float[]{
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f
        };

        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2
        };

        float[] colours = new float[] {
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };
        mesh = new Mesh(positions, indices, colours);
    }

    @Override
    public void input(Window window) {
        if (window.isKeyPressed(GLFW_KEY_E)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }

}
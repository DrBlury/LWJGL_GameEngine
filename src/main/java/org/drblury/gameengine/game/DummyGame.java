package org.drblury.gameengine.game;

import org.drblury.gameengine.engine.GameObject;
import org.drblury.gameengine.engine.IGameLogic;
import org.drblury.gameengine.engine.graph.Mesh;
import org.drblury.gameengine.engine.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    private final Renderer renderer;

    private GameObject[] gameObjects;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        createGameObject();
    }

    @Override
    public void input(Window window) {
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_W)) {
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            displyInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displxInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            displxInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_E)) {
            displzInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval) {
        for (GameObject gameObject : gameObjects) {
            // Update position
            Vector3f itemPos = gameObject.getPosition();
            float posx = itemPos.x + displxInc * 0.01f;
            float posy = itemPos.y + displyInc * 0.01f;
            float posz = itemPos.z + displzInc * 0.01f;
            gameObject.setPosition(posx, posy, posz);

            // Update scale
            float scale = gameObject.getScale();
            scale += scaleInc * 0.05f;
            if ( scale < 0 ) {
                scale = 0;
            }
            gameObject.setScale(scale);

            // Update rotation angle
            float rotation = gameObject.getRotation().z + 1.5f;
            if ( rotation > 360 ) {
                rotation = 0;
            }
            gameObject.setRotation(0, 0, rotation);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gameObjects);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanUp();
        }
    }

    private void createGameObject() {
        float[] positions = new float[]{
                -0.5f,  0.5f, -1.05f,
                -0.5f, -0.5f, -1.05f,
                0.5f, -0.5f, -1.05f,
                0.5f,  0.5f, -1.05f,
                -0.6f,  0.0f, -1.05f,
                0.6f,  0.0f, -1.05f,
        };
        float[] colours = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.2f,
                0.0f, 0.8f, 0.5f,
                0.0f, 0.2f, 0.5f,
                0.0f, 0.2f, 0.5f,
                0.0f, 0.7f, 0.0f,
        };
        int[] indices = new int[]{
                0, 1, 3, // first triangle
                3, 1, 2, // second triangle
                0, 1, 4, // third triangle
                2, 3, 5, // fourth triangle
        };
        Mesh mesh = new Mesh(positions, colours, indices);
        GameObject gameObject = new GameObject(mesh);
        gameObject.setPosition(0, 0, -2);
        gameObjects = new GameObject[] {gameObject};
    }



}
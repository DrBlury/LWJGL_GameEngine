package org.drblury.gameengine.game;

import org.drblury.gameengine.engine.GameObject;
import org.drblury.gameengine.engine.IGameLogic;
import org.drblury.gameengine.engine.graph.Mesh;
import org.drblury.gameengine.engine.Window;
import org.drblury.gameengine.engine.graph.Texture;
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
            float rotationZ = gameObject.getRotation().z + 1.5f;
            if ( rotationZ > 360 ) {
                rotationZ = 0;
            }
            // Update rotation angle
            float rotationX = gameObject.getRotation().x + 1.5f;
            if ( rotationX > 360 ) {
                rotationX = 0;
            }
            gameObject.setRotation(rotationX, 0, rotationZ);
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

    private void createGameObject() throws Exception {
        float[] positions = new float[]{
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };
        float[] textureCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };

        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,
        };
        Texture texture = new Texture("/home/drblury/LWJGL_GameEngine/src/main/resources/textures/grassblock.png");
        Mesh mesh = new Mesh(positions, textureCoords, indices, texture);
        GameObject gameObject = new GameObject(mesh);
        gameObject.setPosition(0, 0, -2);
        gameObjects = new GameObject[] {gameObject};
    }



}
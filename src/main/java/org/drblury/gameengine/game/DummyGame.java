package org.drblury.gameengine.game;

import org.drblury.gameengine.engine.GameObject;
import org.drblury.gameengine.engine.IGameLogic;
import org.drblury.gameengine.engine.graph.Camera;
import org.drblury.gameengine.engine.graph.Mesh;
import org.drblury.gameengine.engine.Window;
import org.drblury.gameengine.engine.graph.OBJLoader;
import org.drblury.gameengine.engine.graph.Texture;
import org.drblury.gameengine.engine.input.MouseInput;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.5f;
    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;
    private GameObject[] gameObjects;
    private static final float CAMERA_POS_STEP = 0.05f;


    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        Texture texture = new Texture("/home/drblury/LWJGL_GameEngine/src/main/resources/grassblock.png");
        Mesh mesh = OBJLoader.loadMesh("/cube.obj");
        mesh.setTexture(texture);
        GameObject gameObject = new GameObject(mesh);
        gameObject.setPosition(0, 0, 0);
        gameObject.setScale(0.3f);

        gameObjects = new GameObject[]{gameObject};
    }

    private void generateSimpleTerrain(Mesh mesh) {
        List<GameObject> gameObjectsList = new ArrayList<>();
        float scaleSize = 1f;
        int xLength = 50;
        int yLength = 3;
        int zLength = 50;
        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                for (int z = 0; z < zLength; z++) {
                    GameObject gameObject = new GameObject(mesh);
                    gameObject.setScale(scaleSize);
                    gameObject.setPosition(x, y, z);
                    gameObjectsList.add(gameObject);
                }
            }
        }

        gameObjects = new GameObject[gameObjectsList.size()];
        gameObjects = gameObjectsList.toArray(gameObjects);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameObjects);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanUp();
        }
    }
}
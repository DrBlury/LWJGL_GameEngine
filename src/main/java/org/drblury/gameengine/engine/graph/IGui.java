package org.drblury.gameengine.engine.graph;

import org.drblury.gameengine.engine.GameObject;

public interface IGui {
    GameObject[] getGameObjects();

    default void cleanup() {
        GameObject[] gameObjects = getGameObjects();
        for (GameObject gameObject : gameObjects) {
            gameObject.getMesh().cleanUp();
        }
    }
}

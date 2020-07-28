package org.drblury.gameengine.engine.graph;

import org.drblury.gameengine.engine.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {
    private Map<Mesh, List<GameObject>> meshMap;

    private SkyBox skyBox;

    private SceneLight sceneLight;

    public Scene() {
        meshMap = new HashMap();
    }

    public Map<Mesh, List<GameObject>> getGameMeshes() {
        return meshMap;
    }

    public void setGameItems(GameObject[] gameObjects) {
        int numGameItems = gameObjects != null ? gameObjects.length : 0;
        for (int i=0; i<numGameItems; i++) {
            GameObject gameItem = gameObjects[i];
            Mesh mesh = gameItem.getMesh();
            List<GameObject> list = meshMap.get(mesh);
            if ( list == null ) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
    }

    public void cleanup() {
        for (Mesh mesh : meshMap.keySet()) {
            mesh.cleanUp();
        }
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }
}

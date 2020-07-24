package engine;

import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 10.0f;
        float sizeY = totalHeight / 10.0f;
        float padding = 5;

        for (int x=0; x < 10; x++) {
            for (int y=0; y < 10; y++) {
                float xPos = xOffset + (x * sizeX) + (padding * x);
                float yPos = yOffset + (y * sizeY) + (padding * y);

                GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 0.5f, 1)));
                this.addGameObjectToScene(go);
            }
        }
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS: " + (1.0f / dt) + " DT: " + dt);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.gameObjects.get(0).transform.position.x += 1 * dt;

        this.renderer.render();
    }
}

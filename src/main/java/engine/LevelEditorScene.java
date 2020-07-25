package engine;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(0, 0));


        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/astronaut.png"))));
        this.addGameObjectToScene(obj1);

        Spritesheet sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(1)));
        this.addGameObjectToScene(obj2);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        Spritesheet sprs = new Spritesheet(
                AssetPool.getTexture("assets/images/spritesheet.png"),
                16, 16, 26, 0);

        AssetPool.addSpritesheet("assets/images/spritesheet.png", sprs);
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

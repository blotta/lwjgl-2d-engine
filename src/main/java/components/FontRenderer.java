package components;

import engine.Component;

public class FontRenderer extends Component {

    public FontRenderer() {

    }

    @Override
    public void start() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Sprite Renderer");
        }
    }

    @Override
    public void update(float dt) {

    }
}

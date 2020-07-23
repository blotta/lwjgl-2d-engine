package engine;

import Util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private float[] vertexArray = {
            // position                // color                    // UV Coordinates
             100.0f,     0f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     1, 1, // bottom right - 0
                 0f, 100.0f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     0, 0, // top left     - 1
             100.0f, 100.0f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f,     1, 0, // top right    - 2
                 0f,     0f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f,     0, 1, // bottom left  - 3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    1      2


                    3      0
             */
            // 2 triangles to make a square
            2, 1, 0, // Top right triangle
            0, 1, 3 // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture;

    public LevelEditorScene() {
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-200, -300));

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/astronaut.png");
        // this.testTexture = new Texture("assets/images/GolfCart.png");

        // ===================================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ===================================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID); // "from this line on, we're referring to vaoID VAO"

        // Create a float buffer of vertices
        FloatBuffer RAMVertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        RAMVertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        // buffer values are still in RAM, now uploading to GPU
        glBufferData(GL_ARRAY_BUFFER, RAMVertexBuffer, GL_STATIC_DRAW); // not changing values, so static

        // Create the indicies buffer and upload to GPU
        IntBuffer RAMelementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        RAMelementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, RAMelementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        // (specify how the values are arranged in the vertex buffer, i.e posx, posy, posz, cr, cg, cb, ca)
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        // corresponds to location = 0 in the glsl shader (aPos)
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        // corresponds to location = 1 in the glsl shader (aColor)
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    @Override
    public void update(float dt) {
        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detatch();
    }
}

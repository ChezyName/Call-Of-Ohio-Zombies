package ObsidianEngine.ui;

import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import ObsidianEngine.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Image {
    private Shader shader;
    private Texture texture;
    public Image(Texture texture){
        this.shader = Shader.defaultUIShader;
        this.texture = texture;
    }
    public void Draw(){
        glEnable(GL_TEXTURE_2D);
        texture.bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 1); // top left
        glVertex2f(-1,-1);

        glTexCoord2f(0, 0); // bottom left
        glVertex2f(-1,1);

        glTexCoord2f(1, 0); // bottom right
        glVertex2f(1,1);

        glTexCoord2f(1, 1); // top right
        glVertex2f(1,-1);
        glEnd();
        texture.unbind();
    }
}

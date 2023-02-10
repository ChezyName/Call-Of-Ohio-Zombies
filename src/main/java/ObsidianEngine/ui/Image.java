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
    private Vector3f[] vertices;
    private int[] indices;
    private float[] UVs;
    private Shader shader;
    private Texture texture;

    private Vector3f Position,Rotation,Color;
    private float Scale;

    private int vao,pbo,ibo,uvbo;

    public Image(Vector2f start, Vector2f end,Texture texture){
        this.vertices = new Vector3f[]{
                new Vector3f(start.x,0,start.y),
                new Vector3f(start.x,0,end.y),
                new Vector3f(end.x,0,start.y),
                new Vector3f(end.x,0,end.y),
        };
        this.indices = new int[]{
                0,1,2,
                1,2,3
        };
        this.UVs = new float[]{
                0,0,

        };
        this.shader = Shader.defaultUIShader;
        this.Color = new Vector3f(255,255,255);
        this.Scale = 1;
        this.Rotation = new Vector3f();
        this.Position = new Vector3f();
        this.texture = texture;
        this.Create();
    }

    public void Create(){
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
        float[] positionData = new float[vertices.length * 3];
        for (int i = 0; i < vertices.length; i++) {
            positionData[i * 3] = vertices[i].x();
            positionData[i * 3 + 1] = vertices[i].y();
            positionData[i * 3 + 2] = vertices[i].z();
        }
        positionBuffer.put(positionData).flip();

        pbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, pbo);
        glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();
        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


        if(this.UVs != null){
            //Creating UV Buffer Objects
            uvbo = glGenBuffers();
            FloatBuffer UVBuffer = MemoryUtil.memAllocFloat(UVs.length);
            UVBuffer.put(UVs).flip();
            glBindBuffer(GL_ARRAY_BUFFER, uvbo);
            glBufferData(GL_ARRAY_BUFFER, UVBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        }
    }

    public void Draw(long WindowID){
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(WindowID,widthBuffer,heightBuffer);

        //Rendering
        /*
        shader.bind();
        shader.uploadColor(Color);

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //Textures
        if(texture != null){
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
            shader.uploadTexture(0);
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        shader.unbind();
        if(texture != null) texture.unbind();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        */

        glEnable(GL_TEXTURE_2D);
        texture.bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); // top left
        glVertex2f(-1,-1);

        glTexCoord2f(0, 1); // bottom left
        glVertex2f(-1,1);

        glTexCoord2f(1, 1); // bottom right
        glVertex2f(1,1);

        glTexCoord2f(1, 0); // top right
        glVertex2f(1,-1);
        glEnd();
        texture.unbind();
    }
}

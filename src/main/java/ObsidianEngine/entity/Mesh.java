package ObsidianEngine.entity;

import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private Vector3f[] vertices;
    private int[] indices;
    private Shader shader;

    private Vector3f Position,Rotation,Scale,Color;

    private int vao,pbo,ibo;

    public Mesh(Vector3f[] vertices, int[] indices, Shader shader, Vector3f Color){
        this.vertices = vertices;
        this.indices = indices;
        this.shader = shader;
        this.Color = Color;
        this.Create();
    }

    public void setColor(Vector3f color){
        this.Color = color;
    }

    public Vector3f[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVAO() {
        return vao;
    }

    public int getPBO() {
        return pbo;
    }

    public int getIBO() {
        return ibo;
    }

    public void Destroy(){
        glDeleteBuffers(pbo);
        glDeleteVertexArrays(vao);
    }

    public void Draw(Camera camera){
        shader.bind();
        shader.uploadMat4f("uProj",camera.getProjectionMatrix());
        shader.uploadMat4f("uView",camera.getViewMatrix());
        shader.uploadColor(Color);

        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        shader.unbind();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
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
    }
}

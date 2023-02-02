package ObsidianEngine.entity;

import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import ObsidianEngine.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MathUtil;
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
    private int[] UVs;
    private Shader shader;
    private Texture texture;

    private Vector3f Position,Rotation,Color;
    private float Scale;

    private int vao,pbo,ibo,uvbo;

    public Mesh(Vector3f[] vertices, int[] indices, Shader shader, Vector3f Color){
        this.vertices = vertices;
        this.indices = indices;
        this.shader = shader;
        this.Color = Color;
        this.Scale = 1;
        this.Rotation = new Vector3f();
        this.Position = new Vector3f();
        this.Create();
    }

    public Mesh(Vector3f[] vertices, int[] indices, Shader shader, Texture texture, int[] UV){
        this.vertices = vertices;
        this.indices = indices;
        this.shader = shader;
        this.Color = Color;
        this.Scale = 1;
        this.Rotation = new Vector3f();
        this.Position = new Vector3f();
        this.texture = texture;
        this.UVs = UV;
        this.Create();
    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public void setUVs( int[] UV){
        this.UVs = UV;
    }
    public void setShader(Shader shader){this.shader = shader;}

    public Vector3f getPosition(){
        return Position;
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

    public void Translate(float px, float py, float pz){
        Vector3f normalizedVector = new Vector3f(px,py,pz).normalize();
        this.Translate(normalizedVector);
    }

    public void Translate(Vector3f newPosition){
        newPosition.normalize();
        this.Position.x += newPosition.x;
        this.Position.y += newPosition.y;
        this.Position.z += newPosition.z;
    }

    public void Rotate(float rx,float ry,float rz){
        Vector3f rot = new Vector3f(rx,ry,rz);
        Rotate(rot);
    }

    public void setRotation(float rx, float ry, float rz){
        setRotation(new Vector3f(rx,ry,rz));
    }

    public void setRotation(Vector3f rotation){
        this.Rotation.x = rotation.x;
        this.Rotation.y = rotation.y;
        this.Rotation.z = rotation.z;
    }

    public void setScale(float ns){
        this.Scale = ns;
    }

    public void Rotate(Vector3f newRotation){
        this.Rotation.x += newRotation.x;
        this.Rotation.y += newRotation.y;
        this.Rotation.z += newRotation.z;
    }

    public void Draw(Camera camera){
        shader.bind();
        shader.uploadMat4f("uProj",camera.getProjectionMatrix());
        shader.uploadMat4f("uView",camera.getViewMatrix());
        shader.uploadColor(Color);

        //Textures
        if(texture != null){
            shader.uploadTexture(0);
            glActiveTexture(GL_TEXTURE0);
            texture.bind();
        }

        Matrix4f Transform = MathUtils.createTransformationMatrix(this.Position,this.Rotation.x,this.Rotation.y,this.Rotation.z,this.Scale);
        shader.uploadMat4f("uTransform",Transform);


        //position layout
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);

        //UNbinding

        shader.unbind();
        if(texture != null) texture.unbind();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
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


        if(this.UVs != null){
            IntBuffer UVBuffer = MemoryUtil.memAllocInt(UVs.length);
            UVBuffer.put(UVs).flip();
            uvbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, uvbo);
            glBufferData(GL_ARRAY_BUFFER, UVBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
    }
}

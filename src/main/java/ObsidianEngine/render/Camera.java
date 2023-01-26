package ObsidianEngine.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Vector;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    private Vector3f position;

    public Camera(Vector3f pos){
        this.position = pos;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public Camera(float x, float y, float z){
        this.position = new Vector3f(x,y,z);
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(-100.0f,100,-100,100,0.f,100.0f);
    }

    public void translate(float x, float y, float z){
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f,0.0f,-1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x,position.y,20.f),
                cameraFront.add(position.x,position.y,0),
                cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}

package ObsidianEngine.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Vector;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    private Vector3f position, orientation;

    private float yaw, pitch;

    public Camera(Vector3f pos){
        this.position = pos;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.orientation = new Vector3f(0,1,0);
        adjustProjection();
    }


    public Camera(float x, float y, float z){
        this.position = new Vector3f(x,y,z);
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.orientation = new Vector3f(0,1,0);
        adjustProjection();
    }

    public void setLookDir(float mouseX, float mouseY) {
        yaw = mouseX * 0.55f;
        pitch = -mouseY *  0.55f;
    }

    public void adjustProjection(){
        projectionMatrix.identity();
        float width = 25*16;
        float height = 25*9;
        float aspectRatio = (16.f/9.f);
        projectionMatrix.ortho(-width,width,-height,height,0.f,100.0f);
        projectionMatrix = new Matrix4f()
                .perspective((float) Math.toRadians(45.f), aspectRatio, 0.01f, 10000.0f)
                .lookAt(0.0f, 0.0f, 10.0f,
                        0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f);

    }

    public void translate(float x, float y, float z){
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public Matrix4f getViewMatrix(){
        Vector3f lookatpoint = new Vector3f(0,0,-1);

        //lookatpoint.rotateY((float)Math.toRadians(yaw),lookatpoint);
        //lookatpoint.rotateX((float)Math.toRadians(pitch),lookatpoint);

        lookatpoint.add(position,lookatpoint);

        Matrix4f matrix = new Matrix4f();
        matrix.lookAt(position,lookatpoint, orientation, matrix);
        return matrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}

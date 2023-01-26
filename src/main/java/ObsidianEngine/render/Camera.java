package ObsidianEngine.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f orientation;

    private float yaw = 0;
    private float pitch = 0;


    public Camera(float startX, float startY, float startZ){
        position = new Vector3f(startX,startY,startZ);
        orientation = new Vector3f(0,1,0);
    }

    public Camera(Vector3f startVector){
        position = startVector;
        orientation = new Vector3f(0,1,0);
    }

    public void translate(float x, float y, float z){
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void translate(Vector3f xyz){
        position.x += xyz.x;
        position.y += xyz.y;
        position.z += xyz.z;
    }

    public void setLookDir(float mouseX, float mouseY){
        yaw = mouseX;
        pitch = mouseY;
    }

    public Matrix4f getMatrix(){
        Vector3f lookatpoint = new Vector3f(0,0,-1);

        lookatpoint.rotateY((float)Math.toRadians(yaw),lookatpoint);
        lookatpoint.rotateX((float)Math.toRadians(pitch),lookatpoint);

        lookatpoint.add(position,lookatpoint);

        Matrix4f matrix = new Matrix4f();
        matrix.lookAt(position,lookatpoint, orientation, matrix);
        return matrix;
    }
}

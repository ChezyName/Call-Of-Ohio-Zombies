package ObsidianEngine.utils;

import ObsidianEngine.io.Input;
import ObsidianEngine.render.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;


public class MouseUtils {

    public static float getMouseRotFromCenter(long WindowID) {
        float mouseX = (float) Input.getMouseX();
        float mouseY = (float) Input.getMouseY();

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(WindowID,widthBuffer,heightBuffer);

        int width = widthBuffer.get(0);
        int height = heightBuffer.get(0);

        float x = (2.0f * mouseX) / width - 1.0f;
        float y = 1.0f - (2.0f * mouseY) / height;

        //Calculate Rotation Angle
        float angle = (float) Math.toDegrees(Math.atan2(y, x));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }
}
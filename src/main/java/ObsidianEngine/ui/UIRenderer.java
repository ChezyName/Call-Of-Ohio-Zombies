package ObsidianEngine.ui;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.*;

public class UIRenderer {
    public static void DrawProgressBar(long WindowID, float posX, float posY, float percentage, float Height, float Width, Vector3f Color){
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(WindowID,widthBuffer,heightBuffer);

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0.0, widthBuffer.get(0), heightBuffer.get(0), 0.0, -1.0, 10.0);
        glMatrixMode(GL_MODELVIEW);
        //glPushMatrix();        ----Not sure if I need this
        glLoadIdentity();
        glDisable(GL_CULL_FACE);

        glClear(GL_DEPTH_BUFFER_BIT);


        //DRAW 2D ITEM
        glBegin(GL_QUADS);
        glColor3f(Color.x / 255, Color.y / 255, Color.z / 255);
        glVertex2f(posX,posY);
        glVertex2f(posX,posY-Height);

        glVertex2f(posX + (Width * percentage),posY-Height);
        glVertex2f(posX + (Width * percentage),posY);
        glEnd();

        //Reset to white
        glColor3f(255,255,255);

        // Making sure we can render 3d again
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
    }
}
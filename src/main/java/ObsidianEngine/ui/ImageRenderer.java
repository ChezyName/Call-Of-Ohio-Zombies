package ObsidianEngine.ui;

import ObsidianEngine.render.Texture;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class ImageRenderer {

    static Texture Menu;
    static Texture DeathTexture;
    public static void RenderMenu(long WindowID){
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(WindowID,widthBuffer,heightBuffer);

        if(Menu == null) Menu = new Texture("/imgs/Grass.png");

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0.0, widthBuffer.get(0), heightBuffer.get(0), 0.0, -1.0, 10.0);
        glMatrixMode(GL_MODELVIEW);
        //glPushMatrix();        ----Not sure if I need this
        glLoadIdentity();
        glDisable(GL_CULL_FACE);

        glClear(GL_DEPTH_BUFFER_BIT);
        glEnable(GL_TEXTURE_2D);

        glBegin(GL_QUADS);
        Menu.bind();
        glColor4f(1,0,0,1);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);

        glTexCoord2f(1, 0);
        glVertex2f(512, 0);

        glTexCoord2f(1, 1);
        glVertex2f(512, 512);

        glTexCoord2f(0, 1);
        glVertex2f(0, 512);
        Menu.unbind();
        glEnd();
    }
}

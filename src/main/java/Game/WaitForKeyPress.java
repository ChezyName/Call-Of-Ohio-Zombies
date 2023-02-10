package Game;

import ObsidianEngine.entity.Box;
import ObsidianEngine.entity.Mesh;
import ObsidianEngine.entity.Plane;
import ObsidianEngine.io.Input;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import ObsidianEngine.ui.Image;
import ObsidianEngine.ui.UIRenderer;
import ObsidianEngine.utils.ColorUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.IntBuffer;

import static ObsidianEngine.ui.ImageRenderer.RenderMenu;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public class WaitForKeyPress {
    public static void WaitMainMenu(long WinID, Camera c,String title){
        boolean Wait = true;
        Image MenuImage = new Image(new Vector2f(0,0),new Vector2f(512,512),new Texture("/imgs/GameMenu.jpg"));

        while (Wait && !GLFW.glfwWindowShouldClose(WinID)){
            GLFW.glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();

            //Render Objects
            GLFW.glfwSetWindowTitle(WinID,title + " | press any key to start...");

            MenuImage.Draw(WinID);

            if(Input.isKeyDown()) Wait = false;


            glPopMatrix();
            GLFW.glfwSwapBuffers(WinID);
        }

        System.out.println("[MAINMENU] GAME HAS STARTED!");
    }
}

package Game;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.entity.Plane;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import ObsidianEngine.utils.ColorUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Map {
    private static void renderMap(ArrayList<Mesh> Meshes, long GLFWWindow, Camera cam){
        //Clear
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glPushMatrix();

        //Render Objects
        for(Mesh m : Meshes){
            m.Draw(cam);
        }

        glPopMatrix();
        GLFW.glfwSwapBuffers(GLFWWindow);
    }

    public static void getMap(float size,float incerment, ArrayList<Mesh> Meshes){
        Vector2f startPos = new Vector2f(-size/2,-size/2);
        while (startPos.y < size){
            while (startPos.x < size) {
                //Create Squares
                Mesh Ground = new Plane(incerment,incerment,new Vector3f(startPos.x,0,startPos.y), ColorUtils.Green);
                Ground.setTexture(new Texture("/imgs/Grass.jpg",false));
                Ground.setShader(Shader.defaultTextureShader);
                Ground.Create();
                Meshes.add(Ground);

                System.out.println("Map Space @ " + startPos.x + "," + startPos.y);

                startPos.x += incerment;
            }
            startPos.y += incerment;
            startPos.x = -size;
        }
    }
    public static void getMap(float size,float incerment, ArrayList<Mesh> Meshes,Camera cam,long WindowID){
        Vector2f startPos = new Vector2f(-size,-size);

        int maxPieces = (int) (incerment*4)*2;
        int currentPiece = 1;

        while (startPos.y < size){
            while (startPos.x < size) {
                //Create Squares
                Mesh Ground = new Plane(incerment,incerment,new Vector3f(startPos.x,0,startPos.y), ColorUtils.Green);
                Ground.setTexture(new Texture("/imgs/Grass.jpg",false));
                Ground.setShader(Shader.defaultTextureShader);
                Ground.Create();
                Meshes.add(Ground);

                System.out.println("[BUILDING MAP]" + currentPiece + "/" + maxPieces);
                currentPiece++;

                startPos.x += incerment;

                //Render Shapes
                renderMap(Meshes,WindowID,cam);
            }
            startPos.y += incerment;
            startPos.x = -size;
        }
    }

}

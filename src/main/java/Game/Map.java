package Game;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.entity.Plane;
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
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.opengl.GL11.*;

public class Map {
    static Image MenuImage = new Image(new Texture("/imgs/GameMenu.jpg"));
    private static void renderMap(ArrayList<Mesh> Meshes, long GLFWWindow, Camera cam,String title,int n,int max){
        //Clear
        GLFW.glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glPushMatrix();

        int percentage = (int)(((float) n/ (float) max)*100);
        GLFW.glfwSetWindowTitle(GLFWWindow,title + " | Loading Map: " + n + "/" + max + " [" + percentage + "%]");

        //Render Objects
        /*
        for(Mesh m : Meshes){
            m.Draw(cam);
        }
        */
        MenuImage.Draw();

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(GLFWWindow,widthBuffer,heightBuffer);

        //RenderUI
        float Half = widthBuffer.get(0)/2;
        UIRenderer.DrawProgressBar(GLFWWindow,(float)(Half - (Half*0.8)) ,(float) (heightBuffer.get(0)*0.9),1,(float) (heightBuffer.get(0)*0.05),(float) (Half*1.6),ColorUtils.Black);
        UIRenderer.DrawProgressBar(GLFWWindow,(float)(Half - (Half*0.8)) ,(float) (heightBuffer.get(0)*0.9),((float) n/ (float) max),(float) (heightBuffer.get(0)*0.05),(float) (Half*1.6),ColorUtils.Purple);

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

                //System.out.println("Map Space @ " + startPos.x + "," + startPos.y);

                startPos.x += incerment;
            }
            startPos.y += incerment;
            startPos.x = -size;
        }
    }

    static Texture Grass = new Texture("/imgs/Grass.jpg",false);
    static Texture Water = new Texture("/imgs/Water.jpg",false);

    public static void getMap(float size,float incerment, ArrayList<Mesh> Meshes,ArrayList<Mesh> WaterMeshes,Camera cam,long WindowID,String title){
        Vector2f startPos = new Vector2f(-size,-size);

        int maxPieces = (int) ((size/incerment) * (size/incerment))*4;
        int currentPiece = 1;

        while (startPos.y < size && !GLFW.glfwWindowShouldClose(WindowID)){
            while (startPos.x < size && !GLFW.glfwWindowShouldClose(WindowID)) {
                if(GLFW.glfwWindowShouldClose(WindowID)) break;

                //Create Squares
                Mesh Ground = new Plane(incerment,incerment,new Vector3f(startPos.x,0,startPos.y), ColorUtils.Green);

                if(startPos.y < -(size/2) || startPos.y > (size/2) || startPos.x < -(size/2) || startPos.x > (size/2)) Ground.setTexture(Water);
                else Ground.setTexture(Grass);
                Ground.setShader(Shader.defaultTextureShader);
                Ground.Create();

                if(startPos.y < -(size/2) || startPos.y > (size/2) || startPos.x < -(size/2) || startPos.x > (size/2)) WaterMeshes.add(Ground);
                else Meshes.add(Ground);

                //System.out.println("[BUILDING MAP]" + currentPiece + "/" + maxPieces);
                currentPiece++;

                startPos.x += incerment;

                //Render Shapes
                renderMap(Meshes,WindowID,cam,title,currentPiece,maxPieces);
            }
            startPos.y += incerment;
            startPos.x = -size;
        }

        System.out.println("[MAP LOADED] " + currentPiece + " Map Slots.");
    }

    public static void DrawMapPieces(ArrayList<Mesh> Water,ArrayList<Mesh> Ground,Camera cam){

    }
}

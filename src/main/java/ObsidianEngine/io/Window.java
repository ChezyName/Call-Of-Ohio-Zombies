package ObsidianEngine.io;

import ObsidianEngine.entity.Box;
import ObsidianEngine.entity.Mesh;
import ObsidianEngine.entity.Plane;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import static  org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

public class Window {
    private int width, height;
    private String title;
    private long GLFWWindow;
    private static long time;
    private Input inputsystem;

    private static Window window = null;
    private Camera cam;

    private final ArrayList<Mesh> Entities = new ArrayList<Mesh>();

    private Window(){
        this.width = 1280;
        this.height = 720;
        this.title = "WindowName";
    }

    public static Window get() {
        if(Window.window == null) Window.window = new Window();
        return Window.window;
    }

    public void run(){
        System.out.println("Running LWJGL " + Version.getVersion());

        init();
        loop();
    }

    private void init(){
        //Core Systems
        time = System.currentTimeMillis();
        inputsystem = new Input();
        cam = new Camera(0,0,100);

        //Debug Logs
        GLFWErrorCallback.createPrint(System.err).set();
        if(!GLFW.glfwInit()) throw new IllegalStateException("Unable to init GLFW");

        //Configure
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE,GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED,GLFW.GLFW_TRUE);
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        // OpenGL Window Hints
        //glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
        //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT,GLFW_TRUE);

        //Create Window
        GLFWWindow = GLFW.glfwCreateWindow(this.width,this.height,this.title, MemoryUtil.NULL,MemoryUtil.NULL);

        if(GLFWWindow == MemoryUtil.NULL){
            throw new IllegalStateException("Failed to create GLFW Window");
        }

        //Create OPENGL Renderer
        GLFW.glfwMakeContextCurrent(GLFWWindow);
        GLFW.glfwSwapInterval(1);

        //Display Window
        GLFW.glfwShowWindow(GLFWWindow);

        //LWJGL -> Binding To OPENGL & GLFW
        GL.createCapabilities();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_DEPTH_TEST);

        //Aspect Ratio 16:9
        glfwSetWindowAspectRatio(GLFWWindow,16,9);

        //Post OpenGL Init
        //Init Default Shader
        Shader.defaultShader = new Shader("/shaders/mainVertex.glsl","/shaders/mainFragment.glsl");
        Shader.defaultShader.create();

        //Create Ground Plane
        Mesh plane = new Plane(10000,10000,new Vector3f(0,0,0));
        plane.Create();
        Entities.add(plane);

        //Create Mesh
        Mesh m = new Box(30,30,30,new Vector3f(0,15,0),new Vector3f(255,255,0));
        m.Create();
        Entities.add(m);

        m = new Box(30,30,30,new Vector3f(18,25,15f),new Vector3f(255,125f,0));
        m.Create();
        Entities.add(m);
    }

    private void FPSCalc(){
        float TimeBetween = System.currentTimeMillis() - time;

        int FPS = (int)(1000/TimeBetween);
        GLFW.glfwSetWindowTitle(GLFWWindow,title + " | FPS: " + FPS);

        time = System.currentTimeMillis();
    }

    private void RenderMeshes(){
        for(Mesh m : Entities){
            m.Draw(cam);
        }
    }

    private void loop() {
        glClearColor(0.f, 0.0f, 0.0f, 1.f);
        //Locks Mouse To Screen / Hids Mosue
        glfwSetInputMode(GLFWWindow, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

        while (!GLFW.glfwWindowShouldClose(GLFWWindow)) {
            //Get Events
            GLFW.glfwPollEvents();

            if(glfwGetKey(GLFWWindow,GLFW_KEY_W) == GLFW_TRUE) cam.translate(0,0,-1);
            if(glfwGetKey(GLFWWindow,GLFW_KEY_S) == GLFW_TRUE) cam.translate(0,0,1);
            if(glfwGetKey(GLFWWindow,GLFW_KEY_A) == GLFW_TRUE) cam.translate(-1,0,0);
            if(glfwGetKey(GLFWWindow,GLFW_KEY_D) == GLFW_TRUE) cam.translate(1,0,0);
            if(glfwGetKey(GLFWWindow,GLFW_KEY_E) == GLFW_TRUE) cam.translate(0,1,0);
            if(glfwGetKey(GLFWWindow,GLFW_KEY_Q) == GLFW_TRUE) cam.translate(0,-1,0);

            //Mouse Positions
            DoubleBuffer mouseX = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer mouseY = BufferUtils.createDoubleBuffer(1);
            int newMouseX = -1;
            int newMouseY = -1;

            glfwGetCursorPos(GLFWWindow, mouseX, mouseY);
            glfwSetInputMode(GLFWWindow, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

            mouseX.rewind();
            mouseY.rewind();
            newMouseX = (int) mouseX.get(0);
            newMouseY = (int) mouseY.get(0);

            cam.setLookDir((float) newMouseX,(float) newMouseY);

            //Clear
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();

            //Render Objects
            RenderMeshes();

            glPopMatrix();
            GLFW.glfwSwapBuffers(GLFWWindow);

            //FPS
            FPSCalc();
        }

        //Cleanup
        inputsystem.destroy();
        GLFW.glfwDestroyWindow(GLFWWindow);
    }
}

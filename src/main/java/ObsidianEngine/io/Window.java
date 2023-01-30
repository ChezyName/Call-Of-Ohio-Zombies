package ObsidianEngine.io;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.entity.Plane;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import ObsidianEngine.utils.ColorUtils;
import ObsidianEngine.utils.FileUtils;
import ObsidianEngine.utils.TimeUtils;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import static  org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

public class Window {
    private int width, height;
    private String title;
    private long GLFWWindow;
    private TimeUtils time;
    private Input inputsystem;

    private static Window window = null;
    private Camera cam;
    private Mesh Player;

    private ArrayList<Mesh> Meshes = new ArrayList<Mesh>();
    //CALLBACKS

    private Window(){
        this.width = 1280;
        this.height = 720;
        this.title = "GAME NAME";
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
        time = TimeUtils.getTimeUtils();
        cam = new Camera(0,20f,20f);

        //Debug Logs
        GLFWErrorCallback.createPrint(System.err).set();
        if(!GLFW.glfwInit()) throw new IllegalStateException("Unable to init GLFW");

        //Configure
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE,GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW_FALSE);
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

        //Post Window Init CoreSystems
        inputsystem = new Input(GLFWWindow);
        //Post OpenGL Init
        //Init Default Shader
        Shader.defaultShader = new Shader("/shaders/mainVertex.glsl","/shaders/mainFragment.glsl");
        Shader.defaultShader.create();

        //Starting Meshes & Models
        //Ground
        Mesh Ground = new Plane(500,500,new Vector3f(0,0,0), ColorUtils.Green);
        Meshes.add(Ground);

        Player = FileUtils.LoadOBJ("/models/Link.obj", ColorUtils.Black, Meshes);
        Player.setScale(5);
    }

    private void drawAllMeshes(){
        for(Mesh m : Meshes){
            m.Draw(cam);
        }
    }

    private void loop() {
        glClearColor(0.f, 1.f, 1.f, 1.f);

        while (!GLFW.glfwWindowShouldClose(GLFWWindow)) {
            //Get Events
            GLFW.glfwPollEvents();

            GLFW.glfwSetWindowTitle(GLFWWindow,title + " | FPS: " + time.getFPS());

            //Controls
            float deltaTime = time.getDelta();
            if(Input.getKeyDown(GLFW_KEY_W)) Player.Translate(0,0,-(1.f)*deltaTime);
            if(Input.getKeyDown(GLFW_KEY_S)) Player.Translate(0,0,(1.f)*deltaTime);
            if(Input.getKeyDown(GLFW_KEY_A)) Player.Translate(-(1.f)*deltaTime,0,0);
            if(Input.getKeyDown(GLFW_KEY_D)) Player.Translate((1.f)*deltaTime,0,0);
            Player.Rotate(0,500*deltaTime,0);

            //Clear
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();

            //Render Objects
            drawAllMeshes();

            glPopMatrix();
            GLFW.glfwSwapBuffers(GLFWWindow);
        }

        //Cleanup
        inputsystem.destroy(GLFWWindow);
        GLFW.glfwDestroyWindow(GLFWWindow);
    }
}

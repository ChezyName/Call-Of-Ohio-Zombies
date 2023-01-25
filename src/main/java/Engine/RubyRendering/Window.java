package Engine.RubyRendering;

import Engine.MoonstoneMaths.Vector3f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import static  org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryUtil;

public class Window {
    private int width, height;
    private String title;
    private long GLFWWindow;
    private static long time;

    private static Window window = null;

    private Mesh m;

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
        //Set FPS
        time = System.currentTimeMillis();

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
        glOrtho(0,1, 0, 1, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_DEPTH_TEST);

        //Create Mesh
        m = new Mesh(new Vertex[] {
                new Vertex(new Vector3f(-0.5f,0.5f,0.0f)),
                new Vertex(new Vector3f(0.5f,0.5f,0.0f)),
                new Vertex(new Vector3f(-0.5f,-0.5f,0.0f)),
                new Vertex(new Vector3f(0.5f,-0.5f,0.0f))
        }, new int[]{
                0, 1, 2,
                0, 3, 2,
        });

        m.Create();
    }

    private void FPSCalc(){
        float TimeBetween = System.currentTimeMillis() - time;

        int FPS = (int)(1000/TimeBetween);
        GLFW.glfwSetWindowTitle(GLFWWindow,title + " | FPS: " + FPS);

        time = System.currentTimeMillis();
    }

    private void loop() {
        glClearColor(0.f, 1.f, 1.f, 1.f);

        while (!GLFW.glfwWindowShouldClose(GLFWWindow)) {
            //Get Events
            GLFW.glfwPollEvents();

            //Clear
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();

            //Render Objects
            m.Draw();

            glPopMatrix();
            GLFW.glfwSwapBuffers(GLFWWindow);

            //FPS
            FPSCalc();
        }
        GLFW.glfwDestroyWindow(GLFWWindow);
    }
}

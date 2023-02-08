package ObsidianEngine.io;

import Game.Map;
import Game.Player;
import Game.Zombie;
import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import ObsidianEngine.ui.UIRenderer;
import ObsidianEngine.utils.ColorUtils;
import ObsidianEngine.utils.FileUtils;
import ObsidianEngine.utils.MouseUtils;
import ObsidianEngine.utils.TimeUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import static  org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Window {
    private int width, height;
    private String title,versionNumber;
    private static long FPStime;
    private long GLFWWindow;
    private TimeUtils time;
    private Input inputSystem;

    private static Window window = null;
    private Camera cam;
    private Game.Player Player;

    private ArrayList<Mesh> MapPieces = new ArrayList<Mesh>();
    private ArrayList<Zombie> Zombies = new ArrayList<Zombie>();
    //CALLBACKS

    private Window(){
        this.width = 1280;
        this.height = 720;
        this.title = "Chezy's Game";
        this.versionNumber = "v0.1";
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

    private void FPSCalc() {
        float TimeBetween = System.currentTimeMillis() - FPStime;

        int FPS = (int) (1000 / TimeBetween);
        GLFW.glfwSetWindowTitle(GLFWWindow, title + " | FPS: " + FPS);
        FPStime = System.currentTimeMillis();
    }

    private void init(){
        //Core Systems
        time = TimeUtils.getTimeUtils();
        FPStime = System.currentTimeMillis();
        cam = new Camera(-30,400f,0f);


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
        glEnable(GL_TEXTURE_2D);

        //Aspect Ratio 16:9
        glfwSetWindowAspectRatio(GLFWWindow,16,9);

        //Post Window Init CoreSystems
        inputSystem = new Input(GLFWWindow);

        //Post OpenGL Init
        //Init Default Shader
        Shader.defaultShader = new Shader("/shaders/mainVertex.glsl","/shaders/mainFragment.glsl");
        Shader.defaultTextureShader  = new Shader("/shaders/TextureVertex.glsl","/shaders/TextureFragment.glsl");

        //Set Cursor Image
        MouseUtils.SetCursorImage(GLFWWindow,"/imgs/Crosshair.png");

        //Starting Meshes & Models
        //Load Map [MULTI-SPRTIE]
        Map.getMap(550,50,MapPieces,cam,GLFWWindow,(title + " " + versionNumber));

        /* SINGLE COLOR MAP
        Mesh map = new Plane(15000,15000,new Vector3f(0,0,0));
        map.setShader(Shader.defaultTextureShader);
        map.setTexture(new Texture("/imgs/Grass.jpg",false));
        MapPieces.add(map);
         */

        Player = new Player(FileUtils.LoadOBJWTextureSingle("/models/Link.obj", new Texture("/imgs/PlayerTexture.png")));

        Zombies.add(new Zombie(Player.getPosition(),350));
        Zombies.add(new Zombie(Player.getPosition(),350));
        Zombies.add(new Zombie(Player.getPosition(),350));
        Zombies.add(new Zombie(Player.getPosition(),350));
        Zombies.add(new Zombie(Player.getPosition(),350));
    }

    private long LastTimeZombiesSpawn = System.nanoTime();
    private void Reset(){
        for(int i = 0; i < Zombies.size(); i++){
            Zombie z = Zombies.get(i);
            z.Destroy();
            Zombies.remove(i);
        }
        Player.resetHealth();
        LastTimeZombiesSpawn = System.nanoTime();
        //Player.setPosition(0,0,0);
    }


    private void updateZombies(float Delta){
        for(Zombie z : Zombies){
            z.update(Player,cam,Delta);
        }

        //To Seconds
        long TimeB = (System.nanoTime() - LastTimeZombiesSpawn);
        long TimeF = TimeUnit.SECONDS.convert(TimeB,TimeUnit.NANOSECONDS);

        System.out.println(TimeF);
    }

    private void drawAllMeshes(float Delta){
        Player.Draw(cam);

        for(Mesh m : MapPieces){
            m.Draw(cam);
        }

        updateZombies(Delta);
    }

    long lastTime = System.nanoTime();
    private float getDelta(){
        long currentTime = System.nanoTime();
        int delta = (int)(currentTime - lastTime);
        lastTime = System.nanoTime();
        return delta;
    }

    private void RenderUI(){
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(GLFWWindow,widthBuffer,heightBuffer);

        //Render HEALTH UI
        float Half = widthBuffer.get(0)/2;
        UIRenderer.DrawProgressBar(GLFWWindow,(float)(Half - (Half*0.8)) ,(float) (heightBuffer.get(0)*0.9),Player.getHealthPercent(),(float) (heightBuffer.get(0)*0.05),(float) (Half*1.6), ColorUtils.Red);

    }

    private void loop() {
        glClearColor(0,0,0,1);
        LastTimeZombiesSpawn = System.nanoTime();

        while (!GLFW.glfwWindowShouldClose(GLFWWindow)) {
            if(Player.getHealth() <= 0) Reset();

            //Get Events
            GLFW.glfwPollEvents();
            //Controls
            float deltaTime = getDelta();
            float pDelta = (deltaTime/500);

            if(Input.getKeyDown(GLFW_KEY_W)) Player.setPosition(0,0,-(0.05f)*pDelta);
            if(Input.getKeyDown(GLFW_KEY_S)) Player.setPosition(0,0,(0.05f)*pDelta);
            if(Input.getKeyDown(GLFW_KEY_A)) Player.setPosition(-(0.05f)*pDelta,0,0);
            if(Input.getKeyDown(GLFW_KEY_D)) Player.setPosition((0.05f)*pDelta,0,0);

            Player.setRotation(0,MouseUtils.getMouseRotFromCenter(GLFWWindow),0);

            //Set Camera position to player position
            cam.setPosition(Player.getPosition().x,400,Player.getPosition().z);

            //Clear
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();

            //Render Objects
            drawAllMeshes(pDelta);

            RenderUI();


            glPopMatrix();
            GLFW.glfwSwapBuffers(GLFWWindow);

            FPSCalc();
        }

        //Cleanup
        inputSystem.destroy(GLFWWindow);
        GLFW.glfwDestroyWindow(GLFWWindow);
    }
}

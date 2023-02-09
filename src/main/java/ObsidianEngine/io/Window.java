package ObsidianEngine.io;

import Game.Bullet;
import Game.Map;
import Game.Player;
import Game.Zombie;
import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import ObsidianEngine.ui.UIRenderer;
import ObsidianEngine.utils.*;
import org.joml.Vector3f;
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
import java.util.Random;

public class Window {
    private int width, height;
    private String title,versionNumber;
    private static long FPStime;
    private long GLFWWindow;
    private TimeUtils time;
    private Input inputSystem;
    private Sound mainMusic;
    private ArrayList<Sound> GunShots = new ArrayList<Sound>();
    private int GunIndex = 0;

    private static Window window = null;
    private Camera cam;
    private Game.Player Player;

    private ArrayList<Mesh> MapPieces = new ArrayList<Mesh>();
    private ArrayList<Zombie> Zombies = new ArrayList<Zombie>();
    private ArrayList<Bullet> Bullets = new ArrayList<Bullet>();
    private long LastTimeShot = System.currentTimeMillis();

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
        GLFW.glfwSetWindowTitle(GLFWWindow, title + " | FPS: " + FPS + " [WAVE #" + Wave + "]");
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

        //INIT AUDIO LIBRARY
        Audio.getAudioManager();

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

        //Create Audio Context + Load Audio File
        Audio.getAudioManager();
        mainMusic = new Sound(FileUtils.getJarLoc() + "/sounds/MainTheme.ogg",true);
        mainMusic.play();

        //Gunshots SFX
        GunShots.add(new Sound(FileUtils.getJarLoc() + "/sounds/GunShotSFX_5.ogg",false));
        GunShots.add(new Sound(FileUtils.getJarLoc() + "/sounds/GunShotSFX_5.ogg",false));
        GunShots.add(new Sound(FileUtils.getJarLoc() + "/sounds/GunShotSFX_5.ogg",false));
        GunShots.add(new Sound(FileUtils.getJarLoc() + "/sounds/GunShotSFX_5.ogg",false));
        GunShots.add(new Sound(FileUtils.getJarLoc() + "/sounds/GunShotSFX_5.ogg",false));

        //Post OpenGL Init
        //Init Default Shader
        Shader.defaultShader = new Shader("/shaders/mainVertex.glsl","/shaders/mainFragment.glsl");
        Shader.defaultTextureShader  = new Shader("/shaders/TextureVertex.glsl","/shaders/TextureFragment.glsl");

        //Set Cursor Image
        MouseUtils.SetCursorImage(GLFWWindow,"/imgs/Crosshair.png");

        //Starting Meshes & Models
        //Load Map [MULTI-SPRTIE]
        Map.getMap(800,50,MapPieces,cam,GLFWWindow,(title + " " + versionNumber));

        /* SINGLE COLOR MAP
        Mesh map = new Plane(15000,15000,new Vector3f(0,0,0));
        map.setShader(Shader.defaultTextureShader);
        map.setTexture(new Texture("/imgs/Grass.jpg",false));
        MapPieces.add(map);
         */

        Player = new Player(FileUtils.LoadPlayer());
    }

    private int Wave = 0;
    private boolean reset = false;
    private void Reset(){
        reset = true;
        for(int i = 0; i < Zombies.size(); i++){
            Zombie z = Zombies.get(i);
            z.Destroy();
            Zombies.remove(i);
        }

        for(int i = 0; i < Bullets.size(); i++){
            Bullet z = Bullets.get(i);
            z.Destroy();
            Bullets.remove(i);
        }

        Player.resetHealth();
        Wave = 0;
        //Player.setPosition(0,0,0);

        for(int i = 0; i < Zombies.size(); i++){
            Zombie z = Zombies.get(i);
            z.Destroy();
            Zombies.remove(i);
        }

        reset = false;
    }

    private void updateBullets(float Delta){
        for(int i = 0; i < Bullets.size(); i++){
            Bullet b = Bullets.get(i);
            b.Update(cam,Delta,Zombies);
            if(b.Dead()) Bullets.remove(i);
        }
    }

    private void updateZombies(float Delta){
        for(int i = 0; i < Zombies.size(); i++){
            Zombie z = Zombies.get(i);
            if(z.isDead()){
                z.Destroy();
                Zombies.remove(i);
            }
            else{
                z.update(Player,cam,Delta);
            }
        }

        //To Seconds
        if(Zombies.size() <= 0){
            //All Zombies Dead / New Wave
            Wave += 1;
            for(int i = 0; i < Math.pow(Wave,2); i++){
                Zombies.add(new Zombie(new Vector3f(-30,0,0),350 * ((Wave/2) <= 1 ? 1 : Wave),Wave));
            }
        }
    }

    private void drawAllMeshes(float Delta){
        Player.Draw(cam);

        for(Mesh m : MapPieces){
            m.Draw(cam);
        }

        updateBullets(Delta);
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
        UIRenderer.DrawProgressBar(GLFWWindow,(float)(Half - (Half*0.8)) ,(float) (heightBuffer.get(0)*0.9),1,(float) (heightBuffer.get(0)*0.05),(float) (Half*1.6), ColorUtils.White);
        UIRenderer.DrawProgressBar(GLFWWindow,(float)(Half - (Half*0.8)) ,(float) (heightBuffer.get(0)*0.9),Player.getHealthPercent(),(float) (heightBuffer.get(0)*0.05),(float) (Half*1.6), ColorUtils.Red);
    }

    private void loop() {
        glClearColor(0,0,0,1);

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

            float BulletDelay = (System.currentTimeMillis() - LastTimeShot);

            if(Input.isMouseButtonDown(0) && BulletDelay >= (400/(Wave <= 0 ? 1 : Wave))) {
                Bullets.add(new Bullet(new Vector3f(cam.getPosition().x,0,cam.getPosition().z),Player.getRotation()));
                LastTimeShot = System.currentTimeMillis();

                //Play Random Sound From Array
                /*
                Random rand = new Random();
                int randomIndex = rand.nextInt(GunShots.size());
                GunShots.get(randomIndex).play();
                 */

                //loop thru gun shots
                GunShots.get(GunIndex).play();
                GunIndex++;
                if(GunIndex >= GunShots.size()) GunIndex = 0;
            }

            Player.setRotation(0,MouseUtils.getMouseRotFromCenter(GLFWWindow),0);

            //Set Camera position to player position
            cam.setPosition(Player.getPosition().x,400,Player.getPosition().z);

            //Clear
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();

            //Render Objects
            if(!reset) drawAllMeshes(pDelta);

            RenderUI();

            glPopMatrix();
            GLFW.glfwSwapBuffers(GLFWWindow);

            FPSCalc();
        }

        //Cleanup
        inputSystem.destroy(GLFWWindow);
        Audio.getAudioManager().Destroy();
        GLFW.glfwDestroyWindow(GLFWWindow);
    }
}

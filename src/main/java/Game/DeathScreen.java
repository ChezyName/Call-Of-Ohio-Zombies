package Game;

import ObsidianEngine.io.Input;
import ObsidianEngine.render.Texture;
import ObsidianEngine.ui.Image;
import ObsidianEngine.utils.FileUtils;
import ObsidianEngine.utils.Sound;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class DeathScreen {
    static Image DeathImage = new Image(new Texture("/imgs/DeathImage.jpg"));
    static Sound DeathAudioF;
    static Sound DeathAudioL;
    public static void DeathScreen(long WinID, String title, ArrayList<Zombie> Zombies){
        if(DeathAudioF == null) DeathAudioF = new Sound(FileUtils.getJarLoc() + "/sounds/DeathF.ogg",false);
        if(DeathAudioL == null) DeathAudioL = new Sound(FileUtils.getJarLoc() + "/sounds/DeathL.ogg",true);
        long DeathTime = System.currentTimeMillis();

        boolean Wait = true;
        DeathAudioL.play();

        //Clear Zombies
        for(int i = 0; i < Zombies.size(); i++){
            Zombie z = Zombies.get(i);
            z.Destroy();
            Zombies.remove(i);
        }

        while (Wait && !GLFW.glfwWindowShouldClose(WinID)){
            GLFW.glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glPushMatrix();

            //Render Objects
            double timeLeft = (System.currentTimeMillis() - DeathTime);
            int T = (int) (timeLeft / 1000);
            T -= 5;

            GLFW.glfwSetWindowTitle(WinID,title + (T < 0 ? "| " + Math.abs(T) + " seconds remaining..." : "| Press any key to restart..."));
            //if(!DeathAudioF.isPlaying()) DeathAudioL.play();

            DeathImage.Draw();

            if(Input.isKeyDown() && timeLeft >= 5250) Wait = false;

            glPopMatrix();
            GLFW.glfwSwapBuffers(WinID);
        }

        //MAKE SURE THERE ARE NO ZOMBIES!
        for(int i = 0; i < Zombies.size(); i++){
            Zombie z = Zombies.get(i);
            z.Destroy();
            Zombies.remove(i);
        }

        DeathAudioF.stop();
        DeathAudioL.stop();
    }
}

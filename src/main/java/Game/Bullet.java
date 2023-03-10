package Game;

import ObsidianEngine.entity.Box;
import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Camera;
import ObsidianEngine.utils.ColorUtils;
import ObsidianEngine.utils.FileUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Bullet {
    private Mesh m;
    private long LifeTime;
    private boolean ShotDude = false;

    public Bullet(Vector3f startPos,float PlayerRotation){
        m = FileUtils.LoadBullet();
        startPos.y = 40;
        m.setPosition(startPos);
        m.setScale(50);
        m.setRotation(0,PlayerRotation,0);
        LifeTime = System.currentTimeMillis();
    }

    public void Update(Camera camera, float Delta, ArrayList<Zombie> Zombies){
        long T = (long) (System.currentTimeMillis() - LifeTime);
        //Stop Displaying After 10s
        if(T <= 10_000){
            //Move Bullet
            double angle = m.getRotation().y;
            double radians = Math.toRadians(angle);
            double x = Math.cos(radians);
            double y = Math.sin(radians);
            Vector2f forward = new Vector2f((float) x,(float) y);

            m.Translate(forward.x,0,-forward.y,(Delta/800) * 0.8f);

            //Check if the Zombie is near the bullet
            for(Zombie z : Zombies){
                //If zombie is 35units away from this
                if(z.getPos().distance(m.getPosition()) <= 60){
                    z.Died();
                    ShotDude = true;
                }
            }

            //Draw the bullet
            m.Draw(camera);
        }
    }

    public boolean Dead(){
        long T = (long) (System.currentTimeMillis() - LifeTime);
        return T >= 10_000 || ShotDude;
    }

    public void Destroy(){
        m.Destroy();
    }
}

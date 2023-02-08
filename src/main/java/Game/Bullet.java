package Game;

import ObsidianEngine.entity.Box;
import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Camera;
import ObsidianEngine.utils.ColorUtils;
import ObsidianEngine.utils.FileUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Bullet {
    private Mesh m;
    private long LifeTime;

    public Bullet(Vector3f startPos,float PlayerRotation){
        m = FileUtils.LoadOBJSingle("/models/Bullet.obj",ColorUtils.Red);
        m.setPosition(startPos);
        m.setScale(50);
        m.setRotation(0,PlayerRotation,0);
        LifeTime = System.currentTimeMillis();
    }

    public void Update(Camera camera,float Delta){
        long T = (long) (System.currentTimeMillis() - LifeTime);
        //Stop Displaying After 10s
        if(T <= 10_000){
            //Move Bullet
            double angle = m.getRotation().y;
            double radians = Math.toRadians(angle);
            double x = Math.cos(radians);
            double y = Math.sin(radians);
            Vector2f forward = new Vector2f((float) x,(float) y);
            forward.x *= Delta/500 * 0.05;
            forward.y *= Delta/500 * 0.05;

            //m.Translate(forward.x,0,forward.y);

            //Draw the bullet
            m.Draw(camera);
        }
    }

    public boolean Dead(){
        long T = (long) (System.currentTimeMillis() - LifeTime);
        return T >= 10_000;
    }

    public void Destroy(){
        m.Destroy();
    }
}

package Game;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Texture;
import ObsidianEngine.utils.FileUtils;
import ObsidianEngine.utils.MathUtils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Zombie {
    private Mesh mesh;

    public Zombie(Vector3f StartPosiition, float Radius){
        Mesh Zombie = FileUtils.LoadOBJWTextureSingle("/models/Zombie.obj", new Texture("/imgs/ZombieTexture.png"));
        Zombie.setScale(50);
        this.mesh = Zombie;

        float X = (StartPosiition.x) + (MathUtils.getRandomNumber() * (Radius/2));
        float Y = (StartPosiition.y) + (MathUtils.getRandomNumber() * (Radius/2));
        Zombie.setPosition(X,0,Y);
    }


    public void update(Player p, Camera cam,float Delta){
        Vector3f goToPos = p.getPosition();
        mesh.lookAt(goToPos);
        if(!mesh.CloseEnough(goToPos)) mesh.MoveToAngle(goToPos,0.005f,Delta/200);
        else{
            //Too Close Deal Damage
            p.takeDamage(1f);
        }
        mesh.Draw(cam);
    }

    public void Destroy(){
     mesh.Destroy();
    }
}

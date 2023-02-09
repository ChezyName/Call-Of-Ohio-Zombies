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
    private boolean Dead;
    private int WaveSpawnedOn = 0;

    public Zombie(Vector3f StartPosiition, float Radius, int Wave){
        Mesh Zombie = FileUtils.LoadZombie();
        Zombie.setScale(50);
        this.mesh = Zombie;
        this.WaveSpawnedOn = Wave;

        System.out.println("Spawning Zombies @ " + StartPosiition);

        float X = (StartPosiition.x) + (MathUtils.getRandomPosNegHalf() * (Radius));
        float Y = (StartPosiition.y) + (MathUtils.getRandomPosNegHalf() * (Radius));
        Zombie.setPosition(X,0,Y);
        Dead = false;
    }

    public Vector3f getPos(){
        return mesh.getPosition();
    }


    public void update(Player p, Camera cam,float Delta){
        if(!isDead()){
            Vector3f goToPos = p.getPosition();
            mesh.lookAt(goToPos);
            if(!mesh.CloseEnough(goToPos)) mesh.MoveToAngle(goToPos,(WaveSpawnedOn * 0.5f) * 0.005f,Delta/200);
            else{
                //Too Close Deal Damage
                p.takeDamage(1f);
            }
        }
        mesh.Draw(cam);
    }

    public void Died(){
        Dead = true;
    }

    public boolean isDead(){
        return Dead;
    }

    public void Destroy(){
     mesh.Destroy();
    }
}

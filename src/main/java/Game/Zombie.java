package Game;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Texture;
import ObsidianEngine.utils.FileUtils;
import ObsidianEngine.utils.MathUtils;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Random;

public class Zombie {
    private Mesh mesh;

    public Zombie(Vector3f StartPosiition, float Radius){
        Mesh Zombie = FileUtils.LoadOBJWTextureSingle("/models/Zombie.obj", new Texture("/imgs/ZombieTexture.png"));
        Zombie.setPosition(-60,0,0);
        Zombie.setScale(50);
        this.mesh = Zombie;

        float X = (StartPosiition.x) + (MathUtils.getRandomNumber() * (Radius/2));
        float Y = (StartPosiition.y) + (MathUtils.getRandomNumber() * (Radius/2));
        Zombie.setPosition(50,0,0);
    }

    public void update(Vector3f goToPos, Camera cam){
        mesh.lookAt(goToPos);
        mesh.Draw(cam);
    }

}

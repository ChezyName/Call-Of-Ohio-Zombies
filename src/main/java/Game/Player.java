package Game;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Camera;
import ObsidianEngine.utils.MathUtils;
import org.joml.Vector3f;

public class Player {
    float Health = 100;
    Mesh mesh;

    public Player(Mesh m){
        this.mesh = m;
        this.mesh.setPosition(-30,0,0);
        this.mesh.setScale(50);
    }

    public float getRotation(){
        return mesh.getRotation().y;
    }

    public void resetHealth(){
        Health = 100;
    }

    public void setPosition(float x,float y, float z){
        mesh.Translate(x,y,z);
    }

    public void setRotation(float x, float y, float z){
        mesh.setRotation(x,y,z);
    }

    public Vector3f getPosition(){
        return mesh.getPosition();
    }

    public void ClampPosition(Vector3f startPos,Vector3f tLeft, Vector3f bRight){
        long x = MathUtils.clampLong((long) this.mesh.getPosition().x,(long) tLeft.x,(long) bRight.x);
        long z = MathUtils.clampLong((long) this.mesh.getPosition().z,(long) bRight.z,(long) tLeft.z);

        //long start = (long) startPos.x;
        //System.out.println("[CLAMP] " + start);

        this.mesh.setPosition(x,0,z);
    }

    public void Draw(Camera cam){
        mesh.Draw(cam);
    }

    public void takeDamage(float DMG){
        Health -= DMG;
    }

    public float getHealthPercent(){
        return Health / 100;
    }

    public int getHealth(){
        return (int) Health;
    }
}

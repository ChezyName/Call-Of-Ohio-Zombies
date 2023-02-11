package Game;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Camera;
import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import ObsidianEngine.utils.FileUtils;
import ObsidianEngine.utils.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Zombie {
    private Mesh mesh;
    private boolean Dead;
    private int WaveSpawnedOn = 0;
    private static Texture ZombieTexture = new Texture("/imgs/ZombieTexture.png");

    public Zombie(Vector3f StartPosiition, float Radius, int Wave){
        Mesh Zombie = FileUtils.LoadZombie();
        Zombie.setScale(50);
        this.mesh = Zombie;
        this.WaveSpawnedOn = Wave;

        Vector2f RandomPoint = MathUtils.getRandomPointOnRadius(StartPosiition.x,StartPosiition.z,Radius);
        Zombie.setPosition(RandomPoint.x,0,RandomPoint.y);
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
        //mesh.Draw(cam);
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

    public static void BatchRenderZombies(ArrayList<Zombie> Zombies,Camera cam){

        Shader.defaultTextureShader.bind();
        glActiveTexture(GL_TEXTURE0);
        ZombieTexture.bind();
        Shader.defaultTextureShader.uploadTexture(0);

        for(Zombie z : Zombies){
            if(!z.isDead()){
                Shader.defaultTextureShader.uploadMat4f("uProj",cam.getProjectionMatrix());
                Shader.defaultTextureShader.uploadMat4f("uView",cam.getViewMatrix());

                glBindVertexArray(z.mesh.getVAO());
                glEnableVertexAttribArray(0);
                glEnableVertexAttribArray(1);

                Matrix4f Transform = MathUtils.createTransformationMatrix(z.mesh.getPosition(),z.mesh.getRotation().x,z.mesh.getRotation().y,z.mesh.getRotation().z,z.mesh.getScale());
                Shader.defaultTextureShader.uploadMat4f("uTransform",Transform);

                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, z.mesh.getIBO());
                glDrawElements(GL_TRIANGLES, z.mesh.getIndices().length, GL_UNSIGNED_INT, 0);

                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);
                glBindVertexArray(0);
            }
        }

        Shader.defaultTextureShader.unbind();
        ZombieTexture.unbind();
    }
}

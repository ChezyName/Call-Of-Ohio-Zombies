package ObsidianEngine.entity;

import ObsidianEngine.render.Shader;
import org.joml.Vector3f;

public class Plane extends Mesh{
    public Plane(float width, float height, Vector3f position){
        super(new Vector3f[]{
                new Vector3f(position.x - (width/2),position.y,position.z + (height/2)),
                new Vector3f(position.x - (width/2),position.y,position.z - (height/2)),
                new Vector3f(position.x + (width/2),position.y,position.z + (height/2)),
                new Vector3f(position.x + (width/2),position.y,position.z - (height/2)),
        }, new int[]{
                0,1,2,
                1,2,3
        }, Shader.defaultShader,new Vector3f(255,255,255));
    }
}

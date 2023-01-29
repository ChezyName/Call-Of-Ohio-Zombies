package ObsidianEngine.entity;

import ObsidianEngine.render.Shader;
import ObsidianEngine.utils.ColorUtils;
import org.joml.Vector3f;

public class Plane extends Mesh{
    public Plane(float width, float depth, Vector3f position){
        super(new Vector3f[]{
                new Vector3f(position.x - (width/2),position.y,position.z + (depth/2)),
                new Vector3f(position.x - (width/2),position.y,position.z - (depth/2)),
                new Vector3f(position.x + (width/2),position.y,position.z + (depth/2)),
                new Vector3f(position.x + (width/2),position.y,position.z - (depth/2)),
        }, new int[] {
                0,1,2,
                1,2,3
        }, Shader.defaultShader, ColorUtils.White);
    }

    public Plane(float width, float depth, Vector3f position,Vector3f Color){
        super(new Vector3f[]{
                new Vector3f(position.x - (width/2),position.y,position.z + (depth/2)),
                new Vector3f(position.x - (width/2),position.y,position.z - (depth/2)),
                new Vector3f(position.x + (width/2),position.y,position.z + (depth/2)),
                new Vector3f(position.x + (width/2),position.y,position.z - (depth/2)),
        }, new int[] {
                0,1,2,
                1,2,3
        }, Shader.defaultShader, Color);
    }
}

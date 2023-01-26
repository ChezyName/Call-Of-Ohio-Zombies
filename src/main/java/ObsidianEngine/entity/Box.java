package ObsidianEngine.entity;

import ObsidianEngine.render.Shader;
import org.joml.Vector3f;

public class Box extends Mesh {

    public Box(float width, float height, float depth, Vector3f position){
        /*
            TOP LEFT -> TOP RIGHT -> BOTTOM LEFT -> BOTTOM RIGHT {FACING FACE}


         */
        super(new Vector3f[] {
                new Vector3f(-50f,50f,0.0f),
                new Vector3f(50f,50f,0.0f),
                new Vector3f(-50f,-50f,0.0f),
                new Vector3f(50f,-50f,0.0f),
        }, new int[]{
                0,1,2,
                0,2,3
        }, Shader.defaultShader);
    }
}

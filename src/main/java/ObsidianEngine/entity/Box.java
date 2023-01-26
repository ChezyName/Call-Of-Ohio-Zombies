package ObsidianEngine.entity;

import ObsidianEngine.render.Shader;
import org.joml.Vector3f;

import java.awt.*;

public class Box extends Mesh {

    public Box(float width, float height, float depth, Vector3f position) {
        /*
            FRONT FACE
            TOP FACE
            BACK FACE
            BOTTOM FACE
            LEFT FACE
         */
        super(new Vector3f[]{
                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z + -(depth / 2)),

                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z - -(depth / 2)),

                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z - -(depth / 2)),

                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z - -(depth / 2)),

                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z - -(depth / 2)),

                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z - -(depth / 2)),
        }, new int[]{
                0, 1, 2,
                1, 2, 3,

                4, 5, 6,
                5, 6, 7,

                8, 9, 10,
                9, 10, 11,

                12, 13, 14,
                13, 14, 15,

                16, 17, 18,
                17, 18, 19,

                20, 21, 22,
                21, 22, 23
        }, Shader.defaultShader, new Vector3f(255, 255, 255));
    }

    public Box(float width, float height, float depth, Vector3f position,Vector3f Color) {
        /*
        FRONT FACE
        TOP FACE
        BACK FACE
        BOTTOM FACE
        LEFT FACE
         */
        super(new Vector3f[]{
                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z + -(depth / 2)),

                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z - -(depth / 2)),

                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z - -(depth / 2)),

                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z - -(depth / 2)),

                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x + (width / 2), position.y - (height / 2), position.z - -(depth / 2)),

                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z + -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y + (height / 2), position.z - -(depth / 2)),
                new Vector3f(position.x - (width / 2), position.y - (height / 2), position.z - -(depth / 2)),
        }, new int[]{
                0, 1, 2,
                1, 2, 3,

                4, 5, 6,
                5, 6, 7,

                8, 9, 10,
                9, 10, 11,

                12, 13, 14,
                13, 14, 15,

                16, 17, 18,
                17, 18, 19,

                20, 21, 22,
                21, 22, 23
        }, Shader.defaultShader, Color);
    }
}

package ObsidianEngine.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Random;

public class MathUtils {
    public static Matrix4f createTransformationMatrix(Vector3f pos, float rx, float ry, float rz, float scale){
        Matrix4f m = new Matrix4f();
        m.identity();
        m.translate(pos);
        m.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0),m);
        m.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0),m);
        m.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1),m);
        m.scale(scale);
        return m;
    }

    public static float getRandomNumber(){
        Random random = new Random();
        return (float)(random.nextDouble() * 2 - 1);
    }

    public static int getRandomPosNeg(){
        float PN = getRandomNumber();
        if(PN > 0) return 1;
        else return -1;
    }
}

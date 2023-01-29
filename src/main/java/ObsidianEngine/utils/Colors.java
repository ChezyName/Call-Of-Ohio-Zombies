package ObsidianEngine.utils;

import org.joml.Random;
import org.joml.Vector3f;

public class Colors {
    public static Vector3f Black = new Vector3f(0,0,0);
    public static Vector3f White = new Vector3f(255,255,255);

    public static Vector3f Red = new Vector3f(255,0,0);
    public static Vector3f Orange = new Vector3f(255,100,0);
    public static Vector3f Yellow = new Vector3f(255,255,0);
    public static Vector3f Green = new Vector3f(0,255,0);
    public static Vector3f Blue = new Vector3f(0,0,255);
    public static Vector3f Cyan = new Vector3f(0,255,255);
    public static Vector3f Purple = new Vector3f(230,230,250);
    public static Vector3f Color(float red,float green,float blue) { return new Vector3f(red,green,blue); }
    public static Vector3f RandColor(){
        int rc = new Random(System.currentTimeMillis()).nextInt(8);
        System.err.println("Random Color: " + rc);
        switch (rc){
            case 1:
                return Black;
            case 2:
                return Red;
            case 3:
                return Orange;
            case 4:
                return Yellow;
            case 5:
                return Green;
            case 6:
                return Blue;
            case 7:
                return Cyan;
            case 8:
                return Purple;
            default:
                return White;
        }
    }

    public static Vector3f RandColor(int Index){
        System.err.println("Random Color: " + Index);
        switch (Index){
            case 1:
                return Black;
            case 2:
                return Red;
            case 3:
                return Orange;
            case 4:
                return Yellow;
            case 5:
                return Green;
            case 6:
                return Blue;
            case 7:
                return Cyan;
            case 8:
                return Purple;
            default:
                return White;
        }
    }
}

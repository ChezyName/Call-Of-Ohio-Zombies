package ObsidianEngine.utils;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class TimeUtils {
    private float lastTime;
    private float lastTimeFPS;
    private static TimeUtils TimeUtilityClass = null;

    public TimeUtils(){
        lastTime = getTime();
        lastTimeFPS = getTime();
    }
    public static TimeUtils getTimeUtils(){
        if(TimeUtilityClass == null) TimeUtilityClass = new TimeUtils();
        return TimeUtilityClass;
    }

    public float getTime(){
        return System.currentTimeMillis();
    }

    public int getFPS(){
        float TimePassed = (System.currentTimeMillis() - lastTimeFPS);
        lastTimeFPS = getTime();
        int fps = (int)(1000/TimePassed);
        return fps;
    }

    public float getDelta(){
        float currentTime = System.currentTimeMillis();
        float deltaTime = ( currentTime - lastTime ) / 1000.0f;
        //Lower Limit = Keep Below 60fps (DELTA TIME)
        if ( deltaTime < 0.0167f )
            deltaTime = 0.0167f;
        //Keep Delta Above 10fps
        else if ( deltaTime > 0.1f )
            deltaTime = 0.1f;

        lastTime = getTime();
        return deltaTime;
    }
}

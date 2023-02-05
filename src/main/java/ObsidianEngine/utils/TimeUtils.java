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
        float delta = currentTime - lastTime;
        lastTime = currentTime;
        float deltaTime = (1000/delta);
        System.out.println(deltaTime);
        return deltaTime;
    }
}

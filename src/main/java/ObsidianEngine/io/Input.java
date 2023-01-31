package ObsidianEngine.io;

import org.lwjgl.glfw.GLFW;
import static  org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Input {
    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double mouseX, mouseY;
    private static double scrollX, scrollY;

    private GLFWKeyCallback keyboard;
    private GLFWCursorPosCallback mouseMove;
    private GLFWMouseButtonCallback mouseButtons;
    private GLFWScrollCallback mouseScroll;

    public Input(long Window) {
        keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != GLFW.GLFW_RELEASE);
            }
        };

        mouseMove = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        };

        mouseButtons = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (action != GLFW.GLFW_RELEASE);
            }
        };

        mouseScroll = new GLFWScrollCallback() {
            public void invoke(long window, double offsetx, double offsety) {
                scrollX += offsetx;
                scrollY += offsety;
            }
        };

        glfwSetKeyCallback(Window,keyboard);
        glfwSetCursorPosCallback(Window,mouseMove);
        glfwSetMouseButtonCallback(Window,mouseButtons);
        glfwSetScrollCallback(Window,mouseScroll);
    }

    public static boolean getKeyDown(int key) {
        return keys[key];
    }

    public static boolean isMouseButtonDown(int button) {
        return buttons[button];
    }

    public void destroy(long Window) {
        //keyboard.free();
        //mouseMove.free();
        //mouseButtons.free();
        //mouseScroll.free();

        glfwSetKeyCallback(Window,null);
        glfwSetCursorPosCallback(Window,null);
        glfwSetMouseButtonCallback(Window,null);
        glfwSetScrollCallback(Window,null);
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static double getScrollX() {
        return scrollX;
    }

    public static double getScrollY() {
        return scrollY;
    }

    public GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public GLFWCursorPosCallback getMouseMoveCallback() {
        return mouseMove;
    }

    public GLFWMouseButtonCallback getMouseButtonsCallback() {
        return mouseButtons;
    }

    public GLFWScrollCallback getMouseScrollCallback() {
        return mouseScroll;
    }
}
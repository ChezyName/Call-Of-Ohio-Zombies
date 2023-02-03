package ObsidianEngine.utils;

import ObsidianEngine.io.Input;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;


public class MouseUtils {

    public static float getMouseRotFromCenter(long WindowID) {
        float mouseX = (float) Input.getMouseX();
        float mouseY = (float) Input.getMouseY();

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(WindowID,widthBuffer,heightBuffer);

        int width =  widthBuffer.get(0);
        int height = heightBuffer.get(0);

        float x = (2.0f * mouseX) / width - 1.0f;
        float y = 1.0f - ((2.0f * mouseY) / height);
        y *= (9.f/16.f);

        System.out.println("(" + x + "," + y + ")");

        //Calculate Rotation Angle
        float angle = (float) Math.toDegrees(Math.atan2(y, x));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public static void SetCursorImage(long Window,String path){
        try {
            InputStream stream = new FileInputStream(FileUtils.getJarLoc() + path);
            BufferedImage image = ImageIO.read(stream);

            int width = image.getWidth();
            int height = image.getHeight();

            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            // convert image to RGBA format
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    int pixel = pixels[y * width + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));  // red
                    buffer.put((byte) ((pixel >> 8) & 0xFF));   // green
                    buffer.put((byte) (pixel & 0xFF));          // blue
                    buffer.put((byte) ((pixel >> 24) & 0xFF));  // alpha
                }
            }
            buffer.flip(); // this will flip the cursor image vertically

            // create a GLFWImage
            GLFWImage cursorImg = GLFWImage.create();
            cursorImg.width(width);     // set up image width
            cursorImg.height(height);   // set up image height
            cursorImg.pixels(buffer);   // pass image data

            // the hotspot indicates the displacement of the sprite to the
            // position where mouse clicks are registered (see image below)
            int hotspotX = 3;
            int hotspotY = 6;

            // create custom cursor and store its ID
            long cursorID = glfwCreateCursor(cursorImg, hotspotX , hotspotY);

            // set current cursor
            glfwSetCursor(Window, cursorID);
        }
        catch (IOException e){

        }
    }
}
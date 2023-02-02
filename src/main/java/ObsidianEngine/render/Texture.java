package ObsidianEngine.render;

import ObsidianEngine.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture {
    private int ID;

    public Texture(String filePath){
        //Create Texture
        ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, ID);

        // Texture Repeating
        //glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
        //glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer colorChannels = BufferUtils.createIntBuffer(1);

        //RGB Data
        ByteBuffer image = STBImage.stbi_load(FileUtils.getJarLoc() + filePath,width,height,colorChannels, 0);

        if(image == null){
            System.err.println("[TEXTURE LOADING] Image: " + filePath + " Could not be loaded...");
        }
        else{
            glPixelStorei(GL_UNPACK_ALIGNMENT,1);
            glTexImage2D(GL_TEXTURE_2D,0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(image);
        }
        this.unbind();
    }

    public int getID() { return ID; }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D,ID);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D,0);
    }
}

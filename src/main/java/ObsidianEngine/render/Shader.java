package ObsidianEngine.render;

import ObsidianEngine.utils.FileUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Vector;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private String vertexFile, fragmentFile,vertexPath,fragmentPath;
    private int vertexID, fragmentID, programID;

    public static Shader defaultShader;
    public static Shader defaultTextureShader;
    public static Shader defaultUIShader;

    public Shader(String vertPath, String fragPath){
        vertexFile = FileUtils.loadAsString(vertPath);
        fragmentFile = FileUtils.loadAsString(fragPath);
        vertexPath = vertPath;
        fragmentPath = fragPath;
        this.create();
    }

    public void create(){
        programID = glCreateProgram();
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        
        glShaderSource(vertexID,vertexFile);
        glCompileShader(vertexID);

        //Did'nt Compile & Err Catch
        if(glGetShaderi(vertexID,GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("[" + FileUtils.getFileNameResource(vertexPath) + "] Vertex Shader: " + glGetShaderInfoLog(vertexID));
            return;
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentID,fragmentFile);
        glCompileShader(fragmentID);

        //Did'nt Compile & Err Catch
        if(glGetShaderi(fragmentID,GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("[" + FileUtils.getFileNameResource(fragmentPath) + "] Fragment Shader: " + glGetShaderInfoLog(fragmentID));
            return;
        }

        glAttachShader(programID,vertexID);
        glAttachShader(programID,fragmentID);

        glLinkProgram(programID);
        if(glGetProgrami(programID,GL_LINK_STATUS) == GL_FALSE){
            System.err.println("Program Linking Error: " + glGetProgramInfoLog(programID));
        }

        glValidateProgram(programID);
        if(glGetProgrami(programID,GL_VALIDATE_STATUS) == GL_FALSE){
            System.err.println("Program Validate Error: " + glGetProgramInfoLog(programID));
        }

        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public void bind(){
        glUseProgram(programID);
    }

    public void unbind(){
        glUseProgram(0);
    }

    public void destroy(){
        glDeleteProgram(programID);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLoc = glGetUniformLocation(programID,varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLoc,false,matBuffer);
    }

    public void uploadColor(Vector3f Color){
        int colorLoc = glGetUniformLocation(programID,"InputColor");
        glUniform3f(colorLoc,Color.x/255,Color.y/255,Color.z/255);
    }

    public void uploadTexture(int slot) {
        int TextureLoc = glGetUniformLocation(programID,"InputTexture");
        glUniform1i(TextureLoc,slot);
    }

    public void uploadVec2(String name, Vector2f vec){
        int Loc = glGetUniformLocation(programID,name);
        glUniform2f(Loc,vec.x,vec.y);
    }
}

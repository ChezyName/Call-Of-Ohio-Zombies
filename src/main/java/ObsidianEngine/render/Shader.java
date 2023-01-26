package ObsidianEngine.render;

import ObsidianEngine.Utils.FileUtils;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private String vertexFile, fragmentFile;
    private int vertexID, fragmentID, programID;

    public static Shader defaultShader;

    public Shader(String vertPath, String fragPath){
        vertexFile = FileUtils.loadAsString(vertPath);
        fragmentFile = FileUtils.loadAsString(fragPath);
    }

    public void create(){
        programID = glCreateProgram();
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        
        glShaderSource(vertexID,vertexFile);
        glCompileShader(vertexID);

        //Did'nt Compile & Err Catch
        if(glGetShaderi(vertexID,GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Vertex Shader: " + glGetShaderInfoLog(vertexID));
            return;
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentID,fragmentFile);
        glCompileShader(fragmentID);

        //Did'nt Compile & Err Catch
        if(glGetShaderi(fragmentID,GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Fragment Shader: " + glGetShaderInfoLog(fragmentID));
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
}

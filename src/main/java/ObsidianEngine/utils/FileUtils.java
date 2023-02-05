package ObsidianEngine.utils;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class FileUtils {
    public static String loadAsString(String path){
        StringBuilder File = new StringBuilder();
        String line = "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(path)))){
            while((line = reader.readLine()) != null){
                File.append(line).append("\n");
            }
        }
        catch (IOException e) {
            System.err.println("file not found at " + path);
        }

        return File.toString();
    }

    public static String getFileNameResource(String path){
        return FileUtils.class.getResource(path).getFile();
    }

    public static String getJarLoc() { return new File(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent(); }

    public static Mesh LoadOBJWTexture(String path, ArrayList<Mesh> AllMeshes, Texture texture){
        //System.out.println(getJarLoc());
        AIScene scene = Assimp.aiImportFile(getJarLoc() + path,Assimp.aiProcess_Triangulate);

        if(scene == null) { System.err.println("Couldn't Find File: " + path); return null; }

        Mesh FinalM = null;
        PointerBuffer buffer = scene.mMeshes();
        for(int i = 0; i < buffer.limit(); i++){
            AIMesh mesh = AIMesh.create(buffer.get(i));

            int v = mesh.mNumVertices();
            AIVector3D.Buffer VB = mesh.mVertices();

            PointerBuffer uv = mesh.mTextureCoords();

            int fc = mesh.mNumFaces();
            AIFace.Buffer FB = mesh.mFaces();

            Vector3f[] verticies = new Vector3f[v];
            int[] indicies = new int[fc * 3];

            //Vertices Calculation
            for(int j = 0; j < v; j++){
                verticies[j] = new Vector3f(VB.get(j).x(),VB.get(j).y(),VB.get(j).z());
            }

            //UV Calculations
            AIVector3D.Buffer coords = mesh.mTextureCoords(i);
            float[] UVs = new float[coords.limit()*2];

            for(int l = 0; l < coords.limit(); l++){
                UVs[l] = coords.x();
                UVs[l+1] = coords.y();
            }

            //IIndices calculation
            for(int k = 0; k < fc; k++){
                indicies[k * 3 + 0] = FB.get(k).mIndices().get(0);
                indicies[k * 3 + 1] = FB.get(k).mIndices().get(1);
                indicies[k * 3 + 2] = FB.get(k).mIndices().get(2);
            }

            Mesh m = new Mesh(verticies,indicies, Shader.defaultTextureShader,texture,UVs);
            FinalM = m;
            AllMeshes.add(m);
        }
        return FinalM;
    }

    public static Mesh LoadOBJWTextureSingle(String path, Texture texture){
        //System.out.println(getJarLoc());
        AIScene scene = Assimp.aiImportFile(getJarLoc() + path,Assimp.aiProcess_Triangulate);

        if(scene == null) { System.err.println("Couldn't Find File: " + path); return null; }

        Mesh FinalM = null;
        PointerBuffer buffer = scene.mMeshes();
        for(int i = 0; i < buffer.limit(); i++){
            AIMesh mesh = AIMesh.create(buffer.get(i));

            int v = mesh.mNumVertices();
            AIVector3D.Buffer VB = mesh.mVertices();

            PointerBuffer uv = mesh.mTextureCoords();

            int fc = mesh.mNumFaces();
            AIFace.Buffer FB = mesh.mFaces();

            Vector3f[] verticies = new Vector3f[v];
            int[] indicies = new int[fc * 3];

            //Vertices Calculation
            for(int j = 0; j < v; j++){
                verticies[j] = new Vector3f(VB.get(j).x(),VB.get(j).y(),VB.get(j).z());
            }

            //UV Calculations
            AIVector3D.Buffer coords = mesh.mTextureCoords(i);
            float[] UVs = new float[coords.limit()*2];

            for(int l = 0; l < coords.limit(); l++){
                UVs[l] = coords.x();
                UVs[l+1] = coords.y();
            }

            //IIndices calculation
            for(int k = 0; k < fc; k++){
                indicies[k * 3 + 0] = FB.get(k).mIndices().get(0);
                indicies[k * 3 + 1] = FB.get(k).mIndices().get(1);
                indicies[k * 3 + 2] = FB.get(k).mIndices().get(2);
            }

            Mesh m = new Mesh(verticies,indicies, Shader.defaultTextureShader,texture,UVs);
            return m;
        }
        return null;
    }

    public static Mesh LoadOBJ(String path,Vector3f Color, ArrayList<Mesh> AllMeshes){
        //System.out.println(getJarLoc());
        AIScene scene = Assimp.aiImportFile(getJarLoc() + path,Assimp.aiProcess_Triangulate);

        if(scene == null) { System.err.println("Couldn't Find File: " + path); return null; }
        Mesh FinalM = null;
        PointerBuffer buffer = scene.mMeshes();
        for(int i = 0; i < buffer.limit(); i++){
            AIMesh mesh = AIMesh.create(buffer.get(i));

            int v = mesh.mNumVertices();
            AIVector3D.Buffer VB = mesh.mVertices();

            int fc = mesh.mNumFaces();
            AIFace.Buffer FB = mesh.mFaces();

            Vector3f[] verticies = new Vector3f[v];
            int[] indicies = new int[fc * 3];

            //Vertices Calculation
            for(int j = 0; j < v; j++){
                verticies[j] = new Vector3f(VB.get(j).x(),VB.get(j).y(),VB.get(j).z());
            }

            //Indices calculation
            for(int k = 0; k < fc; k++){
                indicies[k * 3 + 0] = FB.get(k).mIndices().get(0);
                indicies[k * 3 + 1] = FB.get(k).mIndices().get(1);
                indicies[k * 3 + 2] = FB.get(k).mIndices().get(2);
            }

            Mesh m = new Mesh(verticies,indicies, Shader.defaultShader,Color);
            FinalM = m;
            AllMeshes.add(m);
        }
        return FinalM;
    }
}

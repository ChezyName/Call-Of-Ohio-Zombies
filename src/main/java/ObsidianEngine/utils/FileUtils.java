package ObsidianEngine.utils;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryUtil.memSlice;

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

    public static ByteBuffer loadFileToByteBuffer(String path) throws IOException {
        File file = new File(getJarLoc() + path);
        System.err.println(getJarLoc() + path);
        ByteBuffer buffer;
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel fc = fis.getChannel();)
        {
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        }
        return buffer;
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

    static Texture PlayerTexture;
    static Vector3f[] PlayerV;
    static int[] PlayerI;
    static float[] PlayerUVs;

    public static Mesh LoadPlayer(){
        String path = "/models/Link.obj";

        if(PlayerTexture == null || PlayerV == null || PlayerI == null || PlayerUVs == null){
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

                Texture zT = new Texture("/imgs/PlayerTexture.png");

                PlayerUVs = UVs;
                PlayerV = verticies;
                PlayerI = indicies;
                PlayerTexture = zT;

                Mesh m = new Mesh(verticies,indicies, Shader.defaultTextureShader,zT,UVs);
                return m;
            }
            return null;
        }
        else {
            return new Mesh(PlayerV,PlayerI, Shader.defaultTextureShader,PlayerTexture,PlayerUVs);
        }
    }

    static Texture ZombieTexture;
    static Vector3f[] ZombieV;
    static int[] ZombieI;
    static float[] ZombieUVs;

    public static Mesh LoadZombie(){
        String path = "/models/Zombie.obj";

        if(ZombieTexture == null || ZombieV == null || ZombieI == null || ZombieUVs == null){
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

                Texture zT = new Texture("/imgs/ZombieTexture.png");

                ZombieUVs = UVs;
                ZombieV = verticies;
                ZombieI = indicies;
                ZombieTexture = zT;

                Mesh m = new Mesh(verticies,indicies, Shader.defaultTextureShader,zT,UVs);
                return m;
            }
            return null;
        }
        else {
            return new Mesh(ZombieV,ZombieI, Shader.defaultTextureShader,ZombieTexture,ZombieUVs);
        }
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

    static int[] BulletI;
    static Vector3f[] BulletV;

    public static Mesh LoadBullet(){
        String path = "/models/Bullet.obj";
        Vector3f Color = ColorUtils.Red;

        if(BulletI == null || BulletV == null){
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

                BulletI = indicies;
                BulletV  = verticies;
                Mesh m = new Mesh(verticies,indicies, Shader.defaultShader,Color);
                return m;
            }
            return FinalM;
        }
        else{
            return new Mesh(BulletV,BulletI, Shader.defaultShader,Color);
        }
    }

    public static class ImageBytes{
        public ByteBuffer image;
        public int width, heigh;
        public ImageBytes(int width, int heigh, ByteBuffer image) {
            this.image = image;
            this.heigh = heigh;
            this.width = width;
        }
    }

    public static ImageBytes getImageIcon(String path){
        ByteBuffer image;
        int width, heigh;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            image = stbi_load(path, w, h, comp, 4);
            if (image == null) {
                // throw new resource_error("Could not load image resources.");
            }
            width = w.get();
            heigh = h.get();
        }
        return new ImageBytes(width, heigh, image);
    }
}

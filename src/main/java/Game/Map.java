package Game;

import ObsidianEngine.entity.Mesh;
import ObsidianEngine.entity.Plane;
import ObsidianEngine.render.Shader;
import ObsidianEngine.render.Texture;
import ObsidianEngine.utils.ColorUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Map {
    public static void getMap(float size,float incerment, ArrayList<Mesh> Meshes){
        Vector2f startPos = new Vector2f(-size,-size);
        while (startPos.y < size){
            while (startPos.x < size) {
                //Create Squares
                Mesh Ground = new Plane(incerment,incerment,new Vector3f(startPos.x,0,startPos.y), ColorUtils.Green);
                Ground.setTexture(new Texture("/imgs/Grass.jpg",false));
                Ground.setShader(Shader.defaultTextureShader);
                Ground.Create();
                Meshes.add(Ground);

                System.out.println("Map Space @ " + startPos.x + "," + startPos.y);

                startPos.x += incerment;
            }
            startPos.y += incerment;
            startPos.x = -size;
        }
    }
}

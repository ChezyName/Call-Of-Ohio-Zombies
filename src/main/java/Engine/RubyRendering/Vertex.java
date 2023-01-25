package Engine.RubyRendering;

import Engine.MoonstoneMaths.Vector3f;

public class Vertex {
    private Vector3f position;

    public Vertex(Vector3f pos){
        this.position = pos;
    }

    public Vector3f getPosition() {
        return position;
    }
}

package rs.node.impl;

import rs.node.CacheLink;
import rs.scene.model.Model;
import rs.scene.model.Vertex;

public class Renderable extends CacheLink {

    public int height;
    public Vertex[] normal;

    public Renderable() {
        height = 1000;
    }

    public Model get_model() {
        return null;
    }

    public void render(int rotation, int cam_pitch_sin, int cam_pitch_cos, int cam_yaw_sin, int cam_yaw_cos, int x, int y, int z, int uid) {
        Model m = this.get_model();
        if (m != null) {
            height = ((Renderable) (m)).height;
            m.render(rotation, cam_pitch_sin, cam_pitch_cos, cam_yaw_sin, cam_yaw_cos, x, y, z, uid);
        }
    }
}

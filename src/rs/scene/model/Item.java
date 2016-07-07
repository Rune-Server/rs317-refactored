package rs.scene.model;

import rs.cache.model.ObjConfig;
import rs.node.impl.Renderable;

public class Item extends Renderable {

    public short index;
    public int stack_index;

    public Item() {
    }

    @Override
    public Model get_model() {
        return ObjConfig.get(index).get_model(stack_index);
    }
}

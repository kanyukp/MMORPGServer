package WizardMoneyGroup.MMORPGServer;
import WizardMoneyGroup.MMORPGServer.Models.Entity;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {
    private static final int MAX_ENTITIES = 4;
    private static final int MAX_LEVELS = 5;

    private int level;
    private List<Entity> entities;
    private QuadTree[] nodes;
    private int x,y,width,height;

    public QuadTree(int level,int x, int y, int width, int height) {
        this.level = level;
        this.entities = new ArrayList<>();
        this.nodes = new QuadTree[4];
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void clear() {
        entities.clear();
        for(int i = 0; i < nodes.length; i++){
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() {
        int subWidth = width / 2;
        int subHeight = height / 2;
        int xMid = x + subWidth;
        int yMid = y + subHeight;

        nodes[0] = new QuadTree(level + 1, x, y, subWidth, subHeight);
        nodes[1] = new QuadTree(level + 1, xMid, y, subWidth, subHeight);
        nodes[2] = new QuadTree(level + 1, x, yMid, subWidth, subHeight);
        nodes[3] = new QuadTree(level + 1, xMid, yMid, subWidth, subHeight);
    }

    private int getIndex(Entity entity) {
        int index = -1;
        double verticalMidpoint = x + (width / 2);
        double horizontalMidpoint = y + (height / 2);

        boolean topQuadrant = (entity.getY() < horizontalMidpoint && entity.getY() + entity.getHeight() < horizontalMidpoint);
        boolean bottomQuadrant = (entity.getY() > horizontalMidpoint);

        if (entity.getX() < verticalMidpoint && entity.getX() + entity.getWidth() < verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } else if (entity.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }
        return index;
    }

    public void insert(Entity entity) {
        if(nodes[0] != null) {
            int index = getIndex(entity);
            if (index != -1) {
                nodes[index].insert(entity);
                return;
            }
        }
        entities.add(entity);

        if (entities.size() > MAX_ENTITIES && level < MAX_LEVELS) {
            if (nodes[0] == null){
                split();
            }
            int i = 0;
            while (i < entities.size()){
                int index = getIndex(entities.get(i));
                if(index != -1) {
                    nodes[index].insert(entities.remove(i));
                } else {
                    i++;
                }
            }
        }
    }

    public List<Entity> retrieve(List<Entity> returnEntities, Entity entity){
        int index = getIndex(entity);
        if(index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnEntities, entity);
        }

        for(Entity e : entities) {
            if(intersects(e, entity)) {
                returnEntities.add(e);
            }
        }

        return returnEntities;
    }

    private boolean intersects(Entity a, Entity b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() < a.getWidth() + b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() < a.getHeight() + b.getY();
    }
}

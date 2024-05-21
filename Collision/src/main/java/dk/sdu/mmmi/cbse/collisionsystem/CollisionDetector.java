package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class CollisionDetector implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> toRemove = new ArrayList<>();

        // two for loops for all entities in the world
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {

                // if the two entities are identical, skip the iteration
                if (entity1.getID().equals(entity2.getID())) {
                    continue;                    
                }

                // CollisionDetection
                if (this.collides(entity1, entity2)) {
                    //remove from world
                    world.removeEntity(entity1);
                    world.removeEntity(entity2);

                    if(entity1.getType() == EntityType.ASTEROID){
                        getAsteroidSplitters().stream().findFirst().ifPresent( iAsteroidSplitter -> iAsteroidSplitter.createSplitAsteroid(entity1, world));
                        if(entity2.getType() == EntityType.BULLET && entity2.getParentType() == EntityType.PLAYER){
                            gameData.incrementScore(1);
                        }
                    } else if (entity2.getType() == EntityType.ASTEROID) {
                        getAsteroidSplitters().stream().findFirst().ifPresent( iAsteroidSplitter -> iAsteroidSplitter.createSplitAsteroid(entity2, world));
                        if(entity1.getType() == EntityType.BULLET && entity1.getParentType() == EntityType.PLAYER){
                            gameData.incrementScore(1);
                        }
                    }

                    toRemove.add(entity1);
                }
            }
        }

        for (Entity entity : toRemove){
            if(entity.getType() == EntityType.ASTEROID){
                world.incrementAsteroids(-1);
            }
            if(entity.getType() == EntityType.ENEMY){
                world.incrementEnemies(-1);
            }
            world.removeEntity(entity);
        }

    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private Collection<? extends IAsteroidSplitter> getAsteroidSplitters() {
        return ServiceLoader.load(IAsteroidSplitter.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}

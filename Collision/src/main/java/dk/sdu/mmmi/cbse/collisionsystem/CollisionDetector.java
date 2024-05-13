package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Collection;
import java.util.Optional;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class CollisionDetector implements IPostEntityProcessingService {

    public CollisionDetector() {
    }

    @Override
    public void process(GameData gameData, World world) {
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

                    //update entity counters
                    updateEntityCounter(entity1, world);
                    updateEntityCounter(entity2, world);

                    //call all processing services and let them handle the collision (beyond removing from world)
                    for(IEntityProcessingService iEntityProcessingService : getEntityProcessingServices()){
                        iEntityProcessingService.collision(gameData, world, entity1, entity2);
                    }

                }
            }
        }

    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private void updateEntityCounter(Entity entity, World world) {
        if (entity instanceof Asteroid) {
            System.out.println("remove asteroids");
            world.setAsteroids(world.getAsteroids() - 1);
        }
    }

}

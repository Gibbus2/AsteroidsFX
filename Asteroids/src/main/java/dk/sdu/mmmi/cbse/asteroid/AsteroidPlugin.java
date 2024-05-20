package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Random;

/**
 *
 * @author corfixen
 */
public class AsteroidPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        Entity asteroid = createAsteroid(gameData);
        world.addEntity(asteroid);
        world.setAsteroids(world.getAsteroids() + 1);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
        world.setAsteroids(0);
    }

    @Override
    public EntityType type() {
        return EntityType.ASTEROID;
    }

    private Entity createAsteroid(GameData gameData) {
        Random rnd = new Random();
        int heading = 10 + rnd.nextInt(90);

        return createAsteroid(10, 10 + rnd.nextInt(gameData.getDisplayHeight() - 10),
                10 + rnd.nextInt(gameData.getDisplayWidth() - 10), heading, 400, 200);
    }

    public Entity createAsteroid(int size, double x, double y, double heading, int rotationSpeed, int forwardSpeed){
        System.out.println("Creating asteroid");
        Entity asteroid = new Asteroid();

        asteroid.setPolygonCoordinates(size, -size, -size, -size, -size, size, size, size);
        asteroid.setX(x);
        asteroid.setY(y);
        asteroid.setRadius(size);
        asteroid.setRotation(0);

        asteroid.setRotationSpeed(rotationSpeed);
        asteroid.setForwardSpeed(forwardSpeed);

        asteroid.setHeading(heading);

        asteroid.setType(EntityType.ASTEROID);

        return asteroid;
    }
}

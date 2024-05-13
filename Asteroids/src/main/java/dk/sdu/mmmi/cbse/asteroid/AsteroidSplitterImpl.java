package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 *
 * @author corfixen
 */
public class AsteroidSplitterImpl implements IAsteroidSplitter {
    AsteroidPlugin asteroidPlugin = new AsteroidPlugin();

    @Override
    public void createSplitAsteroid(Entity e, World world) {
        int size = (int) e.getRadius() / 2;
        int speed =  (int) (e.getForwardSpeed() * 1.5);
        double heading1 = e.getHeading() + 20;
        double heading2 = e.getHeading() - 20;

        if(size > 2) {
            System.out.println("Asteroid split");
            world.setAsteroids(world.getAsteroids() + 2);

            Entity split1 = asteroidPlugin.createAsteroid(size, e.getX(), e.getY(), heading1, e.getRotationSpeed(), speed);
            Entity split2 = asteroidPlugin.createAsteroid(size, e.getX(), e.getY(), heading2, e.getRotationSpeed(), speed);

            //move to avoid collision with each other
            split1.forceMove((int) (e.getRadius() * 2));
            split2.forceMove((int) (e.getRadius() * 2));

            //add to world
            world.addEntity(split1);
            world.addEntity(split2);
        }
    }

}

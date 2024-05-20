package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class AsteroidProcessor implements IEntityProcessingService {

    private IAsteroidSplitter asteroidSplitter = new AsteroidSplitterImpl();
    private long spawnDelta = 0;
    private AsteroidPlugin asteroidPlugin = new AsteroidPlugin();

    @Override
    public void process(GameData gameData, World world) {
        //spawn new asteroids if needed
        spawn(gameData, world);

        //move and rotate asteroids
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            asteroid.rotate(gameData.getDelta(), true);
            asteroid.forward(gameData.getDelta());

            if (asteroid.getX() < 0) {
                asteroid.setX(gameData.getDisplayWidth());
            }

            if (asteroid.getX() > gameData.getDisplayWidth()) {
                asteroid.setX(0);
            }

            if (asteroid.getY() < 0) {
                asteroid.setY(gameData.getDisplayHeight());
            }

            if (asteroid.getY() > gameData.getDisplayHeight()) {
                asteroid.setY(0);
            }

        }

    }

    private void spawn(GameData gameData, World world){
        spawnDelta += gameData.getDelta();
        int spawnDeltaSec = (int) (spawnDelta/1_000_000_000);
        if(spawnDeltaSec >= 3 && world.getAsteroids() < 5){
            System.out.println("Spawn new Asteroid");
            Entity asteroid = asteroidPlugin.createAsteroid(gameData);
            world.addEntity(asteroid);
            world.incrementAsteroids(1);
            spawnDelta = 0;
        }
    }


    /**
     * Dependency Injection using OSGi Declarative Services
     */
    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = null;
    }


}

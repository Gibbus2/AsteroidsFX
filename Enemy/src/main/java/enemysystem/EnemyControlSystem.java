package enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;


public class EnemyControlSystem implements IEntityProcessingService {
    private Random random = new Random();
    private EnemyPlugin enemyPlugin = new EnemyPlugin();
    private long spawnDelta;

    @Override
    public void process(GameData gameData, World world) {

        spawn(gameData,world);

        for(Entity e : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) e;

            // rotate
            enemy.rotate(gameData.getDelta(), random.nextBoolean());
            enemy.setHeading(enemy.getRotation());

            // move forward
            enemy.forward(gameData.getDelta());

            // stay inside the screen
            if (enemy.getX() < 0) {
                enemy.setX(gameData.getDisplayWidth());
            }
            if (enemy.getX() > gameData.getDisplayWidth()) {
                enemy.setX(0);
            }
            if (enemy.getY() < 0) {
                enemy.setY(gameData.getDisplayHeight());
            }
            if (enemy.getY() > gameData.getDisplayHeight()) {
                enemy.setY(0);
            }

            if(enemy.canShoot(gameData.getFrame())){
                getBulletSPI().stream().findFirst().ifPresent( bulletSPI -> world.addEntity(bulletSPI.createBullet(enemy, gameData)));
            }
        }

    }

    private void spawn(GameData gameData, World world){
        if(world.getEnemies() < 1) {
            spawnDelta += gameData.getDelta();
            int spawnDeltaSec = (int) (spawnDelta / 1_000_000_000);
            if (spawnDeltaSec >= 5) {
                Entity enemy = enemyPlugin.createShip(gameData);
                world.addEntity(enemy);
                world.incrementEnemies(1);
                spawnDelta = 0;
            }
        }
    }

    private Collection<? extends BulletSPI> getBulletSPI() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}

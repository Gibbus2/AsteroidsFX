package enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;

import java.util.Random;
import java.util.ServiceLoader;


public class EnemyControlSystem implements IEntityProcessingService {
    Random random = new Random();
    @Override
    public void process(GameData gameData, World world) {
        for(Entity e : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) e;

            int ran = random.nextInt(100);

            if(ran <= 40){
                enemy.forward(gameData.getDelta());
            } else if (ran > 40 && ran <= 50) {
                enemy.rotate(gameData.getDelta(), false);
                enemy.setHeading(enemy.getRotation());
            } else if (ran > 50 && ran <= 60) {
                enemy.rotate(gameData.getDelta(), false);
                enemy.setHeading(enemy.getRotation());
            } else if (ran > 97) {
                if(enemy.canShoot(gameData.getFrame())) {
                    //shoot
                    ServiceLoader<BulletSPI> loader = ServiceLoader.load(BulletSPI.class);
                    loader.findFirst().ifPresent(bulletSPI -> world.addEntity(bulletSPI.createBullet(enemy, gameData)));
                }
            }
        }

    }

}

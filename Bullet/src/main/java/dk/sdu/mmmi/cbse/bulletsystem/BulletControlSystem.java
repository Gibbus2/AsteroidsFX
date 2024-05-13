package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity bullet : world.getEntities(Bullet.class)) {
            bullet.forward(gameData.getDelta());

            //remove bullet if out of bounds
            if(bullet.getY() > gameData.getDisplayWidth() || bullet.getY() < 0 || bullet.getX() > gameData.getDisplayHeight()  || bullet.getX() < 0){
                world.removeEntity(bullet);
            }
        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Bullet();
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);
        bullet.setRotation(shooter.getHeading());
        bullet.setHeading(shooter.getHeading());
        bullet.setRadius(1);
        bullet.setForwardSpeed(400);
        bullet.setY(shooter.getY());
        bullet.setX(shooter.getX());
        bullet.forceMove(10);
        return bullet;
    }
}

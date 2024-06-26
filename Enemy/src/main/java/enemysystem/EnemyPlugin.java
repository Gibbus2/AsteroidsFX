package enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
public class EnemyPlugin implements IGamePluginService {
    @Override
    public void start(GameData gameData, World world) {
    }
    public Entity createShip(GameData gameData){
        Entity ship = new Enemy(1.5);
        ship.setPolygonCoordinates(-5,-5,10,0,-5,5);
        ship.setX((double) gameData.getDisplayHeight() /2 - 50);
        ship.setY(50);
        ship.setRadius(8);
        ship.setRotation(90); // spawn looking down
        ship.setRotationSpeed(300);
        ship.setForwardSpeed(100);

        ship.setType(EntityType.ENEMY);
        return ship;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity entity : world.getEntities(Enemy.class)){
            world.removeEntity(entity);
            world.incrementEnemies(-1);
        }
    }

    @Override
    public EntityType type() {
        return EntityType.ENEMY;
    }
}

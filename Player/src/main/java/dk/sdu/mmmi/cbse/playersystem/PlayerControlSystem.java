package dk.sdu.mmmi.cbse.playersystem;


import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.ServiceLoader;



public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity p : world.getEntities(Player.class)) {
            Player player = (Player) p;
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.rotate(gameData.getDelta(), false);
                player.setHeading(player.getRotation());
            }
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.rotate(gameData.getDelta(), true);
                player.setHeading(player.getRotation());
            }
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                player.forward(gameData.getDelta());
            }
            if(gameData.getKeys().isDown(GameKeys.SPACE)) {
                if(player.canShoot(gameData.getFrame())) {

                    ServiceLoader<BulletSPI> loader = ServiceLoader.load(BulletSPI.class);
                    loader.findFirst().ifPresent(bulletSPI -> world.addEntity(bulletSPI.createBullet(player, gameData)));

                }
            }
            
        if (player.getX() < 0) {
            player.setX(1);
        }

        if (player.getX() > gameData.getDisplayWidth()) {
            player.setX(gameData.getDisplayWidth()-1);
        }

        if (player.getY() < 0) {
            player.setY(1);
        }

        if (player.getY() > gameData.getDisplayHeight()) {
            player.setY(gameData.getDisplayHeight()-1);
        }

                                        
        }
    }

}

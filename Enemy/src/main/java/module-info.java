import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module Enemy {
    requires Common;
    requires CommonBullet;
    requires CommonEnemy;
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    provides IEntityProcessingService with enemysystem.EnemyControlSystem;
    provides IGamePluginService with enemysystem.EnemyPlugin;

}
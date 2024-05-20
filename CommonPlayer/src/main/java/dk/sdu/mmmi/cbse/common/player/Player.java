package dk.sdu.mmmi.cbse.common.player;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 *
 * @author Emil
 */
public class Player extends Entity {

    private long lastShot;
    private final double timeBetweenShot;
    public Player(double timeBetweenShot){
        this.lastShot = 0;
        this.timeBetweenShot = timeBetweenShot;
    }

    public boolean canShoot(long now){
        boolean res = (now - lastShot) > (1_000_000_000 * timeBetweenShot);
        if(res) lastShot = now;
        return res;
    }

}

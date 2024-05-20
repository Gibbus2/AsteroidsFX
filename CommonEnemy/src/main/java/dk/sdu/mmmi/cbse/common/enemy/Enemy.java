package dk.sdu.mmmi.cbse.common.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Enemy extends Entity {

    private long lastShot;
    private final double timeBetweenShot;
    public Enemy(double timeBetweenShot){
        this.lastShot = 0;
        this.timeBetweenShot = timeBetweenShot;
    }

    public boolean canShoot(long now){
        boolean res = (now - lastShot) > (1_000_000_000 * timeBetweenShot);
        if(res) lastShot = now;
        return res;
    }
}

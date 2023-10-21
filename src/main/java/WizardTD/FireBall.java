package WizardTD;

import processing.core.PApplet;

public class FireBall extends PApplet {
    int x, y;
    int targetX;
    int targetY;
    int speed;
    int damage;
    boolean canFire;
    boolean isFiring;
    boolean hasHitTarget;

    public FireBall(int x, int y, int targetX, int targetY, int speed) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;
        this.canFire = true;
        this.isFiring = false;
        this.hasHitTarget = false;
    }

    public void move() {
        if (isFiring && !hasHitTarget) {
            int dx = targetX - x;
            int dy = targetY - y;
            float distance = dist(x, y, targetX, targetY);

            if (distance > 0) {
                x += dx / distance * speed;
                y += dy / distance * speed;
            }

            if (distance < 1) {
                hasHitTarget = true;
            }
        }

    }

    public boolean hitTargetMonster() {
        return hasHitTarget;
    }

    public void reset(int x, int y, int targetX, int targetY) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        isFiring = false;
        hasHitTarget = false;
    }
}

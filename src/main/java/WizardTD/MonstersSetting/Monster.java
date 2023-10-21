package WizardTD.MonstersSetting;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.Date;

public class Monster {
    private int monsterX, monsterY;
    public static final int TOPBAR = 40;
    public static final int CELLSIZE = 32;
    private String type;
    private int hp;
    private float speed;
    private double armour;
    private int manaGainedPerKill;
    private int quantity;
    private char[][] map;

    PImage gremlin;
    PImage gremlin1;
    PImage gremlin2;
    PImage gremlin3;
    PImage gremlin4;
    PImage gremlin5;

    public Monster(int monsterX, int monsterY, String type, int hp, float speed, double armour, int manaGainedPerKill, int quantity, PImage gremlin, PImage gremlin1, PImage gremlin2, PImage gremlin3, PImage gremlin4, PImage gremlin5, char[][] map) {
        this.monsterX = monsterX;
        this.monsterY = monsterY;
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.armour = armour;
        this.manaGainedPerKill = manaGainedPerKill;
        this.quantity = quantity;
        this.gremlin = gremlin;
        this.gremlin1 = gremlin1;
        this.gremlin2 = gremlin2;
        this.gremlin3 = gremlin3;
        this.gremlin4 = gremlin4;
        this.gremlin5 = gremlin5;
        this.map = map;
    }

    public String getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public float getSpeed() {
        return speed;
    }

    public double getArmour() {
        return armour;
    }

    public int getManaGainedPerKill() {
        return manaGainedPerKill;
    }

    public int getQuantity() {
        return quantity;
    }

    public PImage getGremlin() {
        return gremlin;
    }

    public PImage getGremlin1() {
        return gremlin1;
    }

    public PImage getGremlin2() {
        return gremlin2;
    }

    public PImage getGremlin3() {
        return gremlin3;
    }

    public PImage getGremlin4() {
        return gremlin4;
    }

    public PImage getGremlin5() {
        return gremlin5;
    }

    public int getMonsterX() {
        return monsterX;
    }

    public int getMonsterY() {
        return monsterY;
    }

    public void display(PApplet app) {
        app.image(gremlin, monsterX, monsterY);
        app.noStroke();
        app.fill(46, 204, 113);
        app.rect(monsterX, monsterY - 6, 23, 4);
    }

    public void move() {
        if (monsterX <= 4 * CELLSIZE) {
            monsterX += getSpeed();
        } else if (monsterY <= 6 * CELLSIZE) {
            monsterY += getSpeed();
        } else if (monsterX < 16 * CELLSIZE) {
            monsterX += getSpeed();
        } else if (monsterY <= 9 * CELLSIZE) {
            monsterY += getSpeed();
        } else if (monsterX <= 20 * CELLSIZE) {
            monsterX -= getSpeed();
        }
    }

}

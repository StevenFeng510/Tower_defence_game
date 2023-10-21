package WizardTD;

import WizardTD.MonstersSetting.Monster;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.List;

public class DefenceTower extends PApplet {
    int x, y;
    boolean isPlaced = false;
    boolean isPreviewed = true;
    int initial_tower_range;
    double initial_tower_firing_speed;
    int initial_tower_damage;
    PImage initialTower;
    List<Monster> targets;
    public static final int CELLSIZE = 32;
    public static final int TOPBAR = 40;

    public DefenceTower(int x, int y, int initial_tower_range, double initial_tower_firing_speed, int initial_tower_damage, PImage initialTower) {
        this.x = x;
        this.y = y;
        this.initial_tower_range = initial_tower_range;
        this.initial_tower_firing_speed = initial_tower_firing_speed;
        this.initial_tower_damage = initial_tower_damage;
        this.initialTower = initialTower;
    }


    void display(PApplet app) {
        // Display defence towers on the map
        int placeX = x / CELLSIZE;
        int placeY = y / CELLSIZE;
        if (isPlaced) {
            if ((x / CELLSIZE) != 0 || (y / CELLSIZE != 0)) {
                app.image(initialTower, placeX * CELLSIZE, (placeY * CELLSIZE));
            }
        }
    }

    void displayPreview(PApplet app, int x, int y) {
        // Show preview of defence towers at mouse position
        if (!isPlaced || isPreviewed) {
            app.image(initialTower, x - 16, y - 16);
            app.stroke(255, 255, 0);
            app.noFill();
            app.ellipse(x, y, 192, 192);
        }
    }

    void place(int x, int y) {
        // Placement of defence towers in designated locations
        this.x = x;
        this.y = y;
        isPlaced = true;
        isPreviewed = false;
    }
}

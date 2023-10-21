package WizardTD;

import WizardTD.MonstersSetting.Monster;
import WizardTD.MonstersSetting.Wave;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;

    public static int WIDTH = CELLSIZE * BOARD_WIDTH + SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH * CELLSIZE + TOPBAR;

    public static final int FPS = 60;
    private int monsterX = 0;
    private int monsterY = 136;
    public String configPath;

    public Random random = new Random();

    private char[][] map;
    private String[] layoutSet;
    // the variables of drawing map
    private PImage grass;
    private PImage shrub;
    private PImage wizardHouse;
    private PImage path0;
    private PImage path1;
    private PImage path2;
    private PImage path3;
    private SidebarButton[] sidebarButtons = new SidebarButton[7];
    private int initial_tower_range;
    private double initial_tower_firing_speed;
    private int initial_tower_damage;
    private int tower_cost;
    private PImage initialTower;
    private DefenceTower defenceTower;
    private int mana_pool_spell_initial_cost;
    private int mana_pool_spell_cost_increase_per_use;
    private float mana_pool_spell_cap_multiplier;
    private int initial_mana;
    private int initialMana;
    private int initial_mana_cap;
    private int initial_mana_gained_per_second;
    private int currentFrameCount;
    private float currentManaBarWeight;
    private ArrayList<DefenceTower> defenceTowers = null;
    private boolean isPaused = false;
    private boolean isFaster = false;
    private float mana_pool_spell_mana_gained_multiplier;
    private JSONArray wavesArray;
    private Monster monster;
    private PImage gremlinMonster;
    private PImage gremlin1;
    private PImage gremlin2;
    private PImage gremlin3;
    private PImage gremlin4;
    private PImage gremlin5;
    private Wave[] waves;
    private ArrayList<Monster> monsters = new ArrayList<Monster>();
    private int currentTime;
    private int lastSpawnTime = 0;
    private int frameCount = 0;

    LinkedList<Monster> waitingMonsters = new LinkedList<>();
    LinkedList<Monster> onScreenMonsters = new LinkedList<>();
    private int monstersQuantity;
    private Monster monsterWave1;

    PVector[] pathPoints;
    private PImage fireball;
    //    private ArrayList<FireBall> fireBalls;
    FireBall fireBall = null;

    // Feel free to add any additional methods or attributes you want. Please put
    // classes in different files.

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the
     * player, enemies and map elements.
     */
    @Override
    public void setup() {
        frameRate(FPS);

        try {
            FileReader reader = new FileReader(configPath);
            JSONObject jsonObject = new JSONObject(reader);
            // Get the layout of the config
            String layout = jsonObject.getString("layout");

            // Get the setting of defence tower
            tower_cost = jsonObject.getInt("tower_cost");
            initial_tower_range = jsonObject.getInt("initial_tower_range");
            initial_tower_firing_speed = jsonObject.getDouble("initial_tower_firing_speed");
            initial_tower_damage = jsonObject.getInt("initial_tower_damage");

            // Get the setting of initial mana
            initial_mana = jsonObject.getInt("initial_mana");
            initial_mana_cap = jsonObject.getInt("initial_mana_cap");
            initial_mana_gained_per_second = jsonObject.getInt("initial_mana_gained_per_second");

            // Get the setting of mana_pool
            mana_pool_spell_initial_cost = jsonObject.getInt("mana_pool_spell_initial_cost");
            mana_pool_spell_cost_increase_per_use = jsonObject.getInt("mana_pool_spell_cost_increase_per_use");
            mana_pool_spell_cap_multiplier = jsonObject.getFloat("mana_pool_spell_cap_multiplier");
            mana_pool_spell_mana_gained_multiplier = jsonObject.getFloat("mana_pool_spell_mana_gained_multiplier");

            // Get the setting of gremlin
            wavesArray = jsonObject.getJSONArray("waves");

            //  JSONArray abc = loadJSONArray(jsonObject.toString());

            // Set an Array of layout
            layoutSet = loadStrings(layout);
            map = new char[layoutSet.length][layoutSet[0].length()];

            for (int i = 0; i < layoutSet.length; i++) {
                for (int j = 0; j < layoutSet[i].length(); j++) {
                    map[i][j] = layoutSet[i].charAt(j);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // Load images during setup
        grass = loadImage("src/main/resources/WizardTD/grass.png");
        shrub = loadImage("src/main/resources/WizardTD/shrub.png");
        wizardHouse = loadImage("src/main/resources/WizardTD/wizard_house.png");
        fireball = loadImage("src/main/resources/WizardTD/fireball.png");

        path0 = loadImage("src/main/resources/WizardTD/path0.png");
        path1 = loadImage("src/main/resources/WizardTD/path1.png");
        path2 = loadImage("src/main/resources/WizardTD/path2.png");
        path3 = loadImage("src/main/resources/WizardTD/path3.png");

        initialTower = loadImage("src/main/resources/WizardTD/tower0.png");
        gremlinMonster = loadImage("src/main/resources/WizardTD/gremlin.png");
        gremlin1 = loadImage("src/main/resources/WizardTD/gremlin1.png");
        gremlin2 = loadImage("src/main/resources/WizardTD/gremlin2.png");
        gremlin3 = loadImage("src/main/resources/WizardTD/gremlin3.png");
        gremlin4 = loadImage("src/main/resources/WizardTD/gremlin4.png");
        gremlin5 = loadImage("src/main/resources/WizardTD/gremlin5.png");

        currentManaBarWeight = 60;

        sidebarButtons[0] = new SidebarButton(645, 50, mouseX, mouseY, "FF", "2x speed");
        sidebarButtons[1] = new SidebarButton(645, 95, mouseX, mouseY, "P", "PAUSE");
        sidebarButtons[2] = new SidebarButton(645, 140, mouseX, mouseY, "T", "Build tower");
        sidebarButtons[3] = new SidebarButton(645, 185, mouseX, mouseY, "U1", "Upgrade\nrange");
        sidebarButtons[4] = new SidebarButton(645, 230, mouseX, mouseY, "U2", "Upgrade\nspeed");
        sidebarButtons[5] = new SidebarButton(645, 275, mouseX, mouseY, "U3", "Upgrade\ndamage");
        sidebarButtons[6] = new SidebarButton(645, 320, mouseX, mouseY, "M", "Mana pool\ncost: " + mana_pool_spell_initial_cost);

        defenceTowers = new ArrayList<DefenceTower>();

//        fireBalls = new ArrayList<FireBall>();

        // Load the settings of waves and monsters
        waves = new Wave[wavesArray.size()];

        for (int i = 0; i < waves.length; i++) {
            JSONObject wavesObject = wavesArray.getJSONObject(i);
            int wavesDuration = wavesObject.getInt("duration");
            float wavePerPause = wavesObject.getFloat("pre_wave_pause");
            JSONArray monstersArray = wavesObject.getJSONArray("monsters");

            for (int j = 0; j < monstersArray.size(); j++) {
                JSONObject monsterObject = monstersArray.getJSONObject(j);
                String type = monsterObject.getString("type");
                int hp = monsterObject.getInt("hp");
                int speed = monsterObject.getInt("speed");
                float armour = monsterObject.getFloat("armour");
                int manaGainedOnKill = monsterObject.getInt("mana_gained_on_kill");
                int quantity = monsterObject.getInt("quantity");

                monsters.add(new Monster(0, 136, type, hp, speed, armour, manaGainedOnKill, quantity, gremlinMonster, gremlin1, gremlin2, gremlin3, gremlin4, gremlin5, map));

            }
            waves[i] = new Wave(wavesDuration, wavePerPause, monsters);
        }

//        pathPoints = new PVector[]{
//                new PVector()
//        }

        monstersQuantity = monsters.get(0).getQuantity();
        // Load the Monster
        for (int k = 0; k < monstersQuantity; k++) {
            Monster monster = new Monster(0, 136, "gremlin", 100, 1, 0.5, 10, 10, gremlinMonster, gremlin1, gremlin2, gremlin3, gremlin4, gremlin5, map);
            waitingMonsters.add(monster);
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed() {
        // The game will run at 2x speed
        if (key == 'F' || key == 'f') {
            sidebarButtons[0].isPressedListener();
            isFaster = !isFaster;
            if (isFaster) {
                this.frameRate(120);
            } else {
                this.frameRate(FPS);
            }
        }

        // Pause the game
        if (key == 'P' || key == 'p') {
            sidebarButtons[1].isPressedListener();
            isPaused = !isPaused;
        }

        // Build a tower
        if (key == 'T' || key == 't') {
            if (initial_mana >= 100) {
                // Instantiate a defenceTower object
                defenceTower = new DefenceTower(mouseX, mouseY, initial_tower_range, initial_tower_firing_speed, initial_tower_damage, initialTower);
                defenceTower.isPlaced = false;
                sidebarButtons[2].isPressedListener();
            }
        }

        // Upgrade the defence Tower
        if (key == '1') {
            sidebarButtons[3].isPressedListener();
        } else if (key == '2') {
            sidebarButtons[4].isPressedListener();
        } else if (key == '3') {
            sidebarButtons[5].isPressedListener();
        }

        // Mana pool spell
        if (key == 'M' || key == 'm') {
            sidebarButtons[6].isPressedListener();
            if (initial_mana > 150) {
                initial_mana -= mana_pool_spell_initial_cost;
                initial_mana_cap = Math.round((float) initial_mana_cap * mana_pool_spell_cap_multiplier);
                mana_pool_spell_initial_cost += mana_pool_spell_cost_increase_per_use;
            }
        }

        // Restart the game
        if (key == 'R' || key == 'r') {
            this.setup();
        }
    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased() {

    }


    @Override
    public void mousePressed(MouseEvent e) {
        // Place a defence tower on the map
        int mouseX = e.getX();
        int mouseY = e.getY();

//        for (int i = 0; i < 6; i++) {
//            sidebarButtons[i].isClickedListener(mouseX, mouseY);
//        }

        if (sidebarButtons[2].isPressed && initial_mana > 100) {
            defenceTower.place(mouseX, mouseY);
            defenceTowers.add(defenceTower);
            sidebarButtons[2].isPressed = false;
            // cost 100 mana
            initial_mana -= tower_cost;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }


    @Override
    public void mouseMoved() {
        super.mouseMoved();
        for (SidebarButton sidebarButton : sidebarButtons) {
            sidebarButton.isHoveredListener(mouseX, mouseY);
        }
    }

    /*
     * @Override
     * public void mouseDragged(MouseEvent e) {
     *
     * }
     */

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        if (!isPaused) {
            background(132, 115, 74, 128);  // #847345a

            // Timer indicator
            textSize(20);
            fill(0);
            text("Wave 1", 10, 30);
            text("starts: " + monstersQuantity, 120, 30);

            // The wizard’s mana bar
            text("MANA: ", 330, 30);
            // Mana bar
            fill(255, 255, 255);
            stroke(0);
            rect(410, 12, 300, 17);
            // Current mana
            fill(0, 214, 214); // 00d6d6
            rect(410, 12, currentManaBarWeight, 17);
            // The value of mana
            fill(0);
            textSize(18);
            text(initial_mana + "/" + initial_mana_cap, 510, 28);

            // Setting the Mana value to change over time
            currentFrameCount++;
            if (currentFrameCount >= 60) {
                if (initial_mana < initial_mana_cap) {
                    initial_mana += initial_mana_gained_per_second;
                    currentManaBarWeight += 0.6;
                }
                currentFrameCount = 0;
            }

            // Initialise the map according to the layout
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    char imgType = map[i][j];
                    switch (imgType) {
                        case ' ':
                            this.image(grass, j * CELLSIZE, i * CELLSIZE + TOPBAR);
                            break;

                        case 'S':
                            this.image(shrub, j * CELLSIZE, i * CELLSIZE + TOPBAR);
                            break;

                        case 'X':
                            // Initialise the path according to the layout
                            if (j != 0) {
                                if (map[i][j - 1] == 'X' && map[i][j + 1] == ' ' && map[i - 1][j] == ' ') {
                                    this.image(path1, j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                } else if ((map[i][j + 1] == ' ' || map[i][j + 1] == 'S') && map[i][j - 1] == 'X' && map[i + 1][j] == 'X' && map[i - 1][j] == 'X') {
                                    this.image(rotateImageByDegrees(path2, 90), j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                } else if (map[i][j - 1] == ' ' && map[i][j + 1] == ' ' || map[i][j + 1] == 'S' && map[i][j - 1] == ' ' || map[i][j - 1] == 'S' && map[i][j + 1] == ' ') {
                                    this.image(rotateImageByDegrees(path0, 90), j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                } else if (map[i][j - 1] == 'X' && map[i][j + 1] == 'X' && map[i + 1][j] == 'X' && map[i - 1][j] == 'X') {
                                    this.image(path3, j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                } else if (map[i][j - 1] == 'X' && map[i][j + 1] == 'X' && map[i + 1][j] == 'X') {
                                    this.image(path2, j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                } else if (map[i][j + 1] == ' ' && map[i + 1][j] == ' ') {
                                    this.image(rotateImageByDegrees(path1, 90), j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                } else if (map[i][j - 1] == ' ' && (map[i - 1][j] == ' ' || map[i - 1][j] == 'S') && map[i + 1][j] == 'X') {
                                    this.image(rotateImageByDegrees(path1, 270), j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                } else if (map[i][j - 1] == 'X' && map[i][j + 1] == 'X' && map[i + 1][j] == ' ' && map[i - 1][j] == 'X') {
                                    this.image(rotateImageByDegrees(path2, 180), j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                } else {
                                    this.image(path0, j * CELLSIZE, i * CELLSIZE + TOPBAR);
                                }
                            } else {
                                this.image(path0, 0, i * CELLSIZE + TOPBAR);
                            }
                            break;
                    }
                }
            }

            // Initialise the wizard house.
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    char imgType = map[i][j];
                    if (imgType == 'W') {
                        this.image(wizardHouse, j * CELLSIZE - 8, (i + 1) * CELLSIZE, 48, 48);
                    }
                }
            }

            // The right sidebar
            for (int i = 0; i < sidebarButtons.length; i++) {
                sidebarButtons[i].draw(this);
            }

            // Show the label of the cost
            if (sidebarButtons[2].isHovered) {
                sidebarButtons[2].showManaCost(this, tower_cost);
            } else if (sidebarButtons[6].isHovered) {
                sidebarButtons[6].showManaCost(this, mana_pool_spell_initial_cost);
            } else if (mana_pool_spell_initial_cost != 1000) {
                sidebarButtons[6].setButtonDescriptionText("Mana pool\ncost: " + mana_pool_spell_initial_cost);
            }

            // Preview the defenceTower when the mouse move
            if (sidebarButtons[2].isPressed) {
                if (mouseX >= 0 && mouseX <= 640 && mouseY >= 40 && mouseY <= 680) {
                    defenceTower.displayPreview(this, mouseX, mouseY);
                }
            }

            // Draw the defenceTowers
            for (DefenceTower defenceTower : defenceTowers) {
                if (defenceTower.isPlaced) {
                    defenceTower.display(this);
                }
            }

            // Draw the monster

//           float spawnInterval = ((float) duration / (float) quantity) * 60;
            frameCount++;
            if (frameCount % 48 == 0 && !waitingMonsters.isEmpty()) {
                Random random = new Random();
                int index = random.nextInt(waitingMonsters.size());
                Monster monster = waitingMonsters.remove(index);
                onScreenMonsters.add(monster);
                monstersQuantity = waitingMonsters.size();
            }
            for (int i = 0; i < onScreenMonsters.size(); i++) {
                monsterWave1 = onScreenMonsters.get(i);
                monsterWave1.display(this);
                monsterWave1.move();
            }

            // Draw the fireBall

            if (fireBall == null || !fireBall.isFiring) {
                // 当火球为空或者火球不在发射状态时
                for (DefenceTower defenceTower : defenceTowers) {
                    if (defenceTower.isPlaced) {
                        for (Monster monster : onScreenMonsters) {
                            if (dist(defenceTower.x, defenceTower.y, monster.getMonsterX(), monster.getMonsterY()) <= defenceTower.initial_tower_range) {
                                if (fireBall == null) {
                                    fireBall = new FireBall(defenceTower.x, defenceTower.y, monster.getMonsterX(), monster.getMonsterY(), 5);
                                    fireBall.isFiring = true;
                                } else if (!fireBall.isFiring) {
                                    fireBall.reset(defenceTower.x, defenceTower.y, monster.getMonsterX(), monster.getMonsterY());
                                    fireBall.isFiring = true;
                                }
                            }
                        }
                    }
                }
            }
            if (fireBall != null) {
                image(fireball, fireBall.x, fireBall.y);
                fireBall.move();
                if (fireBall.hitTargetMonster()) {
                    fireBall.reset(0, 0, 0, 0);
                }
            }

        } else {
            sidebarButtons[1].isPressed = true;
            sidebarButtons[1].draw(this);
        }
    }

    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

    /**
     * Source:
     * https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     *
     * @param pimg  The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, RGB);
        // BufferedImage rotated = new BufferedImage(newWidth, newHeight,
        // BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}

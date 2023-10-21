package WizardTD.MonstersSetting;

import java.util.List;

public class Wave {
    private int duration;
    private float preWavePause;
    private List<Monster> monsters;

    public Wave(int duration, float preWavePause, List<Monster> monsters) {
        this.duration = duration;
        this.preWavePause = preWavePause;
        this.monsters = monsters;
    }

    public int getDuration() {
        return duration;
    }

    public float getPreWavePause() {
        return preWavePause;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }
}

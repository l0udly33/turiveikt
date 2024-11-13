package tankTrouble;

import java.util.Random;

class RapidFireTankStrategy implements TankStrategy {
    private static final Random rand = new Random();

    @Override
    public float getMaxVelocity() {
        return 1.2f; // Higher speed
    }

    @Override
    public float getMaxAcceleration() {
        return 0.3f;
    }

    @Override
    public float getShootingVelocity() {
        return 1.8f;
    }

    @Override
    public float getMaxHealth() {
        return 7f; // Lower health
    }

    @Override
    public int getShootingCooldown() {
        return 750;
    }
}

package tankTrouble;

import java.util.Random;

class HeavyTankStrategy implements TankStrategy {
    private static final Random rand = new Random();

    @Override
    public float getMaxVelocity() {
        return 0.7f; // Higher speed
    }

    @Override
    public float getMaxAcceleration() {
        return 0.1f;
    }

    @Override
    public float getShootingVelocity() {
        return 1.8f;
    }

    @Override
    public float getMaxHealth() {
        return 30f; // Lower health
    }

    @Override
    public int getShootingCooldown() {
        return 3000;
    }
}

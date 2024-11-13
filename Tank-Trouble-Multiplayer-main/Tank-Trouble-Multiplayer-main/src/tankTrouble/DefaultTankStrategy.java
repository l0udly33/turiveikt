package tankTrouble;

import java.util.Random;

class DefaultTankStrategy implements TankStrategy {
    private static final Random rand = new Random();

    @Override
    public float getMaxVelocity() {
        return 1.5f; // Higher speed
    }

    @Override
    public float getMaxAcceleration() {
        return 0.3f;
    }

    @Override
    public float getShootingVelocity() {
        return 2f;
    }

    @Override
    public float getMaxHealth() {
        return 10f; // Lower health
    }

    @Override
    public int getShootingCooldown() {
        return 2000;
    }
}

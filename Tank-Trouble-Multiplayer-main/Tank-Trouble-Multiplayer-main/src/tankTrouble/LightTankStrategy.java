package tankTrouble;

import java.util.Random;

class LightTankStrategy implements TankStrategy {
    private static final Random rand = new Random();

    @Override
    public float getMaxVelocity() {
        return 5f; // Higher speed
    }

    @Override
    public float getMaxAcceleration() {
        return 1f;
    }

    @Override
    public float getShootingVelocity() {
        return 1.8f;
    }

    @Override
    public float getMaxHealth() {
        return 5f; // Lower health
    }

    @Override
    public int getShootingCooldown() {
        return 1250;
    }
}

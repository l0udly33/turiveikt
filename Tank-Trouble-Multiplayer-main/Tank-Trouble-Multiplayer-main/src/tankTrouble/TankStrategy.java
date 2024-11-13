package tankTrouble;

public interface TankStrategy {
    float getMaxVelocity();
    float getMaxAcceleration();
    float getShootingVelocity();
    float getMaxHealth();
    int getShootingCooldown();
}

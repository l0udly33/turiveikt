package tankTrouble;

public class HealthPowerUpFactory extends PowerUpFactory {

    @Override
    public PowerUp createPowerUp(int x, int y) {
        // Create and return a health power-up using the factory method
        return PowerUp.createPowerUp(x, y, "health");
    }
}

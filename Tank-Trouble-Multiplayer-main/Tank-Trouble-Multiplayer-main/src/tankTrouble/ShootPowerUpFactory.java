package tankTrouble;

public class ShootPowerUpFactory extends PowerUpFactory {

    @Override
    public PowerUp createPowerUp(int x, int y) {
        // Create and return a shoot power-up using the factory method
        return PowerUp.createPowerUp(x, y, "shoot");
    }
}

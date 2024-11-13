package tankTrouble;

public class SpeedPowerUpFactory extends PowerUpFactory {

    @Override
    public PowerUp createPowerUp(int x, int y) {
        // Create and return a speed power-up using the factory method
        return PowerUp.createPowerUp(x, y, "speed");
    }
}

package tankTrouble;

import java.util.Random;

public abstract class PowerUpFactory {

    private Random random = new Random();

    public abstract PowerUp createPowerUp(int x, int y);

    // Method to create a random power-up at a specific position
    public PowerUp createRandomPowerUp(int x, int y) {
        // Randomly choose a factory for the power-up
        int choice = random.nextInt(3);  // Three types: health, speed, shoot
        PowerUpFactory factory;

        switch (choice) {
            case 0:
                factory = new HealthPowerUpFactory();
                break;
            case 1:
                factory = new SpeedPowerUpFactory();
                break;
            case 2:
                factory = new ShootPowerUpFactory();
                break;
            default:
                throw new IllegalArgumentException("Unknown power-up type");
        }

        return factory.createPowerUp(x, y);  // Create and return the power-up
    }


}
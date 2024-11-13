package tankTrouble;

import java.awt.Color;
import java.awt.Graphics;

public class PowerUp {
    private int x, y;
    private String type;

    // Constructor is private, so PowerUps can only be created through the PowerUpFactory
    private PowerUp(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void render(Graphics g) {
        if ("speed".equals(type)) {
            g.setColor(Color.BLUE);  // Blue for speed power-up
        } else if ("health".equals(type)) {
            g.setColor(Color.RED);  // Red for health power-up
        } else if ("shoot".equals(type)) {
            g.setColor(Color.GREEN);  // Green for shoot power-up
        }
        g.fillRect(x, y, 20, 20);  // Power-up size 20x20
    }

    public void applyEffect(Tank tank) {
        if ("health".equals(type)) {
            tank.increaseHealth();
        } else if ("speed".equals(type)) {
            tank.increaseSpeed();
        } else if ("shoot".equals(type)) {
            tank.increaseShootVel();
        }
    }

    public boolean checkCollision(Tank tank) {
        int tankX = (int) tank.pos.x;
        int tankY = (int) tank.pos.y;
        int tankWidth = 30;  // Adjust the width as needed
        int tankHeight = 30; // Adjust the height as needed

        return x < tankX + tankWidth && x + 20 > tankX && y < tankY + tankHeight && y + 20 > tankY;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    // Factory Method to create different power-ups
    public static PowerUp createPowerUp(int x, int y, String type) {
        switch (type) {
            case "health":
                return new PowerUp(x, y, "health");
            case "speed":
                return new PowerUp(x, y, "speed");
            case "shoot":
                return new PowerUp(x, y, "shoot");
            default:
                throw new IllegalArgumentException("Unknown power-up type: " + type);
        }
    }
}

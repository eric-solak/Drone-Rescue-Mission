package ca.mcmaster.se2aa4.island.team108;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnergyTest {

    private Energy energy;

    @BeforeEach
    public void initialize() {
        energy = new Energy(1000);
    }

    @Test
    public void subtractPositive() {
        Energy cost = new Energy(10);
        energy = energy.subtract(cost);

        Energy output = new Energy(990);
        assertEquals(output, energy);
    }

    @Test
    public void subtractZero() {
        Energy cost = new Energy(0);
        energy = energy.subtract(cost);

        Energy output = new Energy(1000);
        assertEquals(output, energy);
    }

    @Test
    public void subtractNegative() {
        Energy cost = new Energy(-5);
        energy = energy.subtract(cost);

        Energy output = new Energy(1005);
        assertEquals(output, energy);
    }

}

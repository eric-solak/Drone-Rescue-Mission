package ca.mcmaster.se2aa4.island.team108;

/**
 * Keeps track of the current drone energy/battery
 * @param currentEnergy Current battery level of the drone
 */
public record Energy(int currentEnergy) {

    public Energy subtract(Energy costOfAction) {
        return new Energy(this.currentEnergy - costOfAction.currentEnergy);
    }


}
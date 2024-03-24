package ca.mcmaster.se2aa4.island.team108;

public record Energy(int currentEnergy) {

    public Energy subtract(Energy costOfAction) {
        return new Energy(this.currentEnergy - costOfAction.currentEnergy);
    }


}
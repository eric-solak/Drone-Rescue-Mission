package ca.mcmaster.se2aa4.island.team108;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record Energy(int currentEnergy) {

    public Energy subtract(Energy costOfAction) {
        return new Energy(this.currentEnergy - costOfAction.currentEnergy);
    }


}

# Drone Rescue Mission

- Authors:
  - [Vedant, Patel](patev70@mcmaster.ca)
  - [Tyler, Yue](yuet5@mcmaster.ca)
  - [Eric, Solak](solake@mcmaster.ca)


## Product Description
This project aims to support the rescuing of individuals by (1) locating people in unknown terrain and (2) finding a safe place where a rescue team can be sent close to the people to rescue.

![DroneVisualization-ezgif com-video-to-gif-converter](https://github.com/user-attachments/assets/4ee11c1e-6b89-4215-977a-64d2f520ab8f)

This product is an _exploration command center_ for the [Island](https://ace-design.github.io/island/) serious game.

- The `ca.mcmaster.se2aa4.island.team108.Explorer` class implements the command center, used to compete with the others. (XXX being the team identifier)
- The `Runner` class allows one to run the command center on a specific map.

This project was built using **Java**, **Maven**, and tested with **JUnit**.
  
## How to compile, run and deploy

### Compiling the project:

```
Drone-Rescue-Mission % mvn clean package
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.960 s
[INFO] Finished at: 2024-01-20T18:26:43-05:00
[INFO] ------------------------------------------------------------------------
Drone-Rescue-Mission %
```

### Run the project

The project is not intended to be started by the user, but instead to be part of the competition arena. However, one might one to execute their command center on local files for testing purposes.

To do so, we ask maven to execute the `Runner` class, using a map provided as parameter:

```
Drone-Rescue-Mission % mvn exec:java -q -Dexec.args="./maps/map03.json"
```

It creates three files in the `outputs` directory:

- `_pois.json`: the location of the points of interests
- `Explorer_Island.json`: a transcript of the dialogue between the player and the game engine
- `Explorer.svg`: the map explored by the player, with a fog of war for the tiles that were not visited.

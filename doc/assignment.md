# User interfaces 2021 - Exercise D

The last exercise of the course is an independent programming task. The intention is to apply technologies covered in the course creatively and independently without further help questions and tips.

The topic of the work is probably familiar to everyone: the traditional two-player ship sinking game. More information about the game can be found for example on Wikipedia 
[https://en.wikipedia.org/wiki/Battleship_(game)](https://en.wikipedia.org/wiki/Battleship_(game)). 

The project comes with some license free images that you can use if you wish. The idea of the game may be applied freely within the limits and requirements given in this document. The game can also
themed around a completely different environment than the sea, such as space, and replace ships with Star Trek
style spacecraft and the [LCARS theme](https://en.wikipedia.org/wiki/LCARS) of the user interface.

## Programming language and GUI technology

The game may be programmed using JavaFX on the basis of what has been learned in the weekly exercises, the researched technique (exercise C) or some other technique desired by the group. However, when choosing technology, it is also worth noting that, unfortunately, we are not able to provide guidance or other assistance for all different environments. 

Supported technologies include:
* Java / JavaFX
* C ++ / Qt
* HTML5 / CSS / Javascript
* React

If you plan to use any other technology, you can ask in advance if we can provide support for it.

## Progress of the program

![Flowchart](/doc/ohjelmankulku_EN.png)

After starting the program, the first view will ask for the names of the players and the game settings. Players can decide the size of the game grid and the number of ships. The program should ensure that there are not too many ships in relation to the size of the grid, ie they all fit in the playing area.

Once the settings are in place, players place their ships in the playing area. The game is only played on one machine / program, so another player should look elsewhere when it is not his turn. If desired, the group can also make an online version of the program, in which case the above-mentioned problem does not exist. However, this is not required.

The actual game starts when player 2 has got his ship in place. Players take turns shooting. If the player in turn hits, he is allowed to shoot again. A spacing box is displayed between turns so players can easily switch places without seeing another player's playing field.

If the player does not hit the ship, a miss is recorded in the grid of the player who fired it. The hit is recorded in the grid of both players as well as the sinking of the ship.

The game round continues until all ships of either player have sunk. The program will then ask if you want to play a new round.

Note that the program must remember both the game settings and the game situation when the views change. Only when the new game round starts will the situation be reset.


## Minimum requirements

### Game screen size

The size of the game grid varies depending on the user setting. The minimum allowed grid size is 5 x 5 squares. The maximum size allowed should be 10 x 10 squares. The group can support larger grids if they wish.

### Vessel types, minimum quantities and sizes

There are 5 different ship types in the game. Their amounts vary according to the choice of the players. The number of any ship type can also be set to zero, but the game can only start when at least one ship is being played.

The ship types and numbers below should be able to be added to the 10x10 square game area.

| Number of ships | Type of ship          | Size  |
| :---: | ---           | :---: |
| 1     | carrier | 5     |
| 2     | Battleship | 4     |
| 3     | cruiser    | 3     |
| 4     | submarine  | 3     |
| 5     | destroyer     | 2     |


The interface checks the number of ships allowed so that the area of ​​the grid (RA) should be twice the total area of ​​the ships (AA). For example, above the area of ​​the 10x10 grid `RA = 10 * 10 = 100` and the area of ​​the ships` AA = 1 * 5 + 2 * 4 + 3 * 3 + 4 * 3 + 5 * 2 = 44`. And this gives `100> = 2 * 44` i.e.` RA> = 2 * AA` i.e. settings are allowed.

### Functional requirements

* Game start and end options (you want to end the game at any time)
* Players' names are asked at the beginning of the game
* The size of the game grid is asked after the names
* After the size of the game grid, the number of ships is queried and the value is compared to the size of the grid (`RA> = 2 * AA`)
 (It can be proved mathematically from the sum rule of the above field that ships can always fit in a grid in some arrangement)
  * If the player fails to place all the ships on the grid, the placing can be started from the beginning.
* Ships are placed by drag'n'drop
* When placing the tray, it can be rotated with the `r` key on the keyboard
  * The group can also add ships using the arrow keys and Enter
* Shooting is done by clicking the desired target box with the mouse
  * The group can also use the arrow keys and Enter to shoot
* Ships should be presented as either images or geometric patterns
* Game area drawn according to the size of a dynamically selected grid
  (graphics should not be read as a single image from a file)

### Usability

* Apply to the implementation of the program at least in each of the listed functional requirements of good
  usability principles and design models

### Documentation

* Briefly document code comments at the category / method level, whichever part
  in your solution does.
* Also document the construction and use of the program, if you use it
  technologies other than Java / JavaFX / Maven.

## Evaluation criteria

The exercise is graded using a scale from 0 to 24, based on how
well each of the functional and other goals have been achieved.
By default, all the members of the group receive the same grading.
The deadline for submitting the work is the 14th of March.

### Contributions

Document in a short report, for example, in a README file, the names and
and student numbers of the group members, and the contributions of
each member.

### 1. Compliance with requirements

* Are the minimum requirements for assignment met?
* Is the documentation adequate and understandable?


### 2. Usability
* is the program easy to learn (e.g. what kind of clues does it give to the user, how consistent is it)?
* is the program efficient to use (eg is navigation between functions easy)?
* is the visibility of the program good (is its current internal state easy for the user to understand, is it clear how to reach the next function or desired goal)?
* does the program provide the user with enough informative feedback?
* errors: is the program forgiving of user errors?
* Satisfaction: Is the program easy and effortless to use?


### 3. User experience
* Have design templates been used?
* Is the program visually / aesthetically pleasing?
* Are different user groups somehow taken into account?
* Is the game fun to play?

## Features that don't need to be implemented

The following are ignored in the evaluation

* A computer player and artificial intelligence for it
* Web traffic (if you do web technologies, a purely browser-based SPA will suffice!)
* 3D graphics

However, the following will be taken into account in the evaluation as bonus points

* Programming sound effects
* Animations
* Decorate UI components with textures and your own theme

# Java Yahtzee

## Rundown
When the driver is run, a game of Yahtzee can be started fresh or loaded from a file. If a new game is started, you can choose to add as many players or bots to the game before starting. The game is played in turns, with each player or bot going once in a full turn. At the end of a turn, the game can be saved to a file where it can be loaded from the main menu.

## A-ha! moments
1. After making Die its own class, I was having issues with them not changing their value when they were rolled. This was because although I was returning a random number in the roll() method, I forgot that the dice's value was now stored in a variable where it was later retrieved by the getValue() method.
2. Originally, scores were kept in arrays in the main Yahtzee class, with unscored indices marked as -1. This worked, but was difficult to expand upon. I decided to make the Scorecard class, in order to allow for more than one player, bots, and also to move hundreds of lines of scoring code out of static methods in the main file.
3. Creating GameDriver allowed another layer of control over the game, making it much easier to save and load.

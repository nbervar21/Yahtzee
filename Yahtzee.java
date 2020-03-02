import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

class Yahtzee
{
    Scanner kb;
    public Yahtzee(Scanner input)
    {
        kb = input;
    }
    // called when a fresh game is started
    public void play()
    {
        // add players to the game
        kb.nextLine();
        System.out.println("Adding players, leave blank when finished");
        List<Scorecard> players = new ArrayList<Scorecard>();
        String scorer = "";
        do
        {
            System.out.print("Add player: ");
            scorer = kb.nextLine();
            if (!scorer.equals(""))
            {
                System.out.print("Should " + scorer + " be a CPU player? [Y/N]: ");
                String confirm = "";
                while (!(confirm.equals("y") || confirm.equals("n")))
                {
                    confirm = kb.nextLine().toLowerCase();
                }
                boolean isBot = confirm.equals("y");
                System.out.println("Added " + scorer + " (" + (isBot ? coloredText("BOT", 6) : coloredText("HUMAN", 3)) + ")");
                players.add(new Scorecard(scorer, isBot));
            }
        }
        while (!scorer.equals("") || players.isEmpty());
        play(players);
    }
    // called when loading a game from a file
    public void play(String loadFileName)
    {
        Scanner readSave;
        try
        {
            File loadFile = new File(loadFileName);
            readSave = new Scanner(loadFile);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Can't find file " + loadFileName + "");
            return;
        }
        System.out.print("Loading");
        List<Scorecard> players = new ArrayList<Scorecard>();
        int index = 1;
        while (readSave.hasNextLine())
        {
            String line = readSave.nextLine();
            if (!line.startsWith("#") && !line.startsWith("//"))
            {
                if (index % 2 == 0)
                {
                    players.get(players.size() - 1).loadScoreString(line);
                }
                else
                {
                    String[] plyData = line.split(" ", 2);
                    players.add(new Scorecard(plyData[1], plyData[0].equals("BOT")));
                }
                index++;
            }
        }
        System.out.println("Loaded!");
        play(players);
    }
    // save the game
    public void save(List<Scorecard> players)
    {
        String saveFileName;
        try
        {
            File saveFile;
            do
            {
                System.out.print("Save to file: ");
                saveFileName = kb.nextLine() + ".yz";
                saveFile = new File(saveFileName);
            }
            while (!saveFile.createNewFile());
            System.out.print("Saving...");
            FileWriter saveFileWriter = new FileWriter(saveFileName);
            for (Scorecard scorecard : players)
            {
                saveFileWriter.write(scorecard.saveString());
            }
            saveFileWriter.close();
            System.out.println(" Saved to " + saveFileName + "!");
        }
        catch (IOException e)
        {
            System.out.println("There was a problem saving the game :(");
        }
    }
    // called when we have our player list already
    public void play(List<Scorecard> players)
    {
        YahtzeeAI ai = new YahtzeeAI();
        Die[] dice = {new Die(), new Die(), new Die(), new Die(), new Die()};
        // start the main game loop
        boolean savePending = false;
        boolean gameOver = false;
        while (!gameOver)
        {
            if (savePending)
            {
                save(players);
                return;
            }
            for (int player = 0; player < players.size(); player++)
            {
                Scorecard scorecard = players.get(player);
                int rollsLeft = 2;
                while (true)
                {
                    System.out.println(scorecard.getString(dice) + "\n" + combineDice(dice, false) + "\n" + combineDice(dice, true) + "\n\n[0]   Quit\n[1-5] Hold\n[6]   Roll (" + rollsLeft + " left)\n[7]   Score\n" + (savePending ? "(save pending)" : "[8]   Save"));
                    int input = -1;
                    while (input < 0 || input > (savePending ? 7 : 8))
                    {
                        System.out.print("Enter a number: ");
                        if (scorecard.belongsToBot())
                        {
                            input = ai.rollOrHold(rollsLeft, scorecard, dice);
                            ai.sleep();
                            System.out.print(input);
                            ai.sleep();
                            System.out.print("\n");
                        }
                        else
                        {
                            try
                            {
                                input = kb.nextInt();
                            }
                            catch (InputMismatchException e)
                            {
                                kb.next();
                            }
                            if (input == 6 && rollsLeft == 0)
                            {
                                System.out.println("You are out of rolls!");
                                input = -1;
                            }
                        }
                    }
                    if (input == 0)
                    {
                        return;
                    }
                    else if (input < 6)
                    {
                        dice[input - 1].toggleHeld();
                    }
                    else if (input == 6)
                    {
                        for (Die die : dice)
                        {
                            if (!die.isHeld())
                            {
                                die.roll();
                                System.out.println("Rolled a " + die.getValue());
                            }
                        }
                        rollsLeft--;
                    }
                    else if (input == 7)
                    {
                        System.out.println("[0] Cancel");
                        for (int i = 0; i < 13; i++)
                        {
                            if (!scorecard.isScored(i))
                            {
                                System.out.println("[" + (i + 1) + "] " + scorecard.getScoreName(i));
                            }
                        }
                        int scoreIndex = -2;
                        while (scoreIndex < -1 || scoreIndex > 12 || (scoreIndex > -1 && scorecard.isScored(scoreIndex)))
                        {
                            System.out.print("Score as: ");
                            if (scorecard.belongsToBot())
                            {
                                scoreIndex = ai.whatToScore(scorecard, dice);
                                ai.sleep();
                                System.out.print(scoreIndex + 1);
                                ai.sleep();
                                System.out.print("\n");
                            }
                            else
                            {
                                try
                                {
                                    scoreIndex = kb.nextInt() - 1;
                                }
                                catch (InputMismatchException e)
                                {
                                    kb.next();
                                }
                            }
                        }
                        if (scoreIndex > -1)
                        {
                            System.out.print("Score as " + scorecard.getScoreName(scoreIndex) + " for " + scorecard.getScores(dice)[scoreIndex] + " points?" + " (Y/N) ");
                            String confirm = "";
                            if (scorecard.belongsToBot())
                            {
                                confirm = "y";
                                ai.sleep();
                                System.out.print(confirm);
                                ai.sleep();
                                System.out.print("\n");
                            }
                            else
                            {
                                kb.nextLine();
                                while (!(confirm.equals("y") || confirm.equals("n")))
                                {
                                    confirm = kb.nextLine().toLowerCase();
                                }
                            }
                            if (confirm.equals("y"))
                            {
                                scorecard.score(scoreIndex, dice);
                                for (Die die : dice)
                                {
                                    if (die.isHeld())
                                    {
                                        die.toggleHeld();
                                    }
                                    die.roll();
                                }
                                if (players.lastIndexOf(scorecard) == players.size() - 1)
                                {
                                    gameOver = true;
                                    for (int i = 0; i < 13; i++)
                                    {
                                        if (!scorecard.isScored(i))
                                        {
                                            gameOver = false;
                                        }
                                    }
                                    if (gameOver)
                                    {
                                        System.out.println("\nFinal Score");
                                        for (Scorecard card : players)
                                        {
                                            System.out.println(card.getScorer() + ": " + card.getTotal());
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    else
                    {
                        savePending = true;
                        System.out.println("Game will be saved after this round");
                    }
                }
            }
        }
        System.out.println("Thanks for playing!\n");
    }
    static String combineDice(Die[] dice, boolean heldOnly)
    {
        String[] diceRows = {"", "", "", "", ""};
        for (Die die : dice)
        {
            String[] newDieRows = die.getString(heldOnly).split("\n");
            for (int i = 0; i < 5; i++)
            {
                diceRows[i] = String.join("    ", diceRows[i], newDieRows[i]);
            }
        }
        return String.join("\n", diceRows);
    }
    static String coloredText(String text, int color)
    {
        // special escape characters
        // 0 - black
        // 1 - red
        // 2 - green
        // 3 - yellow
        // 4 - blue
        // 5 - magenta
        // 6 - cyan
        // 7 - white
        return "\u001b[3" + color + "m" + text + "\u001b[0m";
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

class Yahtzee
{
    Scanner kb;
    public Yahtzee(Scanner input)
    {
        kb = input;
    }
    public void play()
    {
        YahtzeeAI ai = new YahtzeeAI();
        Die[] dice = {new Die(), new Die(), new Die(), new Die(), new Die()};
        // add players to the game
        kb.nextLine();
        System.out.println("Adding players, leave blank when finished");
        List<Scorecard> players = new ArrayList<Scorecard>();
        List<Boolean> bots = new ArrayList<Boolean>();
        String scorer = "";
        do
        {
            System.out.print("Add player: ");
            scorer = kb.nextLine();
            if (!scorer.equals(""))
            {
                System.out.print("CPU player? [Y/N]: ");
                String confirm = "";
                while (!(confirm.equals("y") || confirm.equals("n")))
                {
                    confirm = kb.nextLine().toLowerCase();
                }
                boolean isBot = confirm.equals("y");
                System.out.println("Added " + scorer + " (" + (isBot ? coloredText("BOT", 6) : "") + ")");
                players.add(new Scorecard(scorer));
                bots.add(isBot);
            }
        }
        while (!scorer.equals("") || players.isEmpty());
        // start the main game loop
        boolean gameOver = false;
        while (!gameOver)
        {
            for (int player = 0; player < players.size(); player++)
            {
                Scorecard scorecard = players.get(player);
                int rollsLeft = 2;
                while (true)
                {
                    System.out.println(scorecard.getString(dice) + "\n" + combineDice(dice, false) + "\n" + combineDice(dice, true) + "\n\n[0]   Quit\n[1-5] Hold\n[6]   Roll (" + rollsLeft + ")\n[7]   Score");
                    int input = -1;
                    while (input < 0 || input > 7)
                    {
                        System.out.print("Enter a number: ");
                        if (bots.get(player))
                        {
                            input = ai.rollOrHold(rollsLeft, scorecard, dice);
                            ai.sleep();
                            System.out.println(input);
                            ai.sleep();
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
                        if (rollsLeft > 0)
                        {
                            for (Die die : dice)
                            {
                                if (!die.isHeld())
                                {
                                    die.roll();
                                }
                            }
                            rollsLeft--;
                        }
                        else
                        {
                            System.out.println("You are out of rolls!");
                        }
                    }
                    else
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
                            if (bots.get(player))
                            {
                                scoreIndex = ai.whatToScore(scorecard, dice);
                                ai.sleep();
                                System.out.println(scoreIndex + 1);
                                ai.sleep();
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
                            if (bots.get(player))
                            {
                                confirm = "y";
                                ai.sleep();
                                System.out.println(confirm);
                                ai.sleep();
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
                                resetDice(dice);
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
                }
            }
        }
        System.out.println("Thanks for playing!");
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
    static void resetDice(Die[] dice)
    {
        for (Die die : dice)
        {
            if (die.isHeld())
            {
                die.toggleHeld();
            }
            die.roll();
        }
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
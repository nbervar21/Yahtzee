import java.util.Scanner;
import java.util.InputMismatchException;

class Yahtzee
{
    public static void main(String[] args)
    {
        Scanner kb = new Scanner(System.in);
        YahtzeeDie[] dice = {new YahtzeeDie(), new YahtzeeDie(), new YahtzeeDie(), new YahtzeeDie(), new YahtzeeDie()};
        int[] scorecard = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        boolean gameOver = false;
        while (!gameOver)
        {
            System.out.println("\nScorecard\n" + formatScores(getScores(dice, scorecard), scorecard) + "\n" + comboDice(dice, false) + "\n" + comboDice(dice, true) + "\n\n[0]   Quit\n[1-5] Hold\n[6]   Roll\n[7]   Score");
            int input = -1;
            while (input < 0 || input > 7)
            {
                System.out.print("Enter a number: ");
                try
                {
                    input = kb.nextInt();
                }
                catch (InputMismatchException e)
                {
                    kb.next();
                }
            }
            if (input == 0)
            {
                gameOver = true;
            }
            else if (input < 6)
            {
                dice[input - 1].toggleHeld();
            }
            else if (input == 6)
            {
                for (YahtzeeDie die : dice)
                {
                    if (!die.isHeld())
                    {
                        die.roll();
                    }
                }
            }
            else
            {
                for (int i = 0; i < 13; i++)
                {
                    if (scorecard[i] == -1)
                    {
                        System.out.print("\n[" + (i + 1) + "] " + getScoreName(i));
                    }
                }
                int scoreIndex = -2;
                while (scoreIndex < -1 || scoreIndex > 12 || scorecard[scoreIndex] != -1)
                {
                    System.out.print("\nScore as: ");
                    try
                    {
                        scoreIndex = kb.nextInt() - 1;
                    }
                    catch (InputMismatchException e)
                    {
                        kb.next();
                    }
                }
                if (scoreIndex > -1)
                {
                    scorecard[scoreIndex] = getScores(dice, scorecard)[scoreIndex];
                    resetDice(dice);
                    gameOver = true;
                    for (int score : scorecard)
                    {
                        if (score == -1)
                        {
                            gameOver = false;
                        }
                    }
                    if (gameOver)
                    {
                        System.out.println("\nFinal Score\n" + formatScores(getScores(dice, scorecard), scorecard));
                    }
                }
            }
        }
        System.out.println("Thanks for playing!");
    }
    static String comboDice(YahtzeeDie[] dice, boolean heldOnly)
    {
        String[] diceRows = {"", "", "", "", ""};
        for (YahtzeeDie die : dice)
        {
            String[] newDieRows = die.getString(heldOnly).split("\n");
            for (int i = 0; i < 5; i++)
            {
                diceRows[i] = String.join("    ", diceRows[i], newDieRows[i]);
            }
        }
        return String.join("\n", diceRows);
    }
    static int[] getScores(YahtzeeDie[] dice, int[] scorecard)
    {
        int[] scores = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int sum = 0;
        int[] count = {0, 0, 0, 0, 0, 0};
        for (YahtzeeDie die : dice)
        {
            int val = die.getValue();
            sum += val;
            count[val - 1]++;
        }
        // how many different numbers we have
        // yahtzee would be 1
        // full house or 4 of a kind would be 2
        // big straights are 5
        int diffNums = 0;
        for (int num : count)
        {
            if (num > 0)
            {
                diffNums++;
                if (num > 2)
                {
                    // 3 of a kind
                    scores[6] = sum;
                    if (num > 3)
                    {
                        // 4 of a kind
                        scores[7] = sum;
                        if (num > 4)
                        {
                            // yahtzee, baby!
                            scores[11] = 50;
                        }
                    }
                }
            }
        }
        // full house
        if (scores[6] > 0 && scores[7] == 0 && diffNums == 2)
        {
            scores[8] = 25;
        }
        // aces through sixes
        for (int i = 0; i < 6; i++)
        {
            scores[i] = count[i] * (i + 1);
        }
        // little straight
        if (diffNums > 3 && (count[2] > 0 && count[3] > 0) && ((count[1] > 0 && count[0] > 0) || (count[1] > 0 && count[4] > 0) || (count[4] > 0 && count[5] > 0)))
        {
            scores[9] = 30;
            // big straight
            if (diffNums == 5 && (count[0] == 0 || count[5] == 0))
            {
                // if we dont have a 1 or 6
                // we def have big straight
                scores[10] = 40;
            }
        }
        // chance
        scores[12] = sum;
        // replace scores with already scored ones
        for (int i = 0; i < 13; i++)
        {
            if (scorecard[i] != -1)
            {
                scores[i] = scorecard[i];
            }
        }
        return scores;
    }
    static String getScoreName(int index)
    {
        String[] scoreNames = {"Aces", "Deuces", "Threes", "Fours", "Fives", "Sixes", "3 of a Kind", "4 of a Kind", "Full House", "Sm. Straight", "Lg. Straight", "Yahtzee", "Chance"};
        return scoreNames[index];
    }
    static String formatScores(int[] scores, int[] scorecard)
    {
        int total = 0;
        StringBuilder formattedScores = new StringBuilder("");
        for (int i = 0; i < scores.length; i++)
        {
            formattedScores.append("[" + (scorecard[i] == -1 ? " " : "X") + "] " + getScoreName(i) + ": " + scores[i] + "\n");
            if (scorecard[i] != -1)
            {
                total += scorecard[i];
            }
        }
        boolean gotBonus = (scorecard[0] + scorecard[1] + scorecard[2] + scorecard[3] + scorecard[4] + scorecard[5] >= 63);
        if (gotBonus)
        {
            total += 35;
        }
        return formattedScores.toString() + "    Total: " + total + (gotBonus ? " (Bonus +35)" : "") + "\n";
    }
    static void resetDice(YahtzeeDie[] dice)
    {
        for (YahtzeeDie die : dice)
        {
            if (die.isHeld())
            {
                die.toggleHeld();
            }
            die.roll();
        }
    }
}
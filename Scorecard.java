import java.util.Scanner;

class Scorecard
{
    static final int HANDS = 13;
    String scorer;
    boolean isBot = false;
    // there's gotta be a better way to do this
    int[] scores = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    boolean[] scored = {false, false, false, false, false, false, false, false, false, false, false, false, false};
    public Scorecard()
    {
        scorer = "Player";
    }
    public Scorecard(String name)
    {
        scorer = name;
    }
    public Scorecard(String name, boolean bot)
    {
        scorer = name;
        isBot = bot;
    }
    public boolean belongsToBot()
    {
        return isBot;
    }
    public String getScorer()
    {
        return scorer;
    }
    public boolean score(int index, Die[] dice)
    {
        return score(index, getScores(dice)[index]);
    }
    public boolean score(int index, int score)
    {
        if (scored[index])
        {
            return false;
        }
        else
        {
            scores[index] = score;
            scored[index] = true;
            return true;
        }
    }
    public boolean isScored(int index)
    {
        return scored[index];
    }
    public int[] getScores()
    {
        return scores;
    }
    public String saveString()
    {
        StringBuilder scoreString = new StringBuilder((isBot ? "BOT " : "HUM ") + scorer + "\n");
        for (int i = 0; i < HANDS; i++)
        {
            scoreString.append(" ");
            if (scored[i])
            {
                scoreString.append(scores[i]);
            }
            else
            {
                scoreString.append("-1");
            }
        }
        return scoreString.toString() + "\n";
    }
    public boolean loadScoreString(String loadString)
    {
        Scanner load = new Scanner(loadString);
        for (int i = 0; i < HANDS; i++)
        {
            if (!load.hasNextInt())
            {
                System.out.println("LOADING ERROR: Not enough scores!");
                return false;
            }
            scores[i] = load.nextInt();
            if (scores[i] < 0)
            {
                scored[i] = false;
                scores[i] = 0;
            }
            else
            {
                scored[i] = true;
            }
            System.out.print(".");
        }
        return true;
    }
    public int[] getScores(Die[] dice)
    {
        int[] newScores = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int sum = 0;
        int[] count = {0, 0, 0, 0, 0, 0};
        for (Die die : dice)
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
                    newScores[6] = sum;
                    if (num > 3)
                    {
                        // 4 of a kind
                        newScores[7] = sum;
                        if (num > 4)
                        {
                            // yahtzee, baby!
                            newScores[11] = 50;
                        }
                    }
                }
            }
        }
        // full house
        if (newScores[6] > 0 && newScores[7] == 0 && diffNums == 2)
        {
            newScores[8] = 25;
        }
        // aces through sixes
        for (int i = 0; i < 6; i++)
        {
            newScores[i] = count[i] * (i + 1);
        }
        // little straight
        if (diffNums > 3 && (count[2] > 0 && count[3] > 0) && ((count[1] > 0 && count[0] > 0) || (count[1] > 0 && count[4] > 0) || (count[4] > 0 && count[5] > 0)))
        {
            newScores[9] = 30;
            // big straight
            if (diffNums == 5 && (count[0] == 0 || count[5] == 0))
            {
                // if we dont have a 1 or 6
                // we def have big straight
                newScores[10] = 40;
            }
        }
        // chance
        newScores[12] = sum;
        // replace scores with already scored ones
        for (int i = 0; i < HANDS; i++)
        {
            if (scored[i])
            {
                newScores[i] = scores[i];
            }
        }
        return newScores;
    }
    public int getTotal()
    {
        int total = 0;
        for (int i = 0; i < HANDS; i++)
        {
            if (isScored(i))
            {
                total += scores[i];
            }
        }
        if (scores[0] + scores[1] + scores[2] + scores[3] + scores[4] + scores[5] >= 63)
        {
            total += 35;
        }
        return total;
    }
    public String getString()
    {
        int[] newScores = getScores();
        int total = 0;
        StringBuilder formattedScores = new StringBuilder(scorer + "'s Scores\n");
        for (int i = 0; i < newScores.length; i++)
        {
            formattedScores.append("[" + (isScored(i) ? "X" : " ") + "] " + getScoreName(i) + ": " + newScores[i] + "\n");
            if (isScored(i))
            {
                total += scores[i];
            }
        }
        boolean gotBonus = (scores[0] + scores[1] + scores[2] + scores[3] + scores[4] + scores[5] >= 63);
        if (gotBonus)
        {
            total += 35;
        }
        return formattedScores.toString() + "    Total: " + total + (gotBonus ? " (Bonus +35)" : "") + "\n";
    }
    public String getString(Die[] dice)
    {
        int[] newScores = getScores(dice);
        int total = 0;
        StringBuilder formattedScores = new StringBuilder(scorer + "'s Scores\n");
        for (int i = 0; i < newScores.length; i++)
        {
            formattedScores.append("[" + (isScored(i) ? "X" : " ") + "] " + getScoreName(i) + ": " + newScores[i] + "\n");
            if (isScored(i))
            {
                total += scores[i];
            }
        }
        boolean gotBonus = (scores[0] + scores[1] + scores[2] + scores[3] + scores[4] + scores[5] >= 63);
        if (gotBonus)
        {
            total += 35;
        }
        return formattedScores.toString() + "    Total: " + total + (gotBonus ? " (Bonus +35)" : "") + "\n";
    }
    public String getScoreName(int index)
    {
        String[] scoreNames = {"Aces", "Deuces", "Threes", "Fours", "Fives", "Sixes", "3 of a Kind", "4 of a Kind", "Full House", "Sm. Straight", "Lg. Straight", "Yahtzee", "Chance"};
        return scoreNames[index];
    }
}
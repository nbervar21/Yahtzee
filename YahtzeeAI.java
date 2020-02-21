class YahtzeeAI
{
    int difficulty;
    public YahtzeeAI()
    {
        difficulty = 1;
    }
    public YahtzeeAI(int d)
    {
        difficulty = d;
    }
    private int highestScoringIndex(int[] scores)
    {
        int index = 0;
        int highest = 0;
        for (int i = 0; i < 13; i++)
        {
            if (scores[i] > highest)
            {
                index = i;
            }
            highest = scores[index];
        }
        return index;
    }
    private int lowestUnscoredIndex(Scorecard scorecard)
    {
        for (int i = 0; i < 13; i++)
        {
            if (!scorecard.isScored(i))
            {
                return i;
            }
        }
        return -1;
    }
    public boolean[] diceToHold(Scorecard scorecard, Die[] dice)
    {
        boolean[] shouldHold = {false, false, false, false, false};
        int[] count = {0, 0, 0, 0, 0, 0};
        for (Die die : dice)
        {
            count[die.getValue() - 1]++;
        }
        int targetScoreIndex = lowestUnscoredIndex(scorecard);
        if (targetScoreIndex >= 0 && targetScoreIndex <= 5)
        {
            // if we're going for a number:
            // hold all of that number
            for (int i = 0; i < 5; i++)
            {
                shouldHold[i] = dice[i].getValue() == (targetScoreIndex + 1);
            }
        }
        else if (targetScoreIndex == 6 || targetScoreIndex == 7)
        {
            // if we're going for many of a kind:
            // hold values that we have more than 2 of
            for (int i = 0; i < 5; i++)
            {
                shouldHold[i] = count[dice[i].getValue()] > 2;
            }
        }
        return shouldHold;
    }
    public int rollOrHold(int rollsLeft, Scorecard scorecard, Die[] dice)
    {
        if (rollsLeft == 0)
        {
            return 7;
        }
        boolean[] ideal = diceToHold(scorecard, dice);
        for (int i = 0; i < 5; i++)
        {
            if (dice[i].isHeld() != ideal[i])
            {
                return i + 1;
            }
        }
        return 6;
    }
    public int whatToScore(Scorecard scorecard, Die[] dice)
    {
        int[] scores = scorecard.getScores(dice);
        for (int i = 0; i < 13; i++)
        {
            if (scorecard.isScored(i))
            {
                scores[i] = -1;
            }
        }
        return highestScoringIndex(scores);
    }
}
class Die
{
    private int value;
    private boolean held;
    public Die()
    {
        held = false;
        roll();
    }
    public int getValue()
    {
        return value;
    }
    public int roll()
    {
        value = ((int) (Math.random() * 6)) + 1;
        return value;
    }
    public int roll(int weight)
    {
        if (weight < 2)
        {
            return roll();
        }
        else
        {
            int max = 0;
            for (int i = 0; i < weight; i++)
            {
                int cur = roll();
                if (cur > max)
                {
                    max = cur;
                }
            }
            value = max;
            return max;
        }
    }
    public boolean isHeld()
    {
        return held;
    }
    public void toggleHeld()
    {
        held = !held;
    }
    public String getString(boolean heldOnly)
    {
        if (held != heldOnly)
        {
            return "     \n     \n     \n     \n     ";
        }
        int[][] diceDots = {{14}, {9, 19}, {9, 14, 19}, {7, 9, 19, 21}, {7, 9, 14, 19, 21}, {7, 9, 13, 15, 19, 21}};
        StringBuilder die = new StringBuilder("+---+\n|   |\n|   |\n|   |\n+---+");
        for (int dotIndex : diceDots[value - 1])
        {
            die.setCharAt(dotIndex, 'o');
        }
        return die.toString();
    }
}

import java.util.Scanner;
import java.util.InputMismatchException;

class GameDriver
{
    public static void main(String[] args)
    {
        Scanner kb = new Scanner(System.in);
        Yahtzee yahtzee = new Yahtzee(kb);
        System.out.println("\n   ._______.    ______\n  /   o   /|   /\\     \\      __   _____   _   _ _____ ______ _____ _____\n /_______/o|  /o \\  o  \\     \\ \\ / / _ \\ | | | |_   _|___  /|  ___|  ___|\n | o     | | /   o\\_____\\     \\ V / /_\\ \\| |_| | | |    / / | |__ | |__\n |   o   |o/ \\o   /o    /      \\ /|  _  ||  _  | | |   / /  |  __||  __|\n |     o |/   \\ o/  o  /       | || | | || | | | | | ./ /___| |___| |___\n '-------'     \\/____o/        \\_/\\_| |_/\\_| |_/ \\_/ \\_____/\\____/\\____/\n\n");
        while (true)
        {
            System.out.println("Menu\n[0] Quit\n[1] New Game\n[2] Load Game");
            int input = -1;
            while (input < 0 || input > 2)
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
                break;
            }
            else if (input == 1)
            {
                yahtzee.play();
            }
            else if (input == 2)
            {
                System.out.println("AYYYY");
            }
        }
        System.out.println("Have a nice day!");
    }
}
import java.util.Scanner;
import java.util.InputMismatchException;

class GameDriver
{
    public static void main(String[] args)
    {
        Scanner kb = new Scanner(System.in);
        Yahtzee yahtzee = new Yahtzee(kb);
        //   ._______.    ______
        //  /   o   /|   /\     \      __   _____   _   _ _____ ______ _____ _____
        // /_______/o|  /o \  o  \     \ \ / / _ \ | | | |_   _|___  /|  ___|  ___|
        // | o     | | /   o\_____\     \ V / /_\ \| |_| | | |    / / | |__ | |__
        // |   o   |o/ \o   /o    /      \ /|  _  ||  _  | | |   / /  |  __||  __|
        // |     o |/   \ o/  o  /       | || | | || | | | | | ./ /___| |___| |___
        // '-------'     \/____o/        \_/\_| |_/\_| |_/ \_/ \_____/\____/\____/
        System.out.println("\n   ._______.    ______\n  /   o   /|   /\\     \\      __   _____   _   _ _____ ______ _____ _____\n /_______/o|  /o \\  o  \\     \\ \\ / / _ \\ | | | |_   _|___  /|  ___|  ___|\n | o     | | /   o\\_____\\     \\ V / /_\\ \\| |_| | | |    / / | |__ | |__\n |   o   |o/ \\o   /o    /      \\ /|  _  ||  _  | | |   / /  |  __||  __|\n |     o |/   \\ o/  o  /       | || | | || | | | | | ./ /___| |___| |___\n '-------'     \\/____o/        \\_/\\_| |_/\\_| |_/ \\_/ \\_____/\\____/\\____/\n\n");
        while (true)
        {
            System.out.println("Menu\n[0] Quit\n[1] New Game\n[2] Load Game\n[3] How to Play");
            int input = -1;
            while (input < 0 || input > 3)
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
                kb.nextLine();
                System.out.print("Load file: ");
                String loadFileName = kb.nextLine() + ".yz";
                yahtzee.play(loadFileName);
            }
            else if (input == 3)
            {
                System.out.println("\n-= How to play =-\n\nYahtzee is like five-card poker, except with dice.\nYou have up to three rolls, and can choose which dice to roll or not roll at any time.\nWhen you have a satisfactory hand, you can choose to score it.\nKeep in mind that you can only score each type of hand once.\n");
            }
        }
        System.out.println("Have a nice day!");
    }
}
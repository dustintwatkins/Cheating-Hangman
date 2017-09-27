package hangman;

import java.io.File;

public class Main{

    public static void main(String[] args){
    	String inputDict = null;
    	int wordLength = 0;
    	int guesses = 0;
    	if(args.length != 3)
    	{
    		System.out.println("Incorrect number of arguments!");
    	}
    	else
    	{
	    	inputDict = args[0];
	    	String inputLength = args[1];
	    	String inputGuesses = args[2];
	    	
	    	wordLength = verifyLength(Integer.parseInt(inputLength));
	    	guesses = verifyGuessNum(Integer.parseInt(inputGuesses));	    	
    	}
    	evilHangmanGame game = new evilHangmanGame();
    	game.startGame(new File(inputDict), wordLength);
    	game.playGame(guesses);
    }//end main method()
    
    public static int verifyLength(int l)
    {
    	if(l < 2)
    	{
    		while(l < 2)
    		{
    			System.out.println("Please input a wordlength greater than 1...");
    			String input = System.console().readLine();
    			l = Integer.parseInt(input);
    		}
    	}   
    	return l;
    }
    public static int verifyGuessNum(int g)
    {
    	if(g < 1)
    	{
    		while(g < 1)
    		{
    			System.out.println("Please input number of guesses greater than 0...");
    			String input = System.console().readLine();
    			g = Integer.parseInt(input);	
    		}
    	}
    	return g;
    }
}//end class Main


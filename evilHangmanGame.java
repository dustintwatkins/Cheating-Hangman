package hangman;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import hangman.IEvilHangmanGame.GuessAlreadyMadeException;
import java.io.FileNotFoundException;

public class evilHangmanGame implements IEvilHangmanGame{
    Set<String> words = new TreeSet<String>();
    Map<String, Set<String>> map = new TreeMap<String, Set<String>>();
    Set<String> guessedChars = new TreeSet<String>();
    int length = 0;
    String pattern = null;
    private int numGuesses = 0; 
    
    public void startGame(File dictionary, int wordLength)
    {
    	loadDictionary(dictionary, wordLength);
    	length = wordLength;
    	pattern = lettersToDashes();
   	}
    
    public void playGame(int g)
    {
    	numGuesses = g;
    	while(numGuesses != 0 /*&& hasn't won*/)
    	{
    		String usedChars = outputGuessedChars();
    		System.out.println("You have " + numGuesses + " guesses left");
    		System.out.println("Used letters: " + usedChars);
    		System.out.println("Word: " + pattern);
    		System.out.print("Enter Guess: ");
    		String input = System.console().readLine();
    		input = input.toLowerCase();
    		char c = checkChar(input);
    		try{
				makeGuess(c);
			}catch(GuessAlreadyMadeException excpt) {
				System.out.println("You already have guessed that letter!");
			}
    	}
    }
    
    @Override
    public Set<String> makeGuess(char c) throws GuessAlreadyMadeException
    {
    	String guess = Character.toString(c);
    	if(guessedChars.contains(guess))
    	{
    		throw new GuessAlreadyMadeException();
    	}
    	guessedChars.add(guess);
		//Build the map...
		//SHOULD ONLY DECREMENT IF THERE IS AT LEAST 1 WORD THAT DOESNT CONTAIN GUESS
		numGuesses--;
    	return words;
    }
    
    public void loadDictionary(File dictionary, int wordLength)
    {
    	try{
    		FileReader fr = new FileReader(dictionary);
    		Scanner scan = new Scanner(fr);
    		while(scan.hasNext())
    		{
    			String s = scan.next();
    			if(s.length() == wordLength)
    			{
    				s = s.toLowerCase();
    				words.add(s);
    			}
    		}
    		scan.close();
    	}//end try
    	catch(FileNotFoundException e){
    		System.out.println("error found with loading the file...");
    	}
    	
    	

    }// end startGame method
    
    public char checkChar(String input)
    {
    	input = input.toLowerCase();
    	char c = input.charAt(0);
    	if(input.length() == 1 && Character.isLetter(c))
    	{
//    		System.out.println("valid guess");
    		return c;
    	}
    	else
    	{
    		boolean bool = true;
    		while(bool)
    		{
    			System.out.print("Invalid guess, please try again!");
    			String str = System.console().readLine();
    			str = str.toLowerCase();
    			char c2 = str.charAt(0); 
    			if(str.length() == 1 && Character.isLetter(c2))
    	    	{
//    	    		System.out.println("valid guess");
    	    		bool = false;
    	    		return c2;
    	    	}
    		}//end while loop
    	}
//    	System.out.println("should not return here = " + c);
    	return c;
    }//end checkChar()
    
    public String lettersToDashes()
    {
    	StringBuilder sb = new StringBuilder();
    	for(int i = 0; i < length; i++)
    	{
    		sb.append('-');
    	}
    	return sb.toString();
    }
     
    public String outputGuessedChars()
    {
    	StringBuilder sb = new StringBuilder();
    	for(String s : guessedChars)
    	{
    		sb.append(s + " ");
    	}
    	return sb.toString();
    }
    
    public int getLength()
    {
    	return length;
    }
    
    public String getPattern()
    {
    	return pattern;
    }
}//end evilHangman class




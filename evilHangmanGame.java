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
	char g;
    private int numGuesses = 0; 
    
    public void startGame(File dictionary, int wordLength)
    {
    	loadDictionary(dictionary, wordLength);
    	length = wordLength;
    	pattern = lettersToDashes();
   	}//end startGame()
    
    public void playGame(int g)
    {
    	numGuesses = g;
    	while(numGuesses != 0 && !hasWon(pattern))
    	{
    		String usedChars = outputGuessedChars();
    		System.out.println("You have " + numGuesses + " guesses left");
    		System.out.println("Used letters: " + usedChars);
    		System.out.println("Word: " + pattern);
    		System.out.print("Enter Guess: ");
			Scanner scan = new Scanner(System.in);
			String input = scan.nextLine();
			input = input.toLowerCase();
			if(input.equals(""))
			{
				boolean bool = true;
				while(bool) {
					System.out.println("Invalid input, try again");
					String s = scan.nextLine();
					s = s.toLowerCase();
					if(!s.equals(""))
					{
						input = s;
						bool = false;
					}
				}
			}
    		char c = checkChar(input);
    		try{
				makeGuess(c);
			}catch(GuessAlreadyMadeException excpt) {
				System.out.println("You already have guessed that letter!");
			}
    	}//end while loop
		if(hasWon(pattern))
		{
			System.out.println("Congratulations, you won! The word was " + pattern +"!");
		}
		else
		{
			String word = "";
			for(String s : words)
			{
				word = s;
				break;
			}
			System.out.println("Sorry you lose, the word was " + word + "!");
		}
    }//end playGame()
    
    @Override
    public Set<String> makeGuess(char c) throws GuessAlreadyMadeException
    {
//		System.out.println("words size = " + words.size());
    	String guess = Character.toString(c);
    	if(guessedChars.contains(guess))
    	{
    		throw new GuessAlreadyMadeException();
    	}
		g = c;
    	guessedChars.add(guess);
		mapping(c);
		String key = largestSet();
		if(key.equals(pattern))
		{
			System.out.println("Sorry, there are no " + c + "'s");
			System.out.println("");
			numGuesses--;
		}
		else
		{
			int count = 0;
			for(int i = 0; i < key.length(); i ++)
			{
				if(key.charAt(i) == c)
				{
					count++;
				}
			}
			System.out.println("Yes, there is " + count + " " + c );
			System.out.println("");
		}
		pattern = key;
		words = map.get(key);
		//SHOULD ONLY DECREMENT IF THERE IS AT LEAST 1 WORD THAT DOESN'T CONTAIN GUESS
//		numGuesses--;
    	return words;
    }//end makeGuess()

    public void mapping(char guess)
	{
		map.clear();
		for(String s: words)
		{
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < s.length(); i++)
			{
			 	if(s.charAt(i) == guess)
				{
					sb.append(guess);
				}
				else if(pattern.charAt(i) != '-')
				{
					sb.append(pattern.charAt(i));
				}
				else
				{
					sb.append("-");
				}
			}//end inner for loop
			String key = sb.toString();
			if(map.containsKey(key))
			{
				map.get(key).add(s);
			}
			else
			{
				Set<String> subsetWords = new TreeSet<>();
				subsetWords.add(s);
				map.put(key, subsetWords);
			}
		}//end outer for loop
	}//end mapping

	public String largestSet()
	{
		String lSet = "";
		int num = 0;
		for(String s : map.keySet())
		{
			Set<String> temp = map.get(s);
			if(temp.size() > num)
			{
				num = temp.size();
				lSet = s;
			}
			else if (temp.size() == num)
			{
				lSet = fewestLetters(lSet, s);
			}
		}
		return lSet;
	}//end largestSet()

	public String fewestLetters(String s1, String s2)
	{
		int s1Count = countGuess(s1);
		int s2Count = countGuess(s2);

		if(s1Count > s2Count)
		{
			return s2;
		}
		else if(s1Count < s2Count)
		{
			return s1;
		}
		else
		{
			//size are equal... check rightmost
			return rightMost(s1, s2);
		}
	}//end fewestLetters()

	public int countGuess(String s)
	{
		int count = 0;
		for(int i = 0; i < s.length(); i++)
		{
			if(s.charAt(i) == g)
			{
				count++;
			}
		}
		return count;
	}

	public String rightMost(String s1, String s2)
	{
		int l = s1.length();
		for(int i = l -1; i > -1; i--)
		{
			if(s1.charAt(i) == '-' && s2.charAt(i) != '-')
			{
				return s2;
			}
			else if(s1.charAt(i) != '-' && s2.charAt(i) == '-')
			{
				return s1;
			}
		}//end for loop
		//shouldn't ever return here
		System.out.println("shouldn't return here... check this");
		return s1;
	}//end rightMost()

	public boolean hasWon(String s)
	{
		for(int i = 0; i < s.length(); i++)
		{
			if(s.charAt(i) == '-')
				return false;
		}
		return true;
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
		char c = input.charAt(0);
			if (input.length() == 1 && Character.isLetter(c))
			{
				return c;

			}
			else
			{
				boolean bool = true;
				while (bool) {
					System.out.print("Invalid guess, please try again!");
					Scanner scan = new Scanner(System.in);
					String str = scan.nextLine();
					str = str.toLowerCase();
					if (str.equals("")) {

					} else {
						char c2 = str.charAt(0);
						if (str.length() == 1 && Character.isLetter(c2)) {
							//    	    		System.out.println("valid guess");
							bool = false;
							return c2;
						}
					}
				}//end while loop
			}
		System.out.println("bad return");
			return '-';
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



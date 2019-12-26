package server.model;

import java.io.*;
import java.util.*;

public class HangmanGame 
{
	private String hiddenWord;
	private String secretWord;
	private int score;
	private File file;
	private BufferedReader fileReader;
	private int attemptsLeft;
	
	public HangmanGame()
	{
		try
		{
			score = 0;
			file = new File("C:\\Users\\rafim\\eclipse-workspace\\Hangman-HW2\\src\\server\\model\\words.txt");
			//fileReader = new BufferedReader(new FileReader(file));
			attemptsLeft = 7;
			secretWord = wordGen();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void newGame()
	{
		attemptsLeft = 7;
		secretWord = wordGen();
	}
	
	public void newGameReset()
	{
		score--;
		newGame();
	}
	
	public String newGuess(String input)
	{
		if(input.length()>1)// when inputs is larger than 1 ; input is Word
		{
			if(input.toLowerCase().equals(secretWord.toLowerCase()))//input is correct
			{
				score++;
				hiddenWord = secretWord;
				String temp = toString() + "\n";
				newGame();
				temp = temp + toString();
				return temp;
			}
			else //input is wrong
			{
				if(attemptsLeft<=1)// no attempts left
				{
					score--;
					String temp = toString() + "\n";
					newGame();
					temp = temp + toString();
					return temp;
				}
				
				else //decrease attempts and return 
				{
					attemptsLeft--;
					return toString();
				}
			}
		}
		else if(input.length()==1) //when input is letter
		{
			char toChar = input.charAt(0);
			toChar = Character.toLowerCase(toChar);
			int index = secretWord.toLowerCase().indexOf(toChar);
			if(index>=0)
			{
				for(int j=0; j<secretWord.length(); j++)
				{
					if(toChar == secretWord.toLowerCase().charAt(j))
					{
						StringBuilder temp = new StringBuilder(hiddenWord);
						temp.setCharAt(j, secretWord.charAt(j));
						hiddenWord = temp.toString();
					}
				}
				
				if(hiddenWord.toLowerCase().equals(secretWord.toLowerCase()))
				{
					score ++;
					String temp = toString() + "\n";
					newGame();
					temp = temp + toString();
					return temp;
				}
				return toString();
			}
			else if(index<0)
			{
				if(attemptsLeft <= 1)
				{
					score--;
					newGame();
					return toString();
				}
				else
				{
					attemptsLeft --;
					return toString();
				}
			}
		}
		return toString();
	}
	
	public String getSecretWord()
	{
		return secretWord;
	}
	
	public String wordGen()
	{
		try
		{
			hiddenWord = "";
			String toReturn = "";
			fileReader = new BufferedReader(new FileReader(file));
			Random rand = new Random();
			int number = rand.nextInt(850);
//			System.out.println(number);
			for(int i = 0; i < number; i++)
			{
				toReturn = fileReader.readLine();
			}
//			System.out.println(toReturn);
			for(int i = 0; i < toReturn.length(); i++)
			{
				hiddenWord = hiddenWord + "-";
			}
			return toReturn;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "...";
		}
	}
	
	public String toString()
	{
		String toReturn = "[" + hiddenWord + "]\t" + attemptsLeft + "\t" + score;
		return toReturn;
	}
}

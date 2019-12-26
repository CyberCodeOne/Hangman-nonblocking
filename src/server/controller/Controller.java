package server.controller;

import java.nio.channels.*;
import java.io.IOException;
import java.nio.*;
import java.util.*;
import server.model.*;

public class Controller 
{
	public Controller()
	{
		
	}
	
	public void newClient(SocketChannel socketChannel, HashMap clients)
	{
		try
		{
			System.out.println("New Client");
			HangmanGame game = new HangmanGame();
			clients.put(socketChannel.getRemoteAddress(), game);
			String s = "You are now connected to the server.\nType \"***CLOSE***\" to quit or \"New Game to\" get a start a new game\n"
					+ "Guess the following word\n" + game.toString();
			ByteBuffer buff = ByteBuffer.wrap(s.getBytes("UTF-8"));
			socketChannel.write(buff);
			System.out.println(game.getSecretWord());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void handleRequest(SocketChannel socketChannel, HashMap clients, String input)
	{
		try
		{
			HangmanGame saved = (HangmanGame) clients.get(socketChannel.getRemoteAddress());
			System.out.println(socketChannel.getRemoteAddress() + "\t" + saved.getSecretWord());
			String response = "";
			if(input.toLowerCase().equals("new game"))
			{
				saved.newGameReset();
				response = saved.toString();
			}
			else
			{
				response = saved.newGuess(input);
			}
			System.out.println(saved.getSecretWord());
			ByteBuffer buff = ByteBuffer.wrap(response.getBytes("UTF-8"));
			socketChannel.write(buff);
			clients.put(socketChannel.getRemoteAddress(), saved);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

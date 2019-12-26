package server.startup;

import server.model.*;

public class Main 
{
	public static void main(String[]args)
	{
		try 
		{
			new Thread(new Server()).start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

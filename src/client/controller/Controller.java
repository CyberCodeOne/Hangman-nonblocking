package client.controller;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class Controller implements Runnable
{
	private BufferedReader input;
	private Selector selector;
	private SocketChannel socketChannel;
	
	public Controller(Selector selector, SocketChannel socketChannel)
	{
		this.selector = selector;
		this.socketChannel = socketChannel;
		input = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				if(selector.select()>0)
				{
					Boolean done = processKeys(selector.selectedKeys());
					if(done)
						{break;}
				}
			}
			socketChannel.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Boolean processKeys(Set keys) throws Exception
	{
		Iterator iterator = keys.iterator();
		SelectionKey key;
		while(iterator.hasNext())
		{
			key = (SelectionKey) iterator.next();
			iterator.remove();
			
			if(key.isConnectable())
			{
				Boolean connect = handleConnection(key);
				if(!connect)
				{return true;}
			}
			
			if(key.isReadable())
			{
				ByteBuffer reader = ByteBuffer.allocate(1024);
				socketChannel.read(reader);
				String message = new String(reader.array()).trim();
				System.out.println(message);
			}
			
			if(key.isWritable())
			{
				
				String userInput = input.readLine();
				if(userInput.equals("***CLOSE***"))
				{
					return true;
				}
				ByteBuffer sender = ByteBuffer.wrap(userInput.getBytes());
				socketChannel.write(sender);
				Thread.sleep(50);
			}
			
		}
		return false;
	}
	
	public Boolean handleConnection(SelectionKey key)
	{
		try
		{
			SocketChannel temp = (SocketChannel) key.channel();
			while(temp.isConnectionPending())
			{
				temp.finishConnect();
			}
		}
		catch(Exception e)
		{
			key.cancel();
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
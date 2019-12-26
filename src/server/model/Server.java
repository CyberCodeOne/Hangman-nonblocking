package server.model;

import server.controller.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.net.*;
import java.util.*;

public class Server implements Runnable
{
	private final int PORT = 1234;
	private final int CAPACITY = 1024;
	private InetAddress host;
	private  ServerSocketChannel serverSocketChannel;
	private Selector selector; //we need a selector for incoming requests.
	private Controller controller;
	private HashMap clients;
	
	public Server() throws Exception
	{
		host = InetAddress.getLocalHost();
		selector = Selector.open();	//selector is like a multiplexer to select clients.
		serverSocketChannel = ServerSocketChannel.open(); //we set the serverSocketChannel to open to indicate we want to receive selectable connections
		serverSocketChannel.configureBlocking(false);	//we set to false to indicate we want non-blocking
		serverSocketChannel.socket().bind(new InetSocketAddress(host, PORT));	//bind the server's socket to local address
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //we save the option of accepting connections on the 
		controller = new Controller();
		clients = new HashMap();
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				if(selector.select()<=0)
				{
					continue;
				}
				Iterator iterator = selector.selectedKeys().iterator();
				SelectionKey key;
				while(iterator.hasNext())
				{
					key = (SelectionKey) iterator.next();
					iterator.remove();
					if(key.isAcceptable())
					{
						accept(key);
					}
					if(key.isReadable())
					{
						read(key);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void accept(SelectionKey key) throws Exception
	{
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		System.out.println("Connection accepted from " + socketChannel.getRemoteAddress() + " ... ");
		socketChannel.register(selector, SelectionKey.OP_READ);
		controller.newClient(socketChannel, clients);
	}
	
	private void read(SelectionKey key) throws Exception
	{
		SocketChannel clientSocketChannel = (SocketChannel) key.channel();
		ByteBuffer reader = ByteBuffer.allocate(CAPACITY);
		clientSocketChannel.read(reader);
		String userInput = new String(reader.array()).trim();
		if(userInput == null || userInput.length() <= 0)
		{
			clients.remove(clientSocketChannel.getRemoteAddress());
			clientSocketChannel.close();
		}
		else
		{
			controller.handleRequest(clientSocketChannel, clients, userInput);
		}
	}
}

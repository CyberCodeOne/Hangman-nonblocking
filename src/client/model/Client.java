package client.model;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import client.controller.*;

public class Client 
{
	private final int PORT = 1234;
	private InetAddress host;
	private InetSocketAddress serverAdd;
	private Selector selector;
	private SocketChannel socket;
	//private Controller controller;
	
	public Client()
	{
		try
		{
			host = InetAddress.getLocalHost();
			serverAdd = new InetSocketAddress(host, PORT);
			selector = Selector.open();
			socket = SocketChannel.open();
			socket.configureBlocking(false);
			socket.connect(serverAdd);
			int operation = SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
			socket.register(selector, operation);
			Thread control = new Thread(new Controller(selector, socket));
			control.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

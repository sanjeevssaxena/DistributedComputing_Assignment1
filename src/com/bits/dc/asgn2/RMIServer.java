package com.bits.dc.asgn2;
//This class sets up the server.
/**
 * The class is used to run the RMI Server.
 * It is associated with folder which stores the content.
 */
import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer implements Serializable{
	
	/**
	 * The variable port to listen.
	 */
	static int portnumber;
	
     //String remoteObject ="remoteObject";
	static String start = "start";

	/**
	 * Main method to run the RMI Server.
	 */
	public static void main(String[] args) {
		
		try{
			if(start.equals(args[0]))
		    {
				portnumber = Integer.parseInt(args[1]);
				
			}
			Registry reg = LocateRegistry.createRegistry(portnumber);
			
			//Creates and exports a Registry instance on the local host that accepts requests on the specified port.

			RmiImplementation imp =  new RmiImplementation("e:\\upload\\");
			reg.bind("remoteObject", imp);
			System.out.println("Server is ready.");
			System.out.println(portnumber);
		}
		catch(Exception e){
			System.out.println("Server failed: " + e);
		}
	}
}

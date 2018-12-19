import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Lamport extends UnicastRemoteObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Scanner in = new Scanner(System.in);
	
	protected Lamport() throws RemoteException {
	
	}

	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub

		System.setSecurityManager(new SecurityManager());
		
		System.out.println("Master Server is starting...");
		
		String master_id = "//in-csci-rrpc01.cs.iupui.edu:2089/master";
		System.out.println("MarketServer binding it to name: " + master_id);
		
		MasterObj mo = new MasterObj();
		
		try {
			
			//create master obj registry entry
			Naming.rebind(master_id,  mo);
			System.out.println("Master Object is started..");
			
			System.out.println("Start all the processes and then press 'y' to continue...");
			//start the master once all the POs are up
			if(in.nextLine().equals("y")){
				mo.start();
			}
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}//main

}//class Lamport

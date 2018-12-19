import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class LamportPo {

	private static Scanner in;

	public static void main(String[] args) throws RemoteException{
		
		int t= Integer.parseInt(args[1]);
		int SimuIteration = 1000;
		int pro = Integer.parseInt(args[0]);
		int pID;
		ProcessObj po = null;
		in = new Scanner(System.in);
		
		try {
			//get the host name on which PO is running
			String host = InetAddress.getLocalHost().getHostName();
			String name = host.split("\\.")[0];
			
			//truncate the pID from hostname
			pID = Integer.parseInt(name.substring(name.length()-2));
			
			
			System.out.println("Process started with ID: " + pID);
			
			String process_id = "//in-csci-rrpc0"+pID+".cs.iupui.edu:2089/PO" + pID;
			System.out.println("Process binding it to name: " + process_id);
			
			//initialize the PO object
			po = new ProcessObj(pID,SimuIteration,pro,t);
			
			//make registry entry for RMI
			Naming.rebind(process_id,  po);
			System.out.println("Process Object is started..");
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Start all the processes and then press 'y' to continue...");
		//start the logical clock counter once all the PO are started
		if(in.nextLine().equals("y")){
			po.start();
		}
		
	}

}

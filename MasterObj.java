import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MasterObj extends UnicastRemoteObject  implements IMasterObj{

	private static final long serialVersionUID = 1L;

	int logiClk,eventCnt;
	BlockingQueue<Queue> mesQ;
	
	HashMap<Integer,Integer> processClk = new HashMap<Integer,Integer>();
	
	int avgLogiClk=0,offset,CorrLogiClk;
	
	File fout;
	FileOutputStream fos;
	BufferedWriter bw;
	IProcessObj po2,po3,po4,po5;

		
	String 	P2 = "//in-csci-rrpc02.cs.iupui.edu:2089/PO2";
	String 	P3 = "//in-csci-rrpc03.cs.iupui.edu:2089/PO3";
	String 	P4 = "//in-csci-rrpc04.cs.iupui.edu:2089/PO4";
	String 	P5 = "//in-csci-rrpc05.cs.iupui.edu:2089/PO5";
	
	protected MasterObj() throws RemoteException {
		logiClk = 0;
		eventCnt = 0;
		mesQ = new LinkedBlockingQueue<Queue>();
		
		//create file to write the POs logical clock
		fout = new File("Process_LogiClk.txt");
		
		try{
			fos = new FileOutputStream(fout);
			bw = new BufferedWriter(new OutputStreamWriter(fos));
		
			bw.write("P0,P1,P2,P3");
			bw.newLine();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	
	public void start(){
		
		try {
			
			//check for all POs reference
			po2 = (IProcessObj) Naming.lookup(P2);
			po3 = (IProcessObj) Naming.lookup(P3);
			po4 = (IProcessObj) Naming.lookup(P4);
			po5 = (IProcessObj) Naming.lookup(P5);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			
			while(true){
				
				if(!mesQ.isEmpty()){
					processClk.put(mesQ.peek().getProcessId(),mesQ.poll().getLogiClk());
					logiClk++;
					
					//calculate average if all 4 values are received
					if(processClk.keySet().size() == 4){
						avgLogiClk = 0;
						
						logiClk++;
						System.out.println("Master calculation and its logiClk: " + logiClk);
						
						for(Integer val : processClk.keySet()){
							System.out.println(val + " " + processClk.get(val));
							avgLogiClk += processClk.get(val);
						}
																	
						//add master's logical clock
						avgLogiClk += logiClk;
						
						//calculate average of value
						CorrLogiClk = avgLogiClk / 5;
						System.out.println("CorrLogiClk: " + CorrLogiClk);
						
						//adjust master objects logical clock
						logiClk = logiClk + (CorrLogiClk-logiClk) + 1;
						
						StringBuilder sb = new StringBuilder();
						//calculate the offset for each Processes
						//sb.append(CorrLogiClk).append(",");
						for(Integer val : processClk.keySet()){
							offset = CorrLogiClk - processClk.get(val);
							//sb.append(offset).append(",");
							System.out.println("Offset of pId " + val + " is: " + offset);
							//Send the offset to the Processes 
							sendClkOffset(val,offset);
							sb.append(processClk.get(val)).append(",");
						}

												
						bw.write(sb.toString());
						bw.newLine();
						bw.flush();
						System.out.println("Written to file");
						
						processClk.clear();
						
					}
				}//calculate Average
				else{
					//internal event of master
					Thread.sleep(20);
					logiClk++;
					//System.out.println("Master Thread Internal Event: " + logiClk);					
				}
				
			}//while

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	@Override
	public void sendClkMaster(int poID, int clk) throws RemoteException{

		//add the clock value received from POs to queue
		
		Queue q = new Queue();
		q.setLogiClk(clk);
		q.setProcessId(poID);
		
		mesQ.add(q);
		
	}
	
	public void sendClkOffset(int poID,int clk){
		
		//Send the offset to PO based on ID
		System.out.println(poID +" send " + clk);
		try {
		switch(poID){
		case 2:
			po2.sendClkOffset(0, clk);
			break;
		case 3:
			po3.sendClkOffset(0, clk);
			break;
		case 4:
			po4.sendClkOffset(0, clk);
			break;
		case 5:
			po5.sendClkOffset(0, clk);
			break;
			
		}
		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

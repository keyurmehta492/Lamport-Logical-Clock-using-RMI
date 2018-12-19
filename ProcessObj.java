import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessObj extends UnicastRemoteObject  implements IProcessObj{
	
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		int result;
		int logiClk,eventCnt,t,iteration,prob;
		int pId;
		int SimuIteration;
		Random rand = new Random();
		IMasterObj mo;
		BlockingQueue<Queue> mesQ;
		IProcessObj po2,po3,po4,po5;
		
		String 	name = "//in-csci-rrpc01.cs.iupui.edu:2089/master";
		String 	P2 = "//in-csci-rrpc02.cs.iupui.edu:2089/PO2";
		String 	P3 = "//in-csci-rrpc03.cs.iupui.edu:2089/PO3";
		String 	P4 = "//in-csci-rrpc04.cs.iupui.edu:2089/PO4";
		String 	P5 = "//in-csci-rrpc05.cs.iupui.edu:2089/PO5";
		
		public ProcessObj(int pId, int SimuIteration,int prob,int t) throws RemoteException{
			
			// RMI Security Manager
			System.setSecurityManager(new SecurityManager());
				
			//RMI lookup
			try {
				mo = (IMasterObj) Naming.lookup(name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			this.pId =pId;
			logiClk = 0;
			eventCnt = 0;
			this.t = t;
			iteration = 0;
			this.prob = prob;
			this.SimuIteration = SimuIteration;
			
			mesQ = new LinkedBlockingQueue<Queue>();
		}
	
		
		
	public void start(){
		
		
		try {
			
			Thread.sleep(100/pId);
			
			//set all POs reference
			po2 = (IProcessObj) Naming.lookup(P2);
			po3 = (IProcessObj) Naming.lookup(P3);
			po4 = (IProcessObj) Naming.lookup(P4);
			po5 = (IProcessObj) Naming.lookup(P5);
		
		while(iteration < SimuIteration){			
			
			int eventSel = rand.nextInt(10);
						
			//send event
			if(eventCnt == t){
				System.out.println("Send event of Thread " + pId + " with LogiClk: " + logiClk);
				//send logical clock value to other PO 
				sendClkMaster(pId,logiClk);
				
				//increment the logical clock and event counter t
				logiClk++;
				eventCnt = 1;
								
				if(eventSel < 4){
					if((eventSel+2) != pId){
						System.out.println("Send event of Process to other Process..");
						
						//send logical clock value to other PO
						sendClkPo(eventSel+2,logiClk);
						
						//increment the logical clock and event counter t
						eventCnt++;
						logiClk++;
					}
				}
							
			}//send
			
			else {
				//receive event
	
				if(eventSel > prob){
					logiClk++;
					if(!mesQ.isEmpty()){
						//Receive from master
						if(mesQ.peek().getProcessId() == 0) {
							System.out.println(pId + " received offset " + mesQ.peek().getLogiClk());
							
							
							//Byzantine failure						
							if(eventSel == 9){
								System.out.println("Byzantine failure. Do not update the clock");
								mesQ.poll();
							}
							else
								logiClk = logiClk + mesQ.poll().getLogiClk() + 1;
							
							//increment the iteration of PO
							iteration++;
							
							System.out.println("Receive event of Thread " + pId + " with new logicalClk: " + logiClk 
									+ " ITERATION: " + iteration);
														
						}
						//Receive from other process
						else {
							int temp = mesQ.poll().getLogiClk();
							System.out.println("Internal P2P");
							
							if(logiClk <= temp)
								logiClk = temp+1;
						}
					}//msg
					
				}
				
			//internal event
				else if(eventSel < prob){
					try {
						Thread.sleep((eventSel+1)*10);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//increment the logical clock value depending on the random value
					logiClk += (eventSel+1);
					
					System.out.println("Internal event of Thread " + pId + " with increment of "+ eventSel+ " Clock: " + logiClk);
				}
				
				//increment the event count either receive or internal event
				eventCnt++;
				
			}
			
		}//while
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error: ");
			e.printStackTrace();
		}
		System.out.println("goodBye"+ pId);
	}
	
	//send logical clock value to MO
	public void sendClkMaster(int pId, int logiClk){

		try {
			mo.sendClkMaster(pId, logiClk);;
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}



	@Override
	public void sendClkPo(int poID, int clk) throws RemoteException {
		
		//send clock value to other POs
		System.out.println(poID + " Process clock " + clk);
		Queue q = new Queue();
		q.setProcessId(poID);
		q.setLogiClk(clk);
		
		mesQ.add(q);
		
		
		
	}//sendClkPo



	@Override
	public void sendClkOffset(int poID, int clk) throws RemoteException {
		System.out.println(poID + " received clock " + clk);
		
		//add the received offset from master in message queue
		Queue q = new Queue();
		q.setProcessId(poID);
		q.setLogiClk(clk);
		
		mesQ.add(q);
		
	}
}

import java.rmi.RemoteException;

public interface IProcessObj extends java.rmi.Remote{

	//send clock value to other PO
	void sendClkPo(int poID, int clk) throws RemoteException;
	
	//send clock offset to PO
	void sendClkOffset(int poID, int clk) throws RemoteException;
}

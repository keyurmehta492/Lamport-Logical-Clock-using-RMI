import java.rmi.RemoteException;

public interface IMasterObj extends java.rmi.Remote{

	//send logical clock to master
	void sendClkMaster(int poID, int clk) throws RemoteException;
}

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIUtil {

	public static RmiInterface getRemoteConnection(String server) throws RemoteException, NotBoundException {
		
		String environment = System.getenv(server);
        System.out.println("In RMIUtil: "+environment);
        
        String hostname = environment.split(":")[0];
        
        int portnumber = Integer.parseInt(environment.split(":")[1]);
        System.out.println("seeking connection on:" + environment);
        
        Registry myreg = LocateRegistry.getRegistry(hostname, portnumber);                
        RmiInterface inter = (RmiInterface)myreg.lookup("remoteObject");
        
        
        return inter;
	}
}

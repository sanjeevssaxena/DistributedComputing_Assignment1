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
	
	public static boolean isOriginatorSameAsNearestServer(String originator) {
		
		boolean flag = true;
		
		String environment = System.getenv("NEAREST_SERVER");
		if (environment == null)
			return flag;
		        
        String hostname = environment.split(":")[0];
        if(hostname == null)
        	return flag;
	
        if(hostname.equals(hostname))
        	return flag;
        
        return flag;
	}
}

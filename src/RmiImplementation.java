
//This class contains implementation of all the functionalities provided to the client. 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiImplementation extends UnicastRemoteObject implements RmiInterface, Serializable {

	protected RmiImplementation(String s) throws RemoteException {
        File storageDir = new File(s);
        storageDir.mkdir();
    }
    
    public void uploadFileToServer(byte[] mydata, String serverpath, int length) throws RemoteException {

        try {
            File serverpathfile = new File(serverpath);
            FileOutputStream out = new FileOutputStream(serverpathfile);
            byte[] data = mydata;

            out.write(data);
            out.flush();
            out.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        System.out.println("Done writing data...");

    }

    public byte[] downloadFileFromServer(String serverpath, String originator) throws RemoteException {

    	System.out.println("Request received from "+originator+" for file "+serverpath);
        File serverpathfile = new File(serverpath);
        byte[] mydata = new byte[(int) serverpathfile.length()];
        FileInputStream in;
        try {
            in = new FileInputStream(serverpathfile);
            try {
                in.read(mydata, 0, mydata.length);
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {

                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {

            System.out.println("File not found in the local server ");
            if(RMIUtil.isOriginatorSameAsNearestServer(originator)) {
            	System.out.println("Original request is same as nearest server, hence not initiating from nearest server");
            	return mydata;
            }
            try {
            	System.out.println("Initiating request from nearest server for file: "+serverpath);
				mydata = RMIUtil.getRemoteConnection("NEAREST_SERVER").downloadFileFromServer(serverpath, originator);
				System.out.println("File received from nearest server. Saving in local server.");
				uploadFileToServer(mydata, serverpath, mydata.length);
			} catch (RemoteException | NotBoundException e1) {
				// TODO Auto-generated catch block
				System.out.println("Received exception: "+e.getMessage());
			}
        }

        return mydata;

    }

    

	public String[] listFiles(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.list();

    }

    public boolean createDirectory(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.mkdir();

    }

    public boolean removeDirectoryOrFile(String serverpath) throws RemoteException {
        File serverpathdir = new File(serverpath);
        return serverpathdir.delete();

	}

}

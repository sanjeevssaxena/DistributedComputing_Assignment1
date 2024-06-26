package com.bits.dc.asgn2;

/**
 * This class contains implementation of all the functionalities provided to the client. 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RmiImplementation extends UnicastRemoteObject implements RmiInterface, Serializable {
	
	private Map<String,String> hashcodeFiles = new HashMap<String, String>();
	private static MessageDigest messageDigest;
	static {
	    try {
	        messageDigest = MessageDigest.getInstance("SHA-512");
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("cannot initialize SHA-512 hash function", e);
	    }
	}  

	/**
	 * Constructor to create the rmi implementation.
	 */
	protected RmiImplementation(String s) throws RemoteException {
		File storageDir = new File(s);
		storageDir.mkdir();
	}

	/**
	 * Method to upload the file to the folder.
	 */
	public void uploadFileToServer(byte[] mydata, String serverpath, int length) throws RemoteException {

		try {
			String hashcode = getHashCode(mydata);
			File serverpathfile = new File(serverpath);
//			int hashcode = serverpathfile.hashCode();
			String filepath = hashcodeFiles.get(hashcode);
			if(filepath != null) {
				File todelete = new File(filepath);
				todelete.delete();
			}
			hashcodeFiles.put(hashcode, serverpath);
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

	public String getHashCode(byte[] data) {
		String hash = new BigInteger(1, messageDigest.digest(data)).toString(16);
		return hash;
	}
	/**
	 * Method to download the file from the server
	 */
	public byte[] downloadFileFromServer(String serverpath, String originator) throws RemoteException {

		System.out.println("\n\n*************Time ****" + LocalDateTime.now() + "******************");
		System.out.println("Request received from " + originator + " for file " + serverpath);
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
			if (RMIUtil.isOriginatorSameAsNearestServer(originator)) {
				System.out.println(
						"Original request is same as nearest server, hence not initiating from nearest server");
				return mydata;
			}
			try {
				System.out.println("Initiating request from nearest server for file: " + serverpath);
				mydata = RMIUtil.getRemoteConnection("NEAREST_SERVER").downloadFileFromServer(serverpath, originator);
				if (mydata.length == 0) {
					System.out.println("File content not found from nearest server");
					return mydata;
				}
				System.out.println("File received from nearest server. Saving in local server.");
				uploadFileToServer(mydata, serverpath, mydata.length);
			} catch (RemoteException | NotBoundException e1) {
				// TODO Auto-generated catch block
				System.out.println("Received exception: " + e.getMessage());
			}
		}

		return mydata;

	}

	/**
	 * Method to list the files available.
	 */
	public String[] listFiles(String serverpath) throws RemoteException {
		File serverpathdir = new File(serverpath);
		return serverpathdir.list();

	}

	/**
	 * Method to create directory.
	 */
	public boolean createDirectory(String serverpath) throws RemoteException {
		File serverpathdir = new File(serverpath);
		return serverpathdir.mkdir();

	}

	/**
	 * Method to remove the directory.
	 */
	public boolean removeDirectoryOrFile(String serverpath) throws RemoteException {
		File serverpathdir = new File(serverpath);
		return serverpathdir.delete();

	}

}

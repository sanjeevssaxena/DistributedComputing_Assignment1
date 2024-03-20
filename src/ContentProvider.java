import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class ContentProvider {

	public static void main(String[] args) {

		StringBuilder hostName = new StringBuilder();
		try {
			hostName.append(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e1) {
			
			e1.printStackTrace();
			hostName.append("NoHost");
		}
		String strData = "This content is from "+hostName+" written at time"+LocalDateTime.now();
		byte[] data = strData.getBytes();
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy-hhmm");  
		Date date = new Date();
		hostName.append("_").append(formatter.format(date));
		Path path = Paths.get(args[0]+hostName);
		
		try {
			Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package vttp.batchb.ssf.day13_workshop;

import java.io.Console;
import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Day13WorkshopApplication {

	private static final Logger logger = Logger.getLogger(Day13WorkshopApplication.class.getName());
	public static String directory = "";

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(Day13WorkshopApplication.class);

		// task 1
		// --dataDir /opt/tmp/data
		// ApplicationArguments command = new DefaultApplicationArguments(args);
		// if (!command.containsOption("dataDir")){
		// 	logger.severe("Directory is not specified");
		// 	System.exit(-1);
		// }

		// if (command.getOptionValues("dataDir") == null || command.getOptionValues("dataDir").isEmpty()){
		// 	logger.severe("Invalid or missing directory specified for dataDir");
		// 	System.exit(-1);
		// }

		// String directory = "C:" + command.getOptionValues("dataDir").get(0);
		// File dir = new File(directory);
		
		// // check if directory exists
		// if (!dir.exists()){
		// 	dir.mkdir();
		// } else {
		// 	logger.info("Directory already exists");
		// }

		application.run(args);

	}

	

}

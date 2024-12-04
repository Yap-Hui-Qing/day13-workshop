package vttp.batchb.ssf.day13_workshop;

import java.io.Console;
import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Day13WorkshopApplication implements ApplicationRunner {

	private static final Logger logger = Logger.getLogger(Day13WorkshopApplication.class.getName());
	public static File dataDir;

	public static void main(String[] args) {

		SpringApplication.run(Day13WorkshopApplication.class, args);

	}

	@Override
	public void run(ApplicationArguments args){

		logger.info("Application started with command-line arguments: {%s}".formatted(Arrays.toString(args.getSourceArgs())));
		logger.info("NonOptionArgs: {%s}".formatted( args.getNonOptionArgs()));
        logger.info("OptionNames: {%s}".formatted(args.getOptionNames()));

		if (!args.containsOption("dataDir")){
			logger.severe("Directory is not specified");
			System.exit(-1);
		}

		String directory = args.getOptionValues("dataDir").getFirst();
		System.out.println(directory);
		dataDir = new File(directory);
		// check if directory exists
		if (!dataDir.exists()){
			dataDir.mkdirs();
			logger.info("Directory has been created");
		} else {
			logger.info("Directory already exists");
		}
	}
	
}

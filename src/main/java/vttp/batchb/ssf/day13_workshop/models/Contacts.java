package vttp.batchb.ssf.day13_workshop.models;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import static vttp.batchb.ssf.day13_workshop.Day13WorkshopApplication.*;

public class Contacts {

    // @Value("${dataDir}")
    // private String directory;

    private static final Logger logger = Logger.getLogger(Contacts.class.getName());

    private static final File dataDir = new File("src/main/resources/static/data");

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 64, message = "Length must be between 3 and 64")
    private String name;

    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Must be a valid email")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @NotEmpty(message = "Phone number cannot be empty")
    @Size(min = 7, message = "Phone number must contain at least 7 digits")
    private String phone;

    @NotNull(message = "DOB cannot be null")
    @Past(message = "DOB must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    // create a file with generated hex string
    public File createFile() {
        String filename = this.id + ".txt";
        if (!dataDir.exists()) {
            dataDir.mkdir();
        } else {
            logger.info("Directory already exists");
        }
        File file = new File(dataDir.getAbsolutePath() + File.separator + filename);
        logger.info("[Contacts] File %s is created".formatted(this.id));
        return file;
    }

    // write the data into the file
    public void writeFile(File file) throws FileNotFoundException, IOException {
        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);

        bw.write("Name: %s\n".formatted(this.name));
        bw.write("Email: %s\n".formatted(this.email));
        bw.write("Phone Number: %s\n".formatted(this.phone));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        bw.write("Date of Birth: %s\n".formatted(dateFormat.format(this.dob)));

        bw.flush();
        bw.close();

        logger.info("[Contacts] Data has been written into the file (%s)".formatted(file.getName()));

    }

    // task 4
    // look for the file in the directory
    public static File getFile(String id) {

        File[] files = dataDir.listFiles();
        for (File f : files) {
            if (f.getName().equals(id))
                return f;
        }
        
        return null;
    }

    public static List<String> getFiles(){
        List<String> fileList = new LinkedList<>();
        File[] files = dataDir.listFiles();
        for (File f : files){
            fileList.add(f.getName().split("[\\.]")[0]);
        }
        return fileList;
    }

    public static List<String> getContents(File file) throws IOException{

        // read contents of the file
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        String line = "x";
        List<String> result = new LinkedList<>();
        while (line != null){
            line = br.readLine();
            if (line == null)
                break;
            String get = line.split(":")[1];
            result.add(get);
        }

        return result;
    }

}

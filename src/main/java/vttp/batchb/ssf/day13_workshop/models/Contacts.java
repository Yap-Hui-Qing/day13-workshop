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

    // private static final File dataDir = new File("src/main/resources/static/data");

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

    @Override
    public String toString() {
        return "Contacts [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", dob=" + dob + "]";
    }

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


}

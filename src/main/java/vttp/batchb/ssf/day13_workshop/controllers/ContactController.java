package vttp.batchb.ssf.day13_workshop.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.batchb.ssf.day13_workshop.models.Contacts;
import static vttp.batchb.ssf.day13_workshop.models.Contacts.*;

@Controller
@RequestMapping
public class ContactController {

    @GetMapping(path = { "/", "/index.html" })
    public String getIndex(Model model) {
        model.addAttribute("contact", new Contacts());
        return "contact";
    }

    // task 3
    @PostMapping("/contact")
    public String postContact(@Valid @ModelAttribute("contact") Contacts contact, BindingResult bindings, Model model) {

        if (bindings.hasErrors()) {
            return "contact";
        }

        Date today = new Date();
        Date dob = contact.getDob();

        OffsetDateTime startOdt = dob.toInstant().atOffset(ZoneOffset.UTC);
        OffsetDateTime endOdt = today.toInstant().atOffset(ZoneOffset.UTC);

        int age = Period.between(startOdt.toLocalDate(), endOdt.toLocalDate()).getYears();

        if (age < 10 || age > 100) {
            FieldError err = new FieldError("contact", "dob",
                    "Cannot be younger than 10 years old and older than 100 years");
            bindings.addError(err);
            return "contact";
        }

        // part a
        String id = UUID.randomUUID().toString().substring(0, 8);
        contact.setId(id);

        // part b
        File file = contact.createFile();

        try {
            // part c
            contact.writeFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        model.addAttribute("id", id);
        return "created";
    }

    // task 4
    @GetMapping("/contact/{id}")
    public String getContact(@PathVariable String id, Model model) {

        File targetFile = getFile(id + ".txt");
        try {
            List<String> content = getContents(targetFile);
            model.addAttribute("id", id);
            model.addAttribute("name", content.get(0));
            model.addAttribute("email", content.get(1));
            model.addAttribute("phone", content.get(2));
            model.addAttribute("dob", content.get(3));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "contact-id";
    }

    @GetMapping("/contacts")
    public String getContacts(Model model){
        
        List<String> files = getFiles();
        List<String> names = new LinkedList<>();

        try{
            for (String s : files){
                File target = getFile(s + ".txt");
                List<String> content = getContents(target);
                names.add(content.get(0)); 
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
        model.addAttribute("names", names);
        model.addAttribute("files", files);
        return "contacts";

    }
}

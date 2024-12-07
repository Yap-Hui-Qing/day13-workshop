package vttp.batchb.ssf.day13_workshop.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.batchb.ssf.day13_workshop.ContactRedis;
import vttp.batchb.ssf.day13_workshop.models.Contacts;
import static vttp.batchb.ssf.day13_workshop.models.Contacts.*;

@Controller
@RequestMapping
public class ContactController {

    private static final Logger logger = Logger.getLogger(ContactController.class.getName());

    @Autowired
    private ContactRedis contactRedis;

    @GetMapping(path = { "/", "/index.html" })
    public String getIndex(Model model) {
        model.addAttribute("contact", new Contacts());
        return "contact";
    }

    // task 3
    @PostMapping("/contact")
    public String postContact(@Valid @ModelAttribute("contact") Contacts contact, BindingResult bindings, Model model) {

        logger.info("Contact: %s".formatted(contact.toString()));

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

        contactRedis.insertContact(contact);

        model.addAttribute("id", contact.getId());
        return "created";
    }

    // task 4
    @GetMapping("/contact/{id}")
    public ModelAndView getContact(@PathVariable String id, Model model) throws ParseException {

        ModelAndView mav = new ModelAndView();
        Optional<Contacts> opt = contactRedis.getContactById(id);
        
        //404
        if (opt.isEmpty()){
            mav.setViewName("not-found");
            mav.setStatus(HttpStatusCode.valueOf(404));
            mav.addObject("id", id);
            return mav;
        }

        Contacts contact = opt.get();
        // 200
        mav.setViewName("contact-id");
        mav.setStatus(HttpStatusCode.valueOf(200));
        mav.addObject("name", contact.getName());
        mav.addObject("email", contact.getEmail());
        mav.addObject("phone", contact.getPhone());
        mav.addObject("dob", contact.getDob());

        return mav;
    }

    @GetMapping("/contacts")
    public String getContacts(Model model){
        
        List<String> contactids = new ArrayList<>(contactRedis.getContactList());
        model.addAttribute("files", contactids);

        List<String> names = new LinkedList<>();
        for (String id : contactids){
            String name = contactRedis.getContactName(id);
            names.add(name);
        }
        model.addAttribute("names", names);

        return "contacts";

    }
}

package vttp.batchb.ssf.day13_workshop;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.batchb.ssf.day13_workshop.models.Contacts;

@Repository
public class ContactRedis {
    
    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    // hset abc123 name fred
    public void insertContact(Contacts contact){

        String id = UUID.randomUUID().toString().substring(0,8);
        contact.setId(id);

        HashOperations<String, String, String> hashOps = template.opsForHash();

        Map<String, String> values = new HashMap<>();
        values.put("name", contact.getName());
        values.put("email", contact.getEmail());
        values.put("phone", contact.getPhone());
        values.put("dob", contact.getDob().toString());

        hashOps.putAll(id, values);
    }

    public Optional<Contacts> getContactById(String id) throws ParseException{

        //hgetall abc123
        HashOperations<String, String, String> hashOps = template.opsForHash();
        Map<String, String> result = hashOps.entries(id);
        if (result.isEmpty())
            return Optional.empty();

        Contacts contact = new Contacts();
        contact.setName(result.get("name"));
        contact.setEmail(result.get("email"));
        contact.setPhone(result.get("phone"));

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        contact.setDob(sdf.parse(result.get("dob")));

        return Optional.of(contact);
    }

    // keys *
    public Set<String> getContactList(){
        return template.keys("*");
    }

    // hget abc123 name
    public String getContactName(String id){
        HashOperations<String, String, String> hashOps = template.opsForHash();
        return hashOps.get(id, "name");
    }
}

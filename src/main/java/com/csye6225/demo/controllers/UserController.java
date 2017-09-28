package com.csye6225.demo.controllers;


import com.csye6225.demo.UserRepository;
import com.csye6225.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
//@RequestMapping(path="/")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    @PostMapping("user/register") // Map ONLY POST Requests
    public @ResponseBody
    String addNewUser(@RequestParam String userName
            , @RequestParam String password) {

        System.out.println("String userName : " +userName);
        String salt= BCrypt.gensalt(10);
        String hashPwd = BCrypt.hashpw(password, salt);

        User user = userRepository.findByName(userName);

        User newUser = new User();

        if(user==null){
            if(validate(userName)){
                newUser.setPassword(hashPwd);
                newUser.setName(userName);
                userRepository.save(newUser);

                return "User created Successfully";
            }
            else
                return "User Name is not in Email format";
        }
        else{
            return "User already exists";
        }
    }


    @GetMapping("/")
    public @ResponseBody
    String login(@RequestParam String userName
            , @RequestParam String password) {

        User user = userRepository.findByName(userName);

        String dbname = user.getName();
        String dbpassowrd=user.getPassword();
        System.out.println("The username is " +dbname+"The password " +dbpassowrd);

        if(BCrypt.checkpw(password, dbpassowrd)) {
            String result ="you are logged in. current time is " + new Date().toString();
            return result;
        }
        else
            return "not logged in!";
      // return dbname;

    }


    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
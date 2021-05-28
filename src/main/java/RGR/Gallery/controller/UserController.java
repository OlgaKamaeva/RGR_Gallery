package RGR.Gallery.controller;

import RGR.Gallery.domain.Role;
import RGR.Gallery.domain.User;
import RGR.Gallery.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import RGR.Gallery.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService ;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private ResourceBundle resourceBundle;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("{user}/del")
    public String userDelete(@PathVariable User user) {
        String message = String.format(resourceBundle.getString("message.deleteUser"), user.getUsername());
        mailSender.send(user.getEmail(), "Photoalbum", message);
        userService.delete(user);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        userService.saveUser(user, form);

        return "redirect:/user";
    }


//    private final UserService userService;
//
//    public UserResource(UserService userService) {
//        this.userService = userService;
//    }
//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers(){
//        List<User> users = userService.findAllUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//    @GetMapping("/find/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable("id") Long id){
//        User user = userService.findUserById(id);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
//    @PostMapping("/add")
//    public  ResponseEntity<User> addUser(@RequestBody User user){
//        User newUser = userService.addUser(user);
//        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
//    }
//    @PutMapping("/update")
//    public  ResponseEntity<User> updateUser(@RequestBody User user){
//        User updateUser = userService.addUser(user);
//        return new ResponseEntity<>(updateUser, HttpStatus.OK);
//    }
//    @DeleteMapping("/delete/{id}")
//    public  ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
//        userService.deleteUser(id);
//        return new ResponseEntity<> (HttpStatus.OK);
//    }
}

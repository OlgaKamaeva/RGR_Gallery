package RGR.Gallery.controller;

import RGR.Gallery.domain.User;
import RGR.Gallery.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private PublicationRepository publicationRepository;

    @Value("/")
    private String uploadPath;

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user) {
        if (user.isAdmin() == true){
            return "redirect:/user";
        }   else
            return "redirect:/albums/" + user.getId();
    }

}

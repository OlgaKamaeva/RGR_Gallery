package RGR.Gallery.controller;

import RGR.Gallery.service.PublicationService;
import RGR.Gallery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FindController {
    @Autowired
    UserService userService;

    @Autowired
    PublicationService publicationService;


}

package RGR.Gallery.controller;

import RGR.Gallery.service.AlbumService;
import RGR.Gallery.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/albums")
public class AlbumController {
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    PublicationService publicationService;

    @Autowired
    AlbumService albumService;
}

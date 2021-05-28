package RGR.Gallery.controller;

import RGR.Gallery.service.AlbumService;
import RGR.Gallery.service.CommentsService;
import RGR.Gallery.service.MailSender;
import RGR.Gallery.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ResourceBundle;

@Controller
@RequestMapping("albums/album/image")
public class PublicationController {
    @Autowired
    MailSender mailSender;

    @Autowired
    PublicationService publicationService;

    @Autowired
    CommentsService commentsService;

    @Autowired
    AlbumService albumService;

    @Autowired
    private ResourceBundle resourceBundle;

}

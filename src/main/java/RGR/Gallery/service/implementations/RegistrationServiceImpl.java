package RGR.Gallery.service.implementations;

import RGR.Gallery.model.ConfirmationToken;
import RGR.Gallery.model.User;
import RGR.Gallery.service.interfaces.EmailService;
import RGR.Gallery.service.interfaces.RegistrationService;
import RGR.Gallery.validator.RegistrationValidator;
import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.stereotype.Service;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Join(path = "/", to = "/index.jsf")
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationValidator registrationValidator;
    private final UserServiceImpl userServiceImpl;
    private final ConfirmationTokenServiceImpl confirmationTokenServiceImpl;
    private final EmailService emailService;
    private User user = new User();
    private static final String LINK = "http://localhost:8080/confirm?token=";

    public RegistrationServiceImpl(RegistrationValidator registrationValidator, UserServiceImpl userServiceImpl, ConfirmationTokenServiceImpl confirmationTokenServiceImpl, EmailService emailService) {
        this.registrationValidator = registrationValidator;
        this.userServiceImpl = userServiceImpl;
        this.confirmationTokenServiceImpl = confirmationTokenServiceImpl;
        this.emailService = emailService;
    }

    public void register() throws IOException {
        String errorMessage = registrationValidator.validate(user);
        if (errorMessage != null) {
            throw  new IllegalStateException(errorMessage);
        }
        String token = userServiceImpl.save(user);
        String link = LINK + token;
        emailService.send(user.getEmail(),buildEmail(user.getFirstName(),link));
        this.user = new User();
        FacesContext.getCurrentInstance().getExternalContext().redirect("confirmRegistration.jsf");
    }

    public Boolean confirmToken(String token) {
        try {
            ConfirmationToken confirmationToken = confirmationTokenServiceImpl
                    .getToken(token)
                    .orElseThrow(() ->
                            new IllegalStateException("token not found"));

            if (confirmationToken.getConfirmedAt() != null) {
                throw new IllegalStateException("email already confirmed");
            }

            LocalDateTime expiredAt = confirmationToken.getExpiresAt();

            if (expiredAt.isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("token expired");
            }

            confirmationTokenServiceImpl.setConfirmedAt(confirmationToken.getConfirmationTokenId());
            userServiceImpl.enableUser(
                    confirmationToken.getUser().getUserId());
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 30 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

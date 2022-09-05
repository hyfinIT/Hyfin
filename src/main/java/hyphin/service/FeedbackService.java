package hyphin.service;

import hyphin.model.feedback.FeedbackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MailService mailService;

    public void handleFeedback(FeedbackDto feedbackDto) {
        String subject = "Feedback from " + feedbackDto.getName();
        String text = new StringBuilder(feedbackDto.getMessage())
                .append("<br />")
                .append("___________________________________")
                .append("<br />")
                .append("<small>Feedback from: ")
                .append(feedbackDto.getName())
                .append("<br />")
                .append("Organization: ")
                .append(feedbackDto.getOrganization())
                .append("<br />")
                .append("Tel.: ")
                .append(feedbackDto.getPhone())
                .append("<br />")
                .append("Email: ")
                .append("<a href=\"mailto:" + feedbackDto.getEmail() + "\">" + feedbackDto.getEmail() + "</a>")
                .append("</small>")
                .toString();

        try {
            mailService.sendMail("info@hyfin.com", subject , text);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

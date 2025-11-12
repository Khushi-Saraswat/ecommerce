package com.example.demo.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.demo.model.Feedback;
import com.example.demo.model.ProductOrder;
import com.example.demo.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    // for sending email- use javamailsender
    public Boolean sendMail(String url, String reciepentMail) throws UnsupportedEncodingException, MessagingException {

        // create a new email message object to be sent.
        MimeMessage message = mailSender.createMimeMessage();
        // helps to easily set details like sender,receiver,subject and content
        MimeMessageHelper helper = new MimeMessageHelper(message);
        // Sets the sender’s email and the display name (here: “Shopping Cart”).
        helper.setFrom("khushisaraswat69@gmail.com", "Shooping Cart");
        // Sets the recipient’s email address.
        helper.setTo(reciepentMail);
        // Creates the HTML content for the email, including a reset link.
        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
                + "\">Change my password</a></p>";
        // Sets the email subject line.
        helper.setSubject("Password Reset");
        // Sets the email body content and enables HTML formatting.
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public String generateUrl(HttpServletRequest request) {
        // Gets the full URL of the current HTTP request as a string.
        String siteUrl = request.getRequestURL().toString();
        // Removes the servlet path part from the URL to get only the base domain
        // (e.g.,https://example.com).
        return siteUrl.replace(request.getServletPath(), "");

        /*
         * The method takes the full request URL using
         * request.getRequestURL().toString(),
         * then removes the servlet path (request.getServletPath()) using .replace().
         * This returns only the base website URL
         * (e.g., https://example.com) from the full request link.
         */
    }

    String msg = null;

    public Boolean sendMailForProductOrder(ProductOrder order, String status)
            throws UnsupportedEncodingException, MessagingException {
        msg = "<p>Hello [[name]],</p>"
                + "<p>Thank you order <b>[[orderStatus]]</b>.</p>"
                + "<p><b>Product Details:</b></p>"
                + "<p>Name : [[productName]]</p>"
                + "<p>Category : [[category]]</p>"
                + "<p>Quantity : [[quantity]]</p>"
                + "<p>Price : [[price]]</p>"
                + "<p>Payment Type : [[paymentType]]</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("khushisaraswat69@gmail.com", "Trendify");
        helper.setTo(order.getOrderAddress().getEmail());
        msg = msg.replace("[[name]]", order.getOrderAddress().getFirstName());
        msg = msg.replace("[[orderStatus]]", status);
        msg = msg.replace("[[productName]]", order.getProduct().getTitle());
        msg = msg.replace("[[category]]", order.getProduct().getCategory());
        msg = msg.replace("[[quantity]]", order.getQuantity().toString());
        msg = msg.replace("[[price]]", order.getPrice().toString());
        msg = msg.replace("[[paymentType]]", order.getPaymentType());
        helper.setSubject("Password Order Status");
        helper.setText(msg, true);
        mailSender.send(message);
        return true;
    }

    String feedback = null;

    public Boolean sendMailForFeedBack(Feedback feedback) throws UnsupportedEncodingException, MessagingException {
        msg = "Subject: Thank You for Your Feedback!\n" + //
                "\n" + //
                "Dear [[User’s Name]],\n" + //
                "\n" + //
                "Thank you for taking the time to share your thoughts with us!\n" + //
                "\n" + //
                "We’ve received your feedback regarding [specific issue/feature]. Here’s a summary of what you told us: [Brief summary].\n"
                + //
                "\n" + //

                "\n" + //
                "If you have any additional questions or concerns, please feel free to contact us at [support email/phone number].\n"
                + //
                "\n" + //
                "Thank you once again for your valuable feedback. We appreciate your input and look forward to serving you better!\n"
                + //
                "\n" + //
                "Best regards,\n" + //
                "\n" + //
                "[Your Name]\n" + //
                "[Your Position]\n" + //
                "[Company Name]\n" + //
                "\n" + //
                "";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("khushisaraswat69@gmail.com", "Shooping Cart");
        helper.setTo(feedback.getEmail());
        msg = msg.replace("[[User’s Name]]", feedback.getFullName());
        msg = msg.replace("[specific issue/feature]", feedback.getCategory());
        msg = msg.replace("[Brief summary]", feedback.getMessage());
        msg = msg.replace("[Company Name]", "ECOM STORE");
        msg = msg.replace("[Your Name]", "Rohini Saraswat");
        msg = msg.replace("[Your Position]", "Product Manager");
        helper.setSubject("feed back");
        helper.setText(msg, true);
        mailSender.send(message);
        return true;
    }

    /*
     * public UserDtls getLoggedInUserDetails(Principal p) {
     * String email = p.getName();
     * UserDtls userDtls = userService.getUserByEmail(email);
     * return userDtls;
     * }
     */

}

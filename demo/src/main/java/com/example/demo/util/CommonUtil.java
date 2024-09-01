package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.demo.model.Feedback;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
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

    public Boolean sendMail(String url, String reciepentMail) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("khushisaraswat69@gmail.com", "Shooping Cart");
        helper.setTo(reciepentMail);
        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
                + "\">Change my password</a></p>";
        helper.setSubject("Password Reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public String generateUrl(HttpServletRequest request) {

        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");

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
        helper.setFrom("khushisaraswat69@gmail.com", "Shooping Cart");
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

    public UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        UserDtls userDtls = userService.getUserByEmail(email);
        return userDtls;
    }

}

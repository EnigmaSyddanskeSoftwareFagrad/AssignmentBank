/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Sanitas Solutions
 */
public class EmailHandler {

    private final String username;
    private final String password;
    private final String host;
    private final String port;

    private String getHTML(String password, String username) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'at' HH:mm:ss");;
        return "<!doctype html>\n"
                + "<html>\n"
                + "  <head>\n"
                + "    <meta name=\"viewport\" content=\"width=device-width\" />\n"
                + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                + "    <title>Simple Transactional Email</title>\n"
                + "    <style>\n"
                + "      /* -------------------------------------\n"
                + "          GLOBAL RESETS\n"
                + "      ------------------------------------- */\n"
                + "      \n"
                + "      /*All the styling goes here*/\n"
                + "      \n"
                + "      img {\n"
                + "        border: none;\n"
                + "        -ms-interpolation-mode: bicubic;\n"
                + "        max-width: 100%; \n"
                + "      }\n"
                + "      body {\n"
                + "        background-color: #f6f6f6;\n"
                + "        font-family: sans-serif;\n"
                + "        -webkit-font-smoothing: antialiased;\n"
                + "        font-size: 14px;\n"
                + "        line-height: 1.4;\n"
                + "        margin: 0;\n"
                + "        padding: 0;\n"
                + "        -ms-text-size-adjust: 100%;\n"
                + "        -webkit-text-size-adjust: 100%; \n"
                + "      }\n"
                + "      table {\n"
                + "        border-collapse: separate;\n"
                + "        mso-table-lspace: 0pt;\n"
                + "        mso-table-rspace: 0pt;\n"
                + "        width: 100%; }\n"
                + "        table td {\n"
                + "          font-family: sans-serif;\n"
                + "          font-size: 14px;\n"
                + "          vertical-align: top; \n"
                + "      }\n"
                + "      /* -------------------------------------\n"
                + "          BODY & CONTAINER\n"
                + "      ------------------------------------- */\n"
                + "      .body {\n"
                + "        background-color: #f6f6f6;\n"
                + "        width: 100%; \n"
                + "      }\n"
                + "      /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */\n"
                + "      .container {\n"
                + "        display: block;\n"
                + "        margin: 0 auto !important;\n"
                + "        /* makes it centered */\n"
                + "        max-width: 580px;\n"
                + "        padding: 10px;\n"
                + "        width: 580px; \n"
                + "      }\n"
                + "      /* This should also be a block element, so that it will fill 100% of the .container */\n"
                + "      .content {\n"
                + "        box-sizing: border-box;\n"
                + "        display: block;\n"
                + "        margin: 0 auto;\n"
                + "        max-width: 580px;\n"
                + "        padding: 10px; \n"
                + "      }\n"
                + "      /* -------------------------------------\n"
                + "          HEADER, FOOTER, MAIN\n"
                + "      ------------------------------------- */\n"
                + "      .main {\n"
                + "        background: #ffffff;\n"
                + "        border-radius: 3px;\n"
                + "        width: 100%; \n"
                + "      }\n"
                + "      .wrapper {\n"
                + "        box-sizing: border-box;\n"
                + "        padding: 20px; \n"
                + "      }\n"
                + "      .content-block {\n"
                + "        padding-bottom: 10px;\n"
                + "        padding-top: 10px;\n"
                + "      }\n"
                + "      .footer {\n"
                + "        clear: both;\n"
                + "        margin-top: 10px;\n"
                + "        text-align: center;\n"
                + "        width: 100%; \n"
                + "      }\n"
                + "        .footer td,\n"
                + "        .footer p,\n"
                + "        .footer span,\n"
                + "        .footer a {\n"
                + "          color: #999999;\n"
                + "          font-size: 12px;\n"
                + "          text-align: center; \n"
                + "      }\n"
                + "      /* -------------------------------------\n"
                + "          TYPOGRAPHY\n"
                + "      ------------------------------------- */\n"
                + "      h1,\n"
                + "      h2,\n"
                + "      h3,\n"
                + "      h4 {\n"
                + "        color: #000000;\n"
                + "        font-family: sans-serif;\n"
                + "        font-weight: 400;\n"
                + "        line-height: 1.4;\n"
                + "        margin: 0;\n"
                + "        margin-bottom: 30px; \n"
                + "      }\n"
                + "      h1 {\n"
                + "        font-size: 35px;\n"
                + "        font-weight: 300;\n"
                + "        text-align: center;\n"
                + "        text-transform: capitalize; \n"
                + "      }\n"
                + "      p,\n"
                + "      ul,\n"
                + "      ol {\n"
                + "        font-family: sans-serif;\n"
                + "        font-size: 14px;\n"
                + "        font-weight: normal;\n"
                + "        margin: 0;\n"
                + "        margin-bottom: 15px; \n"
                + "      }\n"
                + "        p li,\n"
                + "        ul li,\n"
                + "        ol li {\n"
                + "          list-style-position: inside;\n"
                + "          margin-left: 5px; \n"
                + "      }\n"
                + "      a {\n"
                + "        color: #3498db;\n"
                + "        text-decoration: underline; \n"
                + "      }\n"
                + "      /* -------------------------------------\n"
                + "          BUTTONS\n"
                + "      ------------------------------------- */\n"
                + "      .btn {\n"
                + "        box-sizing: border-box;\n"
                + "        width: 100%; }\n"
                + "        .btn > tbody > tr > td {\n"
                + "          padding-bottom: 15px; }\n"
                + "        .btn table {\n"
                + "          width: auto; \n"
                + "      }\n"
                + "        .btn table td {\n"
                + "          background-color: #ffffff;\n"
                + "          border-radius: 5px;\n"
                + "          text-align: center; \n"
                + "      }\n"
                + "        .btn a {\n"
                + "          background-color: #ffffff;\n"
                + "          border: solid 1px #3498db;\n"
                + "          border-radius: 5px;\n"
                + "          box-sizing: border-box;\n"
                + "          color: #3498db;\n"
                + "          cursor: pointer;\n"
                + "          display: inline-block;\n"
                + "          font-size: 14px;\n"
                + "          font-weight: bold;\n"
                + "          margin: 0;\n"
                + "          padding: 12px 25px;\n"
                + "          text-decoration: none;\n"
                + "          text-transform: capitalize; \n"
                + "      }\n"
                + "      .btn-primary table td {\n"
                + "        background-color: #3498db; \n"
                + "      }\n"
                + "      .btn-primary a {\n"
                + "        background-color: #3498db;\n"
                + "        border-color: #3498db;\n"
                + "        color: #ffffff; \n"
                + "      }\n"
                + "      /* -------------------------------------\n"
                + "          OTHER STYLES THAT MIGHT BE USEFUL\n"
                + "      ------------------------------------- */\n"
                + "      .last {\n"
                + "        margin-bottom: 0; \n"
                + "      }\n"
                + "      .first {\n"
                + "        margin-top: 0; \n"
                + "      }\n"
                + "      .align-center {\n"
                + "        text-align: center; \n"
                + "      }\n"
                + "      .align-right {\n"
                + "        text-align: right; \n"
                + "      }\n"
                + "      .align-left {\n"
                + "        text-align: left; \n"
                + "      }\n"
                + "      .clear {\n"
                + "        clear: both; \n"
                + "      }\n"
                + "      .mt0 {\n"
                + "        margin-top: 0; \n"
                + "      }\n"
                + "      .mb0 {\n"
                + "        margin-bottom: 0; \n"
                + "      }\n"
                + "      .preheader {\n"
                + "        color: transparent;\n"
                + "        display: none;\n"
                + "        height: 0;\n"
                + "        max-height: 0;\n"
                + "        max-width: 0;\n"
                + "        opacity: 0;\n"
                + "        overflow: hidden;\n"
                + "        mso-hide: all;\n"
                + "        visibility: hidden;\n"
                + "        width: 0; \n"
                + "      }\n"
                + "      .powered-by a {\n"
                + "        text-decoration: none; \n"
                + "      }\n"
                + "      hr {\n"
                + "        border: 0;\n"
                + "        border-bottom: 1px solid #f6f6f6;\n"
                + "        margin: 20px 0; \n"
                + "      }\n"
                + "      /* -------------------------------------\n"
                + "          RESPONSIVE AND MOBILE FRIENDLY STYLES\n"
                + "      ------------------------------------- */\n"
                + "      @media only screen and (max-width: 620px) {\n"
                + "        table[class=body] h1 {\n"
                + "          font-size: 28px !important;\n"
                + "          margin-bottom: 10px !important; \n"
                + "        }\n"
                + "        table[class=body] p,\n"
                + "        table[class=body] ul,\n"
                + "        table[class=body] ol,\n"
                + "        table[class=body] td,\n"
                + "        table[class=body] span,\n"
                + "        table[class=body] a {\n"
                + "          font-size: 16px !important; \n"
                + "        }\n"
                + "        table[class=body] .wrapper,\n"
                + "        table[class=body] .article {\n"
                + "          padding: 10px !important; \n"
                + "        }\n"
                + "        table[class=body] .content {\n"
                + "          padding: 0 !important; \n"
                + "        }\n"
                + "        table[class=body] .container {\n"
                + "          padding: 0 !important;\n"
                + "          width: 100% !important; \n"
                + "        }\n"
                + "        table[class=body] .main {\n"
                + "          border-left-width: 0 !important;\n"
                + "          border-radius: 0 !important;\n"
                + "          border-right-width: 0 !important; \n"
                + "        }\n"
                + "        table[class=body] .btn table {\n"
                + "          width: 100% !important; \n"
                + "        }\n"
                + "        table[class=body] .btn a {\n"
                + "          width: 100% !important; \n"
                + "        }\n"
                + "        table[class=body] .img-responsive {\n"
                + "          height: auto !important;\n"
                + "          max-width: 100% !important;\n"
                + "          width: auto !important; \n"
                + "        }\n"
                + "      }\n"
                + "      /* -------------------------------------\n"
                + "          PRESERVE THESE STYLES IN THE HEAD\n"
                + "      ------------------------------------- */\n"
                + "      @media all {\n"
                + "        .ExternalClass {\n"
                + "          width: 100%; \n"
                + "        }\n"
                + "        .ExternalClass,\n"
                + "        .ExternalClass p,\n"
                + "        .ExternalClass span,\n"
                + "        .ExternalClass font,\n"
                + "        .ExternalClass td,\n"
                + "        .ExternalClass div {\n"
                + "          line-height: 100%; \n"
                + "        }\n"
                + "        .apple-link a {\n"
                + "          color: inherit !important;\n"
                + "          font-family: inherit !important;\n"
                + "          font-size: inherit !important;\n"
                + "          font-weight: inherit !important;\n"
                + "          line-height: inherit !important;\n"
                + "          text-decoration: none !important; \n"
                + "        }\n"
                + "        .btn-primary table td:hover {\n"
                + "          background-color: #34495e !important; \n"
                + "        }\n"
                + "        .btn-primary a:hover {\n"
                + "          background-color: #34495e !important;\n"
                + "          border-color: #34495e !important; \n"
                + "        } \n"
                + "      }\n"
                + "    </style>\n"
                + "  </head>\n"
                + "  <body class=\"\">\n"
                + "    <span class=\"preheader\"></span>\n"
                + "    <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\">\n"
                + "      <tr>\n"
                + "        <td>&nbsp;</td>\n"
                + "        <td class=\"container\">\n"
                + "          <div class=\"content\">\n"
                + "\n"
                + "            <!-- START CENTERED WHITE CONTAINER -->\n"
                + "            <table role=\"presentation\" class=\"main\">\n"
                + "\n"
                + "              <!-- START MAIN CONTENT AREA -->\n"
                + "              <tr>\n"
                + "                <td class=\"wrapper\">\n"
                + "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                + "                    <tr>\n"
                + "                      <td>\n"
                + "                        <p style=\"text-align:center;\"><img src=\"https://i.imgur.com/owFgzll.png\" alt=\"Sanitas Overview Logo\" width=\"150\" height=\"60\"></p>\n"
                + "                        <h1>Sanitas Overview</h1>\n"
                + "                        <p>Here is your new password and your username for your Sanitas Overview account. You can change it in the program later</p>\n"
                + "                        <p>Username: " + username + "</p>\n"
                + "                        <p>Password: " + password + "</p>\n"
                + "                      </td>\n"
                + "                    </tr>\n"
                + "                  </table>\n"
                + "                </td>\n"
                + "              </tr>\n"
                + "\n"
                + "            <!-- END MAIN CONTENT AREA -->\n"
                + "            </table>\n"
                + "            <!-- END CENTERED WHITE CONTAINER -->\n"
                + "\n"
                + "            <!-- START FOOTER -->\n"
                + "            <div class=\"footer\">\n"
                + "              <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                + "                <tr>\n"
                + "                  <td class=\"content-block\">\n"
                + "                    <span class=\"apple-link\">Sanitas Overview</span>\n"
                + "                    <br> Didn't ask for this mail? Contact your administrator immidiately.\n"
                + "                    <br> Sent: " + formatter.format(LocalDateTime.now()) + "\n"
                + "                  </td>\n"
                + "                </tr>\n"
                + "              </table>\n"
                + "            </div>\n"
                + "            <!-- END FOOTER -->\n"
                + "\n"
                + "          </div>\n"
                + "        </td>\n"
                + "        <td>&nbsp;</td>\n"
                + "      </tr>\n"
                + "    </table>\n"
                + "  </body>\n"
                + "</html>";
    }

    /**
     * Contructs a mail handler
     *
     * @param host     the host address
     * @param port     the host port
     * @param username the host username
     * @param password the host password
     */
    public EmailHandler(String host, String port, String username, String password) {

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

    }

    /**
     * Sends a mail to the user based on the department domain with the username
     * and password
     *
     * @param reciptient   the username of the recipient
     * @param userPassword the new password of the recipient
     */
    public void sendMail(String reciptient, String userPassword) {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", port);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session) {
                @Override
                protected void updateMessageID() throws MessagingException {
                    setHeader("Message-ID", UUID.randomUUID().toString() + "-" + "Sanitas-Overview");
                }
            };
            message.setFrom(new InternetAddress("sanitasoverview@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reciptient));
            message.setSubject("Password Reset");

            String msg = getHTML(userPassword, reciptient.split("@")[0]);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

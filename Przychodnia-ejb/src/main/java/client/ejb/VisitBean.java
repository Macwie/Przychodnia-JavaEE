package client.ejb;

import client.model.Admission;
import client.model.Visit;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by Maciek on 2017-06-04.
 */
@Stateless
public class VisitBean implements VisitBeanRemote {

    @EJB
    VisitDao visit;

    @Override
    public void save(Visit visit) {
        this.visit.save(visit);
    }

    @Override
    public List<Visit> findAll() {
        return this.visit.findAll();
    }

    @Override
    public List<Visit> findRelatedTo(Admission admission) {
        return this.visit.findRelatedTo(admission);
    }

    @Override
    public List<Visit> findSoon(LocalDateTime date) {
        return visit.findSoon(date);
    }

    @Override
    public void remove(Long id) {
        this.visit.remove(id);
    }

    @Asynchronous
    //@Schedule (hour = "*", minute = "*/10")
    @Schedule(hour = "12")
    @Override
    public void sendEmail() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String mail = "";
        String subject = "";
        String body = "";

        LocalDateTime date = LocalDateTime.now();

        List<Visit> list = visit.findSoon(date);

        if(list.size() > 0)
        {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.wp.pl");
            props.put("mail.smtp.port", 465);
            props.put("mail.smtp.ssl.enable", true);
            Authenticator authenticator = null;
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication("jeetest@wp.pl", "Java1234567890");
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
            Session session = Session.getInstance(props, authenticator);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            try {
                for (int i=0;i<list.size();i++)
                {
                    mail = list.get(i).getPatient().getMail();
                    subject = "Nadchodzaca wizyta lekarska";
                    body = "Dzień dobry "+list.get(i).getPatient().getName()+" "+list.get(i).getPatient().getSurname()+". Przypominamy o wizycie ktora jest zaplanowana na "+list.get(i).getVDate().format(formatter)+". Pozdrawiamy!";
                }

                message.setFrom(new InternetAddress("jeetest@wp.pl"));
                InternetAddress[] address = {new InternetAddress(mail)};
                message.setRecipients(Message.RecipientType.TO, address);
                message.setSubject(subject);
                message.setSentDate(new Date());
                message.setText(body);
                Transport.send(message);
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        }


    }
}

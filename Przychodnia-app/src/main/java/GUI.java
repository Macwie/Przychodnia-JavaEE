import client.ejb.DoctorBeanRemote;
import client.ejb.SystemUserBeanRemote;
import client.model.Doctor;
import client.model.SystemUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by Maciek on 2017-05-21.
 */
public class GUI {

    private JButton zalogujButton;
    private JPanel AppView;
    private JPanel Login;
    private JPanel Register;
    private JTextField loginUsername;
    private JRadioButton registerRecepcja;
    private JRadioButton registerLekarz;
    private JComboBox registerLekarzBox;
    private JTextField registerUsername;
    private JPasswordField registerPassword1;
    private JButton zarejestrujButton;
    private JPasswordField loginPassword;
    private JPasswordField registerPassword2;

    private static InitialContext ic;
    private static JFrame frame;

    private static final Logger logger = LogManager.getLogger("MyLogger");

    public GUI() {

        ButtonGroup group = new ButtonGroup();
        group.add(registerRecepcja);
        group.add(registerLekarz);

        zalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Logowanie użytkownika");

                String username = loginUsername.getText();
                String password = String.valueOf(loginPassword.getPassword());

                java.security.MessageDigest md = null;
                StringBuffer sb = new StringBuffer();
                try {
                    md = java.security.MessageDigest.getInstance("MD5");
                    byte[] array = md.digest(password.getBytes());
                    for (int i = 0; i < array.length; ++i) {
                        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));}
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }

                password = sb.toString();

                try {
                    SystemUserBeanRemote system_user = (SystemUserBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/SystemUserBean!client.ejb.SystemUserBeanRemote");

                    try
                    {
                        SystemUser user = system_user.findUser(username, password);

                        if(user.getDoctor() == null)
                        {
                            frame.setContentPane(new RegistrationView().getRegistrationView());
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.pack();
                            frame.setVisible(true);
                        }
                        else
                        {
                            DoctorsView.setDoctor(user.getDoctor());

                            frame.setContentPane(new DoctorsView().getDoctorsView());
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.pack();
                            frame.setVisible(true);
                        }

                    }
                    catch (Exception err)
                    {
                        JOptionPane.showMessageDialog(frame, "Nie ma takiego użytkownika!");
                        err.printStackTrace();
                    }

                } catch (NamingException e1) {
                    e1.printStackTrace();
                }


            }
        });

        zarejestrujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Rejestracja użytkownika");

                String username = registerUsername.getText();
                String password1 = String.valueOf(registerPassword1.getPassword());
                String password2 = String.valueOf(registerPassword2.getPassword());
                java.security.MessageDigest md = null;
                StringBuffer sb = new StringBuffer();
                try {
                    md = java.security.MessageDigest.getInstance("MD5");
                    byte[] array = md.digest(password1.getBytes());
                    for (int i = 0; i < array.length; ++i) {
                        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));}
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }

                if(!password1.equals(password2))
                {
                    JOptionPane.showMessageDialog(frame, "Podano różne hasła!");
                    return;
                }

                password1 = sb.toString();

                try {
                    SystemUserBeanRemote system_user = (SystemUserBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/SystemUserBean!client.ejb.SystemUserBeanRemote");
                    DoctorBeanRemote doctor = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");
                    SystemUser user = new SystemUser();
                    user.setLogin(username);
                    user.setPassword(password1);

                    if(registerLekarz.isSelected())
                    {
                        String[] temp = registerLekarzBox.getSelectedItem().toString().split(" ");
                        user.setDoctor(doctor.findDoctor(temp[0], temp[1]));
                    }

                    try
                    {
                        system_user.findUser(username, password1);
                        JOptionPane.showMessageDialog(frame, "Użytkownik o takich danych już istnieje!");
                    }
                    catch(Exception err)
                    {
                        system_user.save(user);
                        JOptionPane.showMessageDialog(frame, "Pomyślnie zarejestrowano nowego użytkownika!");
                    }

                } catch (NamingException e1) {
                    e1.printStackTrace();
                }

            }
        });

        registerLekarz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Rejestracja użytkownika (lekarz)");

                registerLekarzBox.setEnabled(true);

                if(registerLekarzBox.getItemCount() > 0)
                    return;

                try {
                    DoctorBeanRemote doctorComboBoxData = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");

                    List<Doctor> l = doctorComboBoxData.findAll();

                    for (int i=0 ;i<l.size();i++)
                    {
                        registerLekarzBox.addItem(l.get(i).getName()+" "+l.get(i).getSurname());
                    }


                } catch (NamingException err) {
                    err.printStackTrace();
                }

            }
        });

        registerRecepcja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerLekarzBox.setEnabled(false);
            }
        });
    }

    public static void main(String[] args) throws NamingException {

        logger.trace("Uruchomienie programu");

        frame = new JFrame("Przychodnia");
        frame.setContentPane(new GUI().AppView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");

        ic = new InitialContext(jndiProperties);


    }
}

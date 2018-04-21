import client.ejb.AdmissionBeanRemote;
import client.ejb.DoctorBeanRemote;
import client.ejb.PatientBeanRemote;
import client.ejb.VisitBeanRemote;
import client.model.Admission;
import client.model.Doctor;
import client.model.Patient;
import client.model.Visit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.SourceType;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Maciek on 2017-05-29.
 */
public class RegistrationView {
    private JPanel RegistrationView;
    private JPanel Doctors;
    private JPanel Visits;
    private JButton dodajButton;
    private JButton modyfikujButton;
    private JButton usuńButton;
    private JButton modyfikujButton1;
    private JButton dodajButton1;
    private JButton usuńButton1;
    private JTable doctorsTable;
    private JTable visitsTable;
    private JTable placesTermsTable;
    private JPanel PlacesTerms;
    private JButton dodajButton2;
    private JButton usuńButton2;

    private DefaultTableModel modelDoc;
    private DefaultTableModel modelVis;
    private DefaultTableModel modelPlacesTerms;

    private static InitialContext ic;

    private static List<String> selectedRowDoctor;
    private static List<String> selectedRowPlacesTerms;
    private static List<String> selectedRowVisits;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Logger logger = LogManager.getLogger("MyLogger");

    public RegistrationView() throws NamingException {

        logger.trace("Przejście do widoku recepcji");

        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");

        ic = new InitialContext(jndiProperties);

        String[] doctorsColumns = {"Id", "Imie", "Nazwisko", "Specjalizacja"};
        String[] visitsColumns = {"Id", "Imie", "Nazwisko", "Data", "Pesel", "Email"};
        String[] placesTermsColumns = {"Id", "Dzień", "Miejsce", "Początek przyjęć", "Koniec przyjęć"};

        modelDoc = new DefaultTableModel();

        modelDoc.setColumnIdentifiers(doctorsColumns);


        modelVis = new DefaultTableModel();

        modelVis.setColumnIdentifiers(visitsColumns);

        modelPlacesTerms = new DefaultTableModel();

        modelPlacesTerms.setColumnIdentifiers(placesTermsColumns);

        doctorsTable.setModel(modelDoc);
        visitsTable.setModel(modelVis);
        placesTermsTable.setModel(modelPlacesTerms);
        visitsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        visitsTable.setRowHeight(25);
        doctorsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        doctorsTable.setRowHeight(25);
        placesTermsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        placesTermsTable.setRowHeight(25);

        DoctorBeanRemote doctorsData = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");

        List<Doctor> l = doctorsData.findAll();

        for (int i=0;i<l.size();i++)
        {
                Object[] temp = new Object[]{"","","",""};
                temp[0] = l.get(i).getId();
                temp[1] = l.get(i).getName();
                temp[2] = l.get(i).getSurname();
                temp[3] = l.get(i).getSpecialization();
                modelDoc.addRow(temp);
        }


        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Dodanie nowego lekarza");

                List<String> daysList = new ArrayList<>();
                daysList.add("Poniedzialek");daysList.add("Wtorek");daysList.add("Sroda");daysList.add("Czwartek");daysList.add("Piątek");daysList.add("Sobota");daysList.add("Niedziela");

                JPanel panel = new JPanel(new GridLayout(0, 1));
                JTextField name = new JTextField();
                JTextField surname = new JTextField();
                JTextField spec = new JTextField();
                JTextField place = new JTextField();
                JComboBox days = new JComboBox();
                JTextField startTime = new JTextField();
                JTextField endTime = new JTextField();

                for (String s: daysList)
                    days.addItem(s);

                panel.add(new JLabel("Imie:"));
                panel.add(name);
                panel.add(new JLabel("Nazwisko:"));
                panel.add(surname);
                panel.add(new JLabel("Specjalizacja:"));
                panel.add(spec);
                panel.add(new JLabel("Miejsce przyjęć:"));
                panel.add(place);
                panel.add(new JLabel("Dzień:"));
                panel.add(days);
                panel.add(new JLabel("Początek przyjęć (HH:MM:SS):"));
                panel.add(startTime);
                panel.add(new JLabel("Koniec przyjęć (HH:MM:SS):"));
                panel.add(endTime);

                int result = JOptionPane.showConfirmDialog(null, panel, "Dodaj lekarza",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION)
                {

                    try {
                        DoctorBeanRemote doctorsData = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");

                        Doctor d = new Doctor();

                        d.setName(name.getText());
                        d.setSurname(surname.getText());
                        d.setSpecialization(spec.getText());

                        Admission admission = new Admission();
                        admission.setPlace(place.getText());
                        admission.setDay(days.getSelectedItem().toString());
                        admission.setStartTime(LocalTime.parse(startTime.getText()));
                        admission.setEndTime(LocalTime.parse(endTime.getText()));
                        admission.setDoctor(d);

                        d.addAdmission(admission);
                        doctorsData.save(d);

                        d = doctorsData.findDoctor(d.getName(), d.getSurname());

                        modelDoc.addRow(new Object[] {d.getId(), d.getName(), d.getSurname(), d.getSpecialization()});
                        modelDoc.fireTableDataChanged();

                        modelPlacesTerms.setRowCount(0);
                        modelVis.setRowCount(0);

                    } catch (NamingException e1) {
                        e1.printStackTrace();
                    }

                }


            }
        });

        doctorsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                usuńButton.setEnabled(true);
                dodajButton1.setEnabled(true);
                modyfikujButton.setEnabled(true);
                DefaultTableModel temp = (DefaultTableModel) doctorsTable.getModel();
                Object tempRow = temp.getDataVector().elementAt(doctorsTable.getSelectedRow());
                List<String> tempList = Arrays.asList(tempRow.toString().split("\\s*,\\s*"));
                selectedRowDoctor = new ArrayList<>(tempList);
                selectedRowDoctor.set(0, selectedRowDoctor.get(0).replace("[", ""));
                selectedRowDoctor.set(selectedRowDoctor.size()-1, selectedRowDoctor.get(selectedRowDoctor.size()-1).replace("]", ""));
                selectedRowDoctor.add(String.valueOf(doctorsTable.getSelectedRow()));

                /*Wyświeltanie wizyt zwiazanych z zaznaczonym lekarzem*/

                try {
                    AdmissionBeanRemote admissionData = (AdmissionBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/AdmissionBean!client.ejb.AdmissionBeanRemote");
                    DoctorBeanRemote doctorsData = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");

                    Doctor d = doctorsData.findDoctor(selectedRowDoctor.get(1), selectedRowDoctor.get(2));

                    List<Admission> list = admissionData.findRelatedTo(d);

                    modelPlacesTerms.setRowCount(0);
/*
                    for(int i=0;i<modelPlacesTerms.getRowCount();i++)
                        modelPlacesTerms.removeRow(i);
*/
                    for (Admission a : list)
                        modelPlacesTerms.addRow(new Object[] {a.getId(), a.getDay(), a.getPlace(), a.getStartTime(), a.getEndTime()});

                    modelPlacesTerms.fireTableDataChanged();
                    modelVis.setRowCount(0);


                } catch (NamingException e1) {
                    e1.printStackTrace();
                }



/*
                for (int i=0 ;i<selectedRowDoctor.size();i++)
                {
                    System.out.println(selectedRowDoctor.get(i));
                }
*/
            }
         });

        usuńButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Usunięcie lekarza");

                try {
                    DoctorBeanRemote doctorsData = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");


                    //System.out.println(Long.valueOf(selectedRowDoctor.get(0)));
                    //System.out.println("Row:"+Integer.valueOf(selectedRowDoctor.get(selectedRowDoctor.size()-1)));

                    doctorsData.remove(Long.valueOf(selectedRowDoctor.get(0)));

                    modelDoc.removeRow(Integer.valueOf(selectedRowDoctor.get(selectedRowDoctor.size()-1)));
                    modelDoc.fireTableDataChanged();
                    modelPlacesTerms.setRowCount(0);
                    modelVis.setRowCount(0);
                    usuńButton.setEnabled(false);
                    modyfikujButton.setEnabled(false);


                } catch (NamingException e1) {
                    e1.printStackTrace();
                }
            }
        });
        dodajButton1.addActionListener(new ActionListener() {   //dodaj miejsce/termin
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Dodanie nowego miejsca i godzin przyjęć");

                List<String> daysList = new ArrayList<>();
                daysList.add("Poniedzialek");daysList.add("Wtorek");daysList.add("Sroda");daysList.add("Czwartek");daysList.add("Piątek");daysList.add("Sobota");daysList.add("Niedziela");

                JPanel panel = new JPanel(new GridLayout(0, 1));

                JTextField place = new JTextField();
                JComboBox days = new JComboBox();
                JTextField startTime = new JTextField();
                JTextField endTime = new JTextField();

                for (String s: daysList)
                    days.addItem(s);

                panel.add(new JLabel("Miejsce przyjęć:"));
                panel.add(place);
                panel.add(new JLabel("Dzień:"));
                panel.add(days);
                panel.add(new JLabel("Początek przyjęć (HH:MM:SS):"));
                panel.add(startTime);
                panel.add(new JLabel("Koniec przyjęć (HH:MM:SS):"));
                panel.add(endTime);

                int result = JOptionPane.showConfirmDialog(null, panel, "Dodaj miejsce/termin",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION)
                {
                    try {

                        AdmissionBeanRemote admissionData = (AdmissionBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/AdmissionBean!client.ejb.AdmissionBeanRemote");
                        DoctorBeanRemote doctorsData = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");

                        Doctor d = doctorsData.findDoctor(selectedRowDoctor.get(1), selectedRowDoctor.get(2));

                        Admission admission = new Admission();
                        admission.setPlace(place.getText());
                        admission.setDay(days.getSelectedItem().toString());
                        admission.setStartTime(LocalTime.parse(startTime.getText()));
                        admission.setEndTime(LocalTime.parse(endTime.getText()));
                        admission.setDoctor(d);

                        admissionData.save(admission);

                        modelPlacesTerms.setRowCount(0);

                        List<Admission> list = admissionData.findRelatedTo(d);
/*
                        for(int i=0;i<modelPlacesTerms.getRowCount();i++)
                            modelPlacesTerms.removeRow(i);
*/
                        for (Admission a : list)
                            modelPlacesTerms.addRow(new Object[] {a.getId(), a.getDay(), a.getPlace(), a.getStartTime(), a.getEndTime()});

                        modelPlacesTerms.fireTableDataChanged();


                    } catch (NamingException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        });
        modyfikujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Edycja danych lekarza");

                JPanel panel = new JPanel(new GridLayout(0, 1));
                JTextField name = new JTextField(selectedRowDoctor.get(1));
                JTextField surname = new JTextField(selectedRowDoctor.get(2));
                JTextField spec = new JTextField(selectedRowDoctor.get(3));

                panel.add(new JLabel("Imie:"));
                panel.add(name);
                panel.add(new JLabel("Nazwisko:"));
                panel.add(surname);
                panel.add(new JLabel("Specjalizacja:"));
                panel.add(spec);

                int result = JOptionPane.showConfirmDialog(null, panel, "Modyfikuj lekarza",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION)
                {

                    try {
                        DoctorBeanRemote doctorsData = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");

                        Doctor d = new Doctor();

                        d.setId(Long.valueOf(selectedRowDoctor.get(0)));
                        d.setName(name.getText());
                        d.setSurname(surname.getText());
                        d.setSpecialization(spec.getText());

                        doctorsData.update(d);

                        d = doctorsData.findDoctor(d.getName(), d.getSurname());

                        modelDoc.setValueAt(d.getId(), Integer.valueOf(selectedRowDoctor.get(selectedRowDoctor.size()-1)), 0);
                        modelDoc.setValueAt(d.getName(), Integer.valueOf(selectedRowDoctor.get(selectedRowDoctor.size()-1)), 1);
                        modelDoc.setValueAt(d.getSurname(), Integer.valueOf(selectedRowDoctor.get(selectedRowDoctor.size()-1)), 2);
                        modelDoc.setValueAt(d.getSpecialization(), Integer.valueOf(selectedRowDoctor.get(selectedRowDoctor.size()-1)), 3);
                        modelDoc.fireTableDataChanged();

                    } catch (NamingException e1) {
                        e1.printStackTrace();
                    }

                }
                modyfikujButton.setEnabled(false);
                usuńButton.setEnabled(false);
            }
        });
        placesTermsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                DefaultTableModel temp = (DefaultTableModel) placesTermsTable.getModel();
                Object tempRow = temp.getDataVector().elementAt(placesTermsTable.getSelectedRow());
                List<String> tempList = Arrays.asList(tempRow.toString().split("\\s*,\\s*"));
                selectedRowPlacesTerms = new ArrayList<>(tempList);
                selectedRowPlacesTerms.set(0, selectedRowPlacesTerms.get(0).replace("[", ""));
                selectedRowPlacesTerms.set(selectedRowPlacesTerms.size()-1, selectedRowPlacesTerms.get(selectedRowPlacesTerms.size()-1).replace("]", ""));
                selectedRowPlacesTerms.add(String.valueOf(placesTermsTable.getSelectedRow()));

                try {

                    AdmissionBeanRemote admissionData = (AdmissionBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/AdmissionBean!client.ejb.AdmissionBeanRemote");
                    VisitBeanRemote visitData = (VisitBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/VisitBean!client.ejb.VisitBeanRemote");


                    Admission admission = admissionData.findAdmission(Long.valueOf(selectedRowPlacesTerms.get(0)));

                    List<Visit> list = visitData.findRelatedTo(admission);

                    modelVis.setRowCount(0);

                    for (Visit v : list)
                        modelVis.addRow(new Object[] {v.getId(), v.getPatient().getName(), v.getPatient().getSurname(), v.getVDate().format(formatter), v.getPatient().getPesel(), v.getPatient().getMail()});

                    modelVis.fireTableDataChanged();

                } catch (NamingException e1) {
                    e1.printStackTrace();
                }


                usuńButton1.setEnabled(true);
                modyfikujButton1.setEnabled(true);
                dodajButton2.setEnabled(true);

            }
        });
        usuńButton1.addActionListener(new ActionListener() {    //usun miejsce/termin
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Usunięcie miejsca i godzin przyjęć");

                try {

                    AdmissionBeanRemote admissionData = (AdmissionBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/AdmissionBean!client.ejb.AdmissionBeanRemote");

                    admissionData.remove(Long.valueOf(selectedRowPlacesTerms.get(0)));

                    modelPlacesTerms.removeRow(Integer.valueOf(selectedRowPlacesTerms.get(selectedRowPlacesTerms.size()-1)));
                    modelPlacesTerms.fireTableDataChanged();

                    usuńButton1.setEnabled(false);

                } catch (NamingException e1) {
                    e1.printStackTrace();
                }
            }
        });
        modyfikujButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Edycja danych dotyczących miejsca i godzin przyjęć");

                List<String> daysList = new ArrayList<>();
                daysList.add("Poniedzialek");daysList.add("Wtorek");daysList.add("Sroda");daysList.add("Czwartek");daysList.add("Piątek");daysList.add("Sobota");daysList.add("Niedziela");

                JPanel panel = new JPanel(new GridLayout(0, 1));

                JTextField place = new JTextField(selectedRowPlacesTerms.get(2));
                JComboBox days = new JComboBox();
                JTextField startTime = new JTextField(selectedRowPlacesTerms.get(3));
                JTextField endTime = new JTextField(selectedRowPlacesTerms.get(4));

                for (String s: daysList)
                    days.addItem(s);

                panel.add(new JLabel("Miejsce przyjęć:"));
                panel.add(place);
                panel.add(new JLabel("Dzień:"));
                panel.add(days);
                panel.add(new JLabel("Początek przyjęć (HH:MM:SS):"));
                panel.add(startTime);
                panel.add(new JLabel("Koniec przyjęć (HH:MM:SS):"));
                panel.add(endTime);

                int result = JOptionPane.showConfirmDialog(null, panel, "Modyfikuj miejsce/termin",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION)
                {
                    try {

                        AdmissionBeanRemote admissionData = (AdmissionBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/AdmissionBean!client.ejb.AdmissionBeanRemote");
                        DoctorBeanRemote doctorsData = (DoctorBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/DoctorBean!client.ejb.DoctorBeanRemote");

                        Doctor d = doctorsData.findDoctor(selectedRowDoctor.get(1), selectedRowDoctor.get(2));

                        Admission admission = new Admission();
                        admission.setId(Long.valueOf(selectedRowPlacesTerms.get(0)));
                        admission.setPlace(place.getText());
                        admission.setDay(days.getSelectedItem().toString());
                        admission.setStartTime(LocalTime.parse(startTime.getText()));
                        admission.setEndTime(LocalTime.parse(endTime.getText()));
                        admission.setDoctor(d);

                        admissionData.update(admission);

                        modelPlacesTerms.setValueAt(admission.getId(), Integer.valueOf(selectedRowPlacesTerms.get(selectedRowPlacesTerms.size()-1)), 0);
                        modelPlacesTerms.setValueAt(admission.getDay(), Integer.valueOf(selectedRowPlacesTerms.get(selectedRowPlacesTerms.size()-1)), 1);
                        modelPlacesTerms.setValueAt(admission.getPlace(), Integer.valueOf(selectedRowPlacesTerms.get(selectedRowPlacesTerms.size()-1)), 2);
                        modelPlacesTerms.setValueAt(admission.getStartTime(), Integer.valueOf(selectedRowPlacesTerms.get(selectedRowPlacesTerms.size()-1)), 3);
                        modelPlacesTerms.setValueAt(admission.getEndTime(), Integer.valueOf(selectedRowPlacesTerms.get(selectedRowPlacesTerms.size()-1)), 4);
                        modelPlacesTerms.fireTableDataChanged();

                        modyfikujButton1.setEnabled(false);
                        usuńButton1.setEnabled(false);


                    } catch (NamingException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });
        dodajButton2.addActionListener(new ActionListener() {   //dodawanie wizyt do określonego miejsca/terminu
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Dodanie nowej wizyty");

                JPanel panel = new JPanel(new GridLayout(0, 1));

                JTextField name = new JTextField();
                JTextField surname = new JTextField();
                JTextField pesel = new JTextField();
                JTextField mail = new JTextField();
                JTextField time = new JTextField();

                panel.add(new JLabel("Imie:"));
                panel.add(name);
                panel.add(new JLabel("Nazwisko:"));
                panel.add(surname);
                panel.add(new JLabel("Pesel:"));
                panel.add(pesel);
                panel.add(new JLabel("Adres email:"));
                panel.add(mail);
                panel.add(new JLabel("Data i godzina wizyty (YYYY-MM-DD HH:MM):"));
                panel.add(time);

                int result = JOptionPane.showConfirmDialog(null, panel, "Dodaj wizytę",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION)
                {
                    try {

                        AdmissionBeanRemote admissionData = (AdmissionBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/AdmissionBean!client.ejb.AdmissionBeanRemote");
                        VisitBeanRemote visitData = (VisitBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/VisitBean!client.ejb.VisitBeanRemote");
                        PatientBeanRemote patientData = (PatientBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/PatientBean!client.ejb.PatientBeanRemote");


                        Patient patient = new Patient();
                        patient.setName(name.getText());
                        patient.setSurname(surname.getText());
                        patient.setPesel(pesel.getText());
                        patient.setMail(mail.getText());

                        Visit visit = new Visit();
                        visit.setVDate(LocalDateTime.parse(time.getText(), formatter));
                        visit.setPatient(patient);
                        Admission admission = admissionData.findAdmission(Long.valueOf(selectedRowPlacesTerms.get(0)));
                        visit.setAdmission(admission);

                        patient.addVisit(visit);
                        patientData.save(patient);

                        //visitData.save(visit);

                        modelVis.setRowCount(0);

                        List<Visit> list = visitData.findRelatedTo(admission);

                        for (Visit v : list)
                            modelVis.addRow(new Object[] {v.getId(), v.getPatient().getName(), v.getPatient().getSurname(), v.getVDate().format(formatter), v.getPatient().getPesel(), v.getPatient().getMail()});


                        modelVis.fireTableDataChanged();

                        modyfikujButton1.setEnabled(false);
                        usuńButton1.setEnabled(false);


                    } catch (NamingException e1) {
                        e1.printStackTrace();
                    }

                }

            }
        });
        visitsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                DefaultTableModel temp = (DefaultTableModel) visitsTable.getModel();
                Object tempRow = temp.getDataVector().elementAt(visitsTable.getSelectedRow());
                List<String> tempList = Arrays.asList(tempRow.toString().split("\\s*,\\s*"));
                selectedRowVisits = new ArrayList<>(tempList);
                selectedRowVisits.set(0, selectedRowVisits.get(0).replace("[", ""));
                selectedRowVisits.set(selectedRowVisits.size()-1, selectedRowVisits.get(selectedRowVisits.size()-1).replace("]", ""));
                selectedRowVisits.add(String.valueOf(visitsTable.getSelectedRow()));

                usuńButton2.setEnabled(true);

            }
        });
        usuńButton2.addActionListener(new ActionListener() {    //usuwanie wizyt
            @Override
            public void actionPerformed(ActionEvent e) {

                logger.trace("Usunięcie wizyty");

                try {

                    VisitBeanRemote visitsData = (VisitBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/VisitBean!client.ejb.VisitBeanRemote");

                    visitsData.remove(Long.valueOf(selectedRowVisits.get(0)));

                    modelVis.removeRow(Integer.valueOf(selectedRowVisits.get(selectedRowVisits.size()-1)));
                    modelVis.fireTableDataChanged();

                    usuńButton2.setEnabled(false);

                } catch (NamingException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public JPanel getRegistrationView() {
        return RegistrationView;
    }

    public void setRegistrationView(JPanel registrationView) {
        RegistrationView = registrationView;
    }
}

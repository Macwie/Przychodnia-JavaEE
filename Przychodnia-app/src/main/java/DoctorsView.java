import client.ejb.AdmissionBeanRemote;
import client.ejb.VisitBeanRemote;
import client.model.Admission;
import client.model.Doctor;
import client.model.Visit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by macwi on 01.06.2017.
 */
public class DoctorsView {
    private JTable visitsTable;
    private JPanel doctorsView;
    private JLabel doctorName;
    private JButton usuńWizytęButton;

    private DefaultTableModel modelVis;
    private static List<String> selectedRowVisits;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static InitialContext ic;

    private static Doctor doctor;

    private static final Logger logger = LogManager.getLogger("MyLogger");

    public DoctorsView() throws NamingException {

        logger.trace("Przejscie do widoku lekarza");

        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");

        ic = new InitialContext(jndiProperties);

        doctorName.setText("Zalogowano na konto: "+doctor.getName()+" "+doctor.getSurname());

        String[] visitsColumns = {"Id", "Imie", "Nazwisko", "Data", "Pesel", "Email"};

        modelVis = new DefaultTableModel();
        modelVis.setColumnIdentifiers(visitsColumns);
        visitsTable.setModel(modelVis);
        visitsTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        visitsTable.setRowHeight(25);

        VisitBeanRemote visitData = (VisitBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/VisitBean!client.ejb.VisitBeanRemote");
        AdmissionBeanRemote admissionData = (AdmissionBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/AdmissionBean!client.ejb.AdmissionBeanRemote");


        List<Admission> admissions = admissionData.findRelatedTo(doctor);

        List<Visit> list = new ArrayList<>();
        for (int i=0;i<admissions.size();i++)
        {
            list.addAll(visitData.findRelatedTo(admissions.get(i)));
        }


        Collections.sort(list, new Comparator<Visit>() {
            public int compare(Visit v1, Visit v2) {
                if (v1.getVDate() == null || v2.getVDate() == null)
                    return 0;
                return v1.getVDate().getDayOfMonth()-v2.getVDate().getDayOfMonth();
            }
        });

        modelVis.setRowCount(0);

        for (Visit v : list)
            modelVis.addRow(new Object[] {v.getId(), v.getPatient().getName(), v.getPatient().getSurname(), v.getVDate().format(formatter), v.getPatient().getPesel(), v.getPatient().getMail()});

        modelVis.fireTableDataChanged();

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

                usuńWizytęButton.setEnabled(true);
            }
        });

        usuńWizytęButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    logger.trace("Usuniecie wizyty (lekarz)");

                    VisitBeanRemote visitsData = (VisitBeanRemote) ic.lookup("/Przychodnia-ear-1.0-SNAPSHOT/Przychodnia-ejb-1.0-SNAPSHOT/VisitBean!client.ejb.VisitBeanRemote");

                    visitsData.remove(Long.valueOf(selectedRowVisits.get(0)));

                    modelVis.removeRow(Integer.valueOf(selectedRowVisits.get(selectedRowVisits.size()-1)));
                    modelVis.fireTableDataChanged();

                    usuńWizytęButton.setEnabled(false);

                } catch (NamingException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public JPanel getDoctorsView() {
        return doctorsView;
    }

    public static Doctor getDoctor() {
        return doctor;
    }

    public static void setDoctor(Doctor doctor) {
        DoctorsView.doctor = doctor;
    }
}

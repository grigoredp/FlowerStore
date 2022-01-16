package proiect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Plafar {
    private JButton btnLogin;
    private JPanel panouPrincipal;
    private JTextField fieldUser;
    private JLabel labelUser;
    private JLabel labelPass;
    private JPasswordField fieldPass;
    private JTabbedPane panouTab;
    private JPanel panouEvidenta;
    private JPanel panouLogin;
    private JTable tabelPlante;
    private JButton btnCumparare;
    private JSpinner spinnerCantitate;
    private JLabel labelSpinner;
    private JButton btnActualizare;
    private JButton btnStoc;
    private JTextField fieldAddDen;
    private JSpinner spinAddCant;
    private JSpinner spinAddPret;
    private JButton btnAddPlanta;
    private JButton btnEliminare;

    public Plafar() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Incercare: " + fieldUser.getText() + " - " + String.valueOf(fieldPass.getPassword()));
                if (fieldUser.getText().equals("admin") && String.valueOf(fieldPass.getPassword()).equals("1234")){
                    JOptionPane.showMessageDialog(null, "Bine ai venit!");
                    panouTab.setEnabledAt(1, true);
                }
            }
        });
        btnActualizare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = "src\\proiect\\PlanteVandute.txt";
                File file = new File(filePath);
                tabelPlante.setModel(new DefaultTableModel());
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));

                    String primaLinie = br.readLine().trim();
                    String[] coloane = primaLinie.split(",");
                    DefaultTableModel model = (DefaultTableModel) tabelPlante.getModel();
                    model.setColumnIdentifiers(coloane);

                    Object[] tableLines = br.lines().toArray();

                    for (int i = 0; i < tableLines.length; i++) {
                        String line = tableLines[i].toString().trim();
                        String[] dataRow = line.split("/");
                        model.addRow(dataRow);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnCumparare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabelPlante.getSelectionModel().isSelectionEmpty() != true) {
                    int temp1 = Integer.valueOf(spinnerCantitate.getValue().toString());
                    int temp2 = Integer.valueOf(tabelPlante.getModel().getValueAt(tabelPlante.getSelectedRow(), 1).toString());
                    if (temp1 <= temp2 && temp1 > 0){
                        int valoareNoua = temp2-temp1;
                        tabelPlante.setValueAt(String.valueOf(valoareNoua), tabelPlante.getSelectedRow(), 1);
                        ScriereFisier();
                        VanzarePlante();
                        System.out.println("Vanzare efectuata!");
                    }
                }
            }
        });
        btnStoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabelPlante.getSelectionModel().isSelectionEmpty() != true) {
                    int temp1 = Integer.valueOf(spinnerCantitate.getValue().toString());
                    int temp2 = Integer.valueOf(tabelPlante.getModel().getValueAt(tabelPlante.getSelectedRow(), 1).toString());
                    if (temp1 > 0){
                        int valoareNoua = temp2+temp1;
                        tabelPlante.setValueAt(String.valueOf(valoareNoua), tabelPlante.getSelectedRow(), 1);
                        ScriereFisier();
                        System.out.println("Cantitate adaugata!");
                    }
                }
            }
        });
        btnAddPlanta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tabelPlante.getModel();
                String tempDen = fieldAddDen.getText();
                int tempCant = Integer.valueOf(spinAddCant.getValue().toString());
                int tempPret = Integer.valueOf(spinAddPret.getValue().toString());
                if (tempDen == "") {
                    JOptionPane.showMessageDialog(null, "Denumirea este goala!");
                }
                if (tempCant < 0) {
                    JOptionPane.showMessageDialog(null, "Cantitatea este negativa!");
                }
                if (tempPret < 0) {
                    JOptionPane.showMessageDialog(null, "Pretul este negativ!");
                }
                if (tempDen != "" && tempCant >= 0 && tempPret >= 0) {
                    model.addRow(new Object[]{tempDen, tempCant, tempPret});
                    ScriereFisier();
                    System.out.println("Planta adaugata!");
                }
            }
        });
        btnEliminare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tabelPlante.getModel();
                String tempDen = fieldAddDen.getText();
                model.removeRow(tabelPlante.getSelectedRow());
                ScriereFisier();
                System.out.println("Planta eliminata!");
            }
        });
    }

    private void ScriereFisier() {
        try {
            String filePath = "src\\proiect\\PlanteVandute.txt";
            File file = new File(filePath);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Denumire,Cantitate,Pret\n");
            for (int i = 0; i < tabelPlante.getRowCount(); i++) {
                for (int j = 0; j < tabelPlante.getColumnCount(); j++) {
                    bw.write(tabelPlante.getModel().getValueAt(i, j) + "");
                    if (j < tabelPlante.getColumnCount() - 1){
                        bw.write("/");
                    }
                }

                bw.write("\n");
            }
            bw.close();
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void VanzarePlante() {
        try {
            String filePath = "src\\proiect\\PlanteVandute.txt";
            int temp1 = Integer.valueOf(spinnerCantitate.getValue().toString());
            int temp2 = Integer.valueOf(tabelPlante.getModel().getValueAt(tabelPlante.getSelectedRow(), 2).toString());
            File file = new File(filePath);
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            bw.write(timeStamp + " - Vanzare: " +tabelPlante.getModel().getValueAt(tabelPlante.getSelectedRow(), 0).toString() +
                    " - Cantitate vanduta: "
                    + temp1 + " - Profit: " +
                    temp1 * temp2 + "\n");

            bw.close();
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        JFrame f1 = new JFrame("Florărie");
        Plafar p1 = new Plafar();
        File listaPlante = new File("src\\proiect\\Plante.txt");

        f1.setContentPane(p1.panouPrincipal);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.pack();
        p1.panouTab.setEnabledAt(1, false);
        p1.btnActualizare.doClick();
        f1.setSize(800, 600);
        f1.setLocationRelativeTo(null);
        f1.setVisible(true);

    }
}

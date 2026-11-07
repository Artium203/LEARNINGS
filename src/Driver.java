import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class Driver {

    private final String[] genderComboTexts = { "Male", "Female" };

    private static JFrame testFrame;
    private JPanel mainPanel;

    private JTextField nameTF;
    private JTextField fnameTF;
    private JTextField ageTF;

    private JComboBox genderChoices = null;

    private JSlider heightSlider;
    private JTextField weightTF;

    public Driver() {
        mainPanel = new JPanel();
        mainPanel.add(initPresonalDataPanel());
        mainPanel.add(initGenderPanel());
        mainPanel.add(initHeightSlider());
        mainPanel.add(initWeightPanel());
    }

    private JPanel initWeightPanel() {
        // TODO Auto-generated method stub
        JPanel panel = new JPanel();
        weightTF = new JTextField();
        weightTF.setPreferredSize(new Dimension(100, 20));
        panel.add(weightTF);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Weight"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return panel;
    }

    private JPanel initHeightSlider() {
        // TODO Auto-generated method stub
        JPanel panel = new JPanel();
        JLabel weight = new JLabel("160");
        heightSlider = new JSlider(JSlider.VERTICAL, 140, 190, 160);

        heightSlider.setMinorTickSpacing(1);
        heightSlider.setMajorTickSpacing(5);
        heightSlider.setPaintTicks(true);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(140, new JLabel("140"));
        labels.put(150, new JLabel("150"));
        labels.put(160, new JLabel("160"));
        labels.put(170, new JLabel("170"));
        labels.put(180, new JLabel("180"));
        labels.put(190, new JLabel("190"));

        heightSlider.setLabelTable(labels);
        heightSlider.setPaintLabels(true);

        panel.add(weight);
        panel.add(heightSlider);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Height"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        heightSlider.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                weight.setText("" + heightSlider.getValue());
            }
        });
        return panel;
    }

    private JPanel initGenderPanel() {
        // TODO Auto-generated method stub
        JPanel panel = new JPanel();
        genderChoices = new JComboBox(genderComboTexts);
        panel.add(genderChoices);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Gender"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return panel;
    }

    private JPanel initPresonalDataPanel() {
        // TODO Auto-generated method stub
        // init
        JPanel panel = new JPanel();
        JLabel name = new JLabel("Name");
        JLabel fname = new JLabel("Famely name");
        JLabel age = new JLabel("Age");

        nameTF = new JTextField();
        fnameTF = new JTextField();
        ageTF = new JTextField();
        JButton print = new JButton("Print");
        // order
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.add(name);
        panel.add(nameTF);
        panel.add(fname);
        panel.add(fnameTF);
        panel.add(age);
        panel.add(ageTF);
        panel.add(print);
        panel.add(new JLabel());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Personal data"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        print.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Name:" + nameTF.getText());
                System.out.println("Famely name:" + fnameTF.getText());
                System.out.println("Age:" + ageTF.getText());
                System.out.println("Height:" + heightSlider.getValue());
                System.out.println("Weight:" + weightTF.getText());
            }
        });
        return panel;

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Driver driver = new Driver();
        testFrame = new JFrame("Test");
        // TODO
        testFrame.setContentPane(driver.mainPanel);

        // Exit when the window is closed.
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Show the converter.
        testFrame.pack();
        testFrame.setVisible(true);
    }

}
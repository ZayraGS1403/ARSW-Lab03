package edu.eci.arsw.highlandersim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

public class ControlFrame extends JFrame {

    private static final int DEFAULT_IMMORTAL_HEALTH = 100;
    private static final int DEFAULT_DAMAGE_VALUE = 10;

    private JPanel contentPane;

    private List<Immortal> immortals;

    private JTextArea output;
    private JLabel statisticsLabel;
    private JScrollPane scrollPane;
    private JTextField numOfImmortals;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ControlFrame frame = new ControlFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ControlFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 647, 248);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JToolBar toolBar = new JToolBar();
        contentPane.add(toolBar, BorderLayout.NORTH);

        final JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startExecution();
                btnStart.setEnabled(false);

            }
        });
        toolBar.add(btnStart);

        JButton btnPauseAndCheck = new JButton("Pause and check");
        btnPauseAndCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pauseAndCheck();
            }
        });
        toolBar.add(btnPauseAndCheck);

        JButton btnResume = new JButton("Resume");


        btnResume.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                resumenExecution();
            }
        });

        toolBar.add(btnResume);

        JLabel lblNumOfImmortals = new JLabel("num. of immortals:");
        toolBar.add(lblNumOfImmortals);

        numOfImmortals = new JTextField();
        numOfImmortals.setText("3");
        toolBar.add(numOfImmortals);
        numOfImmortals.setColumns(10);

        JButton btnStop = new JButton("STOP");
        btnStop.setForeground(Color.RED);

        toolBar.add(btnStop);

        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e){
                output.setText("");
                stopExecution();
                btnStart.setEnabled(true);
            }
        });

        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        output = new JTextArea();
        output.setEditable(false);
        scrollPane.setViewportView(output);


        statisticsLabel = new JLabel("Immortals total health:");
        contentPane.add(statisticsLabel, BorderLayout.SOUTH);

    }

    private void startExecution(){
        immortals = setupInmortals();
        if (immortals != null) {
            synchronized (immortals) {
                for (Immortal im : immortals) {
                    im.start();
                }
            }
        }
    }

    private void resumenExecution(){
        synchronized (immortals) {
            for (Immortal i : immortals) {
                i.resumeInmortal();
            }

        }
    }

    private void pauseAndCheck(){
        synchronized (immortals) {
            for(Immortal i: immortals){
                i.pause();
            }
            try {
                Thread.sleep(immortals.size() / 10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            int sum = 0;
            for (Immortal im : immortals) {
                sum += im.getHealth();
            }
            statisticsLabel.setText("<html>"+immortals.toString()+"<br>Health sum:"+ sum);
        }
    }

    private void stopExecution(){
        pauseAndCheck();
        immortals.clear();
    }

    public List<Immortal> setupInmortals() {

        ImmortalUpdateReportCallback ucb=new TextAreaUpdateReportCallback(output,scrollPane);

        try {
            int ni = Integer.parseInt(numOfImmortals.getText());
            List<Immortal> il = Collections.synchronizedList(new ArrayList<Immortal>());
            for (int i = 0; i < ni; i++) {
                Immortal i1 = new Immortal("im" + i, il, DEFAULT_IMMORTAL_HEALTH, DEFAULT_DAMAGE_VALUE,ucb);
                il.add(i1);
            }
            return il;
        } catch (NumberFormatException e) {
            JOptionPane.showConfirmDialog(null, "Número inválido.");
            return null;
        }

    }

}

class TextAreaUpdateReportCallback implements ImmortalUpdateReportCallback{

    JTextArea ta;
    JScrollPane jsp;

    public TextAreaUpdateReportCallback(JTextArea ta,JScrollPane jsp) {
        this.ta = ta;
        this.jsp=jsp;
    }

    @Override
    public void processReport(String report) {
        ta.append(report);

        //move scrollbar to the bottom
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   JScrollBar bar = jsp.getVerticalScrollBar();
                   bar.setValue(bar.getMaximum());
               }
           }
        );

    }

}
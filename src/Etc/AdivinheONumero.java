//AdivinheONumero 
package Etc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class AdivinheONumero extends JFrame {
    private int numeroSecreto;
    private JTextField chuteTextField;
    private JLabel mensagemLabel;
    private JButton novoJogoButton;

    public AdivinheONumero() {
        super("Adivinhe o Número");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel instrucaoLabel = new JLabel("Eu tenho um número entre 1 e 100, você pode adivinhá-lo? Entre com seu chute:");
        panel.add(instrucaoLabel);

        chuteTextField = new JTextField();
        panel.add(chuteTextField);

        JButton chuteButton = new JButton("Chutar");
        chuteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fazerChute();
            }
        });
        panel.add(chuteButton);

        mensagemLabel = new JLabel("");
        panel.add(mensagemLabel);

        novoJogoButton = new JButton("Novo Jogo");
        novoJogoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarNovoJogo();
            }
        });
        panel.add(novoJogoButton);
        novoJogoButton.setEnabled(false);

        add(panel);

        iniciarNovoJogo();
    }

    private void iniciarNovoJogo() {
        Random random = new Random();
        numeroSecreto = random.nextInt(100) + 1; 
        chuteTextField.setText("");
        mensagemLabel.setText("");
        chuteTextField.setEditable(true);
        chuteTextField.requestFocus();
        novoJogoButton.setEnabled(false);
    }

    private void fazerChute() {
        try {
            int chute = Integer.parseInt(chuteTextField.getText());
            if (chute < 1 || chute > 100) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um número entre 1 e 100.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (chute == numeroSecreto) {
                mensagemLabel.setText("Correto!");
                chuteTextField.setEditable(false);
                novoJogoButton.setEnabled(true);
            } else if (chute < numeroSecreto) {
                mensagemLabel.setText("Tente um número maior.");
            } else {
                mensagemLabel.setText("Tente um número menor.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdivinheONumero().setVisible(true);
            }
        });
    }
}

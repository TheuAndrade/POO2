// Calculadora de Investimento
package Etc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculadoraDeInvestimento extends JFrame {
    private JTextField jurosTextField;
    private JTextField anosTextField;
    private JTextField depositoTextField;
    private JLabel totalPoupadoLabel;

    public CalculadoraDeInvestimento() {
        super("Calculadora de Investimento");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel jurosLabel = new JLabel("Juros ao mês (%)");
        jurosLabel.setBounds(10, 10, 150, 20);
        panel.add(jurosLabel);

        jurosTextField = new JTextField();
        jurosTextField.setBounds(170, 10, 150, 20);
        panel.add(jurosTextField);

        JLabel anosLabel = new JLabel("Número de anos");
        anosLabel.setBounds(10, 40, 150, 20);
        panel.add(anosLabel);

        anosTextField = new JTextField();
        anosTextField.setBounds(170, 40, 150, 20);
        panel.add(anosTextField);

        JLabel depositoLabel = new JLabel("Depósito mensal");
        depositoLabel.setBounds(10, 70, 150, 20);
        panel.add(depositoLabel);

        depositoTextField = new JTextField();
        depositoTextField.setBounds(170, 70, 150, 20);
        panel.add(depositoTextField);

        JButton calcularButton = new JButton("Calcular");
        calcularButton.setBounds(150, 100, 100, 30);
        calcularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularTotalPoupado();
            }
        });
        panel.add(calcularButton);

        totalPoupadoLabel = new JLabel("Total poupado R$");
        totalPoupadoLabel.setBounds(10, 140, 200, 20);
        panel.add(totalPoupadoLabel);

        add(panel);
    }

    private void calcularTotalPoupado() {
        double juros = Double.parseDouble(jurosTextField.getText()) / 100;
        int anos = Integer.parseInt(anosTextField.getText());
        double depositoMensal = Double.parseDouble(depositoTextField.getText());
        double totalPoupado = 0;

        for (int i = 0; i < anos * 12; i++) {
            totalPoupado *= (1 + juros); // Aplicar juros no saldo anterior
            totalPoupado += depositoMensal; // Adicionar o depósito mensal no início do mês
        }

        totalPoupadoLabel.setText("Total poupado R$ " + String.format("%.2f", totalPoupado));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalculadoraDeInvestimento().setVisible(true);
            }
        });
    }
}

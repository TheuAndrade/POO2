//ConversorDeMoeda
package Etc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ConversorMoeda extends JFrame {
    private JTextField valorReaisTextField;
    private JComboBox<String> moedaComboBox;
    private JLabel resultadoLabel;
    private HashMap<String, Double> taxaDeCambio;

    public ConversorMoeda() {
        super("Conversor de Moeda");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel valorReaisLabel = new JLabel("Valor em Reais:");
        panel.add(valorReaisLabel);

        valorReaisTextField = new JTextField();
        panel.add(valorReaisTextField);

        JLabel moedaLabel = new JLabel("Moeda:");
        panel.add(moedaLabel);

        moedaComboBox = new JComboBox<>(new String[]{"Dólar", "Euro", "Libra"});
        panel.add(moedaComboBox);

        JButton converterButton = new JButton("Converter");
        converterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                converterMoeda();
            }
        });
        panel.add(converterButton);

        resultadoLabel = new JLabel("");
        panel.add(resultadoLabel);

        taxaDeCambio = new HashMap<>();
        taxaDeCambio.put("Dólar", 0.18); // 1 Real = 0.18 Dólares
        taxaDeCambio.put("Euro", 0.15);  // 1 Real = 0.15 Euros
        taxaDeCambio.put("Libra", 0.13); // 1 Real = 0.13 Libras

        add(panel);
    }

    private void converterMoeda() {
        try {
            double valorReais = Double.parseDouble(valorReaisTextField.getText());
            String moedaSelecionada = (String) moedaComboBox.getSelectedItem();
            double taxa = taxaDeCambio.get(moedaSelecionada);
            double valorConvertido = valorReais * taxa;
            resultadoLabel.setText(String.format("%.2f Reais equivalem a %.2f %s", valorReais, valorConvertido, moedaSelecionada));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor válido em Reais.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConversorMoeda().setVisible(true);
            }
        });
    }
}

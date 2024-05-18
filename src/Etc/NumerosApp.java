///NumerosApp
package Etc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NumerosApp extends JFrame {
    private JTextField numerosTextField;
    private JLabel menorLabel;
    private JLabel maiorLabel;
    private JLabel mediaLabel;
    private ArrayList<Double> numeros;

    public NumerosApp() {
        super("Manipulação de Números");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel numerosLabel = new JLabel("Números (separados por ,):");
        panel.add(numerosLabel);

        numerosTextField = new JTextField();
        panel.add(numerosTextField);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarNumeros();
            }
        });
        panel.add(okButton);

        JLabel menorTituloLabel = new JLabel("Menor:");
        panel.add(menorTituloLabel);

        menorLabel = new JLabel();
        panel.add(menorLabel);

        JLabel maiorTituloLabel = new JLabel("Maior:");
        panel.add(maiorTituloLabel);

        maiorLabel = new JLabel();
        panel.add(maiorLabel);

        JLabel mediaTituloLabel = new JLabel("Média:");
        panel.add(mediaTituloLabel);

        mediaLabel = new JLabel();
        panel.add(mediaLabel);

        JButton exibirButton = new JButton("Exibir");
        exibirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirResultados();
            }
        });
        panel.add(exibirButton);

        add(panel);

        numeros = new ArrayList<>();
    }

    private void cadastrarNumeros() {
        String numerosStr = numerosTextField.getText();
        String[] numerosArray = numerosStr.split(",");
        for (String numStr : numerosArray) {
            try {
                double num = Double.parseDouble(numStr);
                numeros.add(num);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, insira números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Números cadastrados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exibirResultados() {
        if (numeros.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum número cadastrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double menor = numeros.get(0);
        double maior = numeros.get(0);
        double soma = 0;

        for (double num : numeros) {
            if (num < menor) menor = num;
            if (num > maior) maior = num;
            soma += num;
        }

        double media = soma / numeros.size();

        menorLabel.setText(Double.toString(menor));
        maiorLabel.setText(Double.toString(maior));
        mediaLabel.setText(Double.toString(media));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NumerosApp().setVisible(true);
            }
        });
    }
}

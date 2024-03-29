import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdicionarItemFrame extends JFrame implements ActionListener {
    JTextField txtDescricao, txtPreco;
    JButton btnAdicionar;
    Connection conexao;

    public AdicionarItemFrame(Connection conexao) {
        super("Adicionar Item");
        this.conexao = conexao;

        // Configurações do JFrame
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null);

        // Layout para organizar os componentes
        setLayout(new GridLayout(3, 2));

        // Inicializa os campos de texto
        txtDescricao = new JTextField();
        txtPreco = new JTextField();

        // Inicializa o botão
        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(this);

        // Adiciona os componentes ao JFrame
        add(new JLabel("Descrição:"));
        add(txtDescricao);
        add(new JLabel("Preço:"));
        add(txtPreco);
        add(btnAdicionar);

        // Exibe o JFrame
        setVisible(true);
    }

    // Implementação do método actionPerformed para tratar eventos de botão
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdicionar) {
            String descricao = txtDescricao.getText().trim();
            String precoStr = txtPreco.getText().trim();

            // Verifica se os campos estão vazios
            if (descricao.isEmpty() || precoStr.isEmpty()) {
                System.out.println("Os campos não podem estar vazios.");
            } else {
                try {
                    // Converte o preço para double
                    double preco = Double.parseDouble(precoStr);

                    // Insere o novo item no banco de dados
                    inserirItem(descricao, preco);

                    // Fecha a janela após adicionar o item
                    dispose();
                } catch (NumberFormatException ex) {
                    System.out.println("Preço inválido.");
                }
            }
        }
    }

    // Método para inserir um novo item no banco de dados
    private void inserirItem(String descricao, double preco) {
        try {
            // Cria uma declaração SQL parametrizada para inserir o item
            String sql = "INSERT INTO produtos (descricao, preco, ativo) VALUES (?, ?, true)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, descricao);
            stmt.setDouble(2, preco);

            // Executa a declaração
            stmt.executeUpdate();

            // Fecha o PreparedStatement
            stmt.close();

            System.out.println("Item adicionado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditarItemFrame extends JFrame implements ActionListener {
    JTextField txtDescricao, txtPreco;
    JButton btnAtualizar;
    int id;
    Connection conexao;

    public EditarItemFrame(Connection conexao, int id) {
        super("Editar Detalhes do Item");
        this.conexao = conexao;
        this.id = id;

        // Configurações do JFrame
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout para organizar os componentes
        setLayout(new GridLayout(3, 2));

        // Inicializa os componentes
        JLabel lblDescricao = new JLabel("Descrição:");
        txtDescricao = new JTextField(10);
        JLabel lblPreco = new JLabel("Preço:");
        txtPreco = new JTextField(10);
        btnAtualizar = new JButton("Atualizar");

        // Adiciona ActionListener ao botão
        btnAtualizar.addActionListener(this);

        // Adiciona os componentes ao JFrame
        add(lblDescricao);
        add(txtDescricao);
        add(lblPreco);
        add(txtPreco);
        add(btnAtualizar);

        // Exibe o JFrame
        setVisible(true);
    }

    // Implementação do método actionPerformed para tratar eventos de botão
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAtualizar) {
            String descricao = txtDescricao.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            if (!descricao.isEmpty() && !txtPreco.getText().isEmpty()) {
                try {
                    // Query SQL para atualizar os valores
                    String sql = "UPDATE produtos SET descricao = ?, preco = ? WHERE id = ?";
                    PreparedStatement stmt = conexao.prepareStatement(sql);
                    stmt.setString(1, descricao);
                    stmt.setDouble(2, preco);
                    stmt.setInt(3, id);
                    // Executa a atualização
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "Valores atualizados com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Falha ao atualizar valores.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            }
        }
    }
}

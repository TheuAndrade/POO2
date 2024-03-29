import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoverItem extends JFrame {
    private JTextField txtId;

    public RemoverItem(Connection conexao) {
        setTitle("Remover Item");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField();
        JButton btnRemover = new JButton("Remover");

        btnRemover.addActionListener(e -> removerItem());

        panel.add(lblId);
        panel.add(txtId);
        panel.add(new JLabel()); // Espaço em branco para alinhar o botão
        panel.add(btnRemover);

        add(panel);
        setVisible(true);
    }

    private void removerItem() {
        String idText = txtId.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "É preciso informar um ID.");
            return;
        }

        int id = Integer.parseInt(idText);
        try (Connection conexao = DataBaseConnection.conectar();
             PreparedStatement statement = conexao.prepareStatement("DELETE FROM produtos WHERE Id = ?")) {

            statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Item removido com sucesso!");
                dispose(); // Fecha o JFrame após a remoção bem-sucedida
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum item encontrado com o ID especificado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover o item: " + ex.getMessage());
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditarItemFrame extends JFrame {
    private JTextField txtDescricao;
    private JTextField txtValor;
    private Object[] itemSelecionado;

    public EditarItemFrame(Object[] itemSelecionado) {
        this.itemSelecionado = itemSelecionado;

        setTitle("Editar Item");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel lblDescricao = new JLabel("Descrição:");
        JLabel lblValor = new JLabel("Valor:");

        txtDescricao = new JTextField(itemSelecionado[1].toString());
        txtValor = new JTextField(itemSelecionado[2].toString());

        panel.add(lblDescricao);
        panel.add(txtDescricao);
        panel.add(lblValor);
        panel.add(txtValor);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> {
            atualizarItem();
        });
        panel.add(btnAtualizar);

        add(panel);
        setVisible(true);
    }

    private void atualizarItem() {
        String novaDescricao = txtDescricao.getText();
        String novoValor = txtValor.getText();
        int id = (int) itemSelecionado[0];

        Connection conexao = null;
        PreparedStatement statement = null;
        try {
            conexao = DataBaseConnection.conectar();
            String query = "UPDATE Produtos SET Descricao = ?, Preco = ? WHERE Id = ?";
            statement = conexao.prepareStatement(query);
            statement.setString(1, novaDescricao);
            statement.setString(2, novoValor);
            statement.setInt(3, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Valores atualizados com sucesso!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar os valores: " + ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (conexao != null) {
                try {
                    conexao.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

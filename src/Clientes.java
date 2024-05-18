import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;




//////////////LISTAR CLIENTES 

class ListagemClientes {
    private Connection conexao;

    public ListagemClientes(Connection conexao) {
        this.conexao = conexao;
    }

    public DefaultTableModel listarItens() throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conexao.createStatement();
            rs = stmt.executeQuery("SELECT * FROM clientes WHERE ativo = TRUE");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            //informações das colunas
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            //informações das linhas
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                int index = 0;
                for (int i = 1; i <= columnCount; i++) {
                    row[index++] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return model;
    }
}

class AdicionarClienteFrame extends JFrame {
    private Connection conexao;
    private JFrame frame;
    private JTextField txtNome;
    private JButton btnAdicionar;

    public AdicionarClienteFrame(Connection conexao) {
        this.conexao = conexao;

        frame = new JFrame("Adicionar Cliente");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2));

        JLabel lblNome = new JLabel("Nome do Cliente:");
        txtNome = new JTextField();
        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> adicionarCliente());

        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(new JLabel()); // Espaço em branco
        panel.add(btnAdicionar);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void adicionarCliente() {
        String nome = txtNome.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor, insira o nome do cliente.");
            return;
        }

        try {
            String sql = "INSERT INTO clientes (nome) VALUES (?)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(frame, "Cliente adicionado com sucesso.");
            frame.dispose(); // Fecha o frame
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Erro ao adicionar o cliente: " + ex.getMessage());
        }
    }
}

class RemoverCliente extends JFrame {
    private JTextField txtId;

    public RemoverCliente(Connection conexao) {
        setTitle("Remover Item");
        setSize(300, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField();
        JButton btnRemover = new JButton("Remover");

        btnRemover.addActionListener(e -> {
            removerItem();
        });

        panel.add(lblId);
        panel.add(txtId);
        panel.add(new JLabel());
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
             PreparedStatement statement = conexao.prepareStatement("UPDATE clientes SET Ativo = FALSE WHERE Id = ?")) {

            statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(this, "Item removido com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum item encontrado com o ID especificado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover o item: " + ex.getMessage());
        }
    }
}

class EditarClienteFrame extends JFrame {
    private JTextField txtNome;
    private Object[] clienteSelecionado;

    public EditarClienteFrame(Object[] clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;

        setTitle("Editar Cliente");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel lblNome = new JLabel("Nome:");

        txtNome = new JTextField(clienteSelecionado.length > 1 ? clienteSelecionado[1].toString() : "");

        panel.add(lblNome);
        panel.add(txtNome);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> {
            atualizarCliente();
        });
        panel.add(btnAtualizar);

        add(panel);
        setVisible(true);
    }

    private void atualizarCliente() {
        String novoNome = txtNome.getText();

        int id = (int) clienteSelecionado[0];

        Connection conexao = null;
        PreparedStatement statement = null;
        try {
            conexao = DataBaseConnection.conectar();
            String query = "UPDATE Clientes SET Nome = ? WHERE Id = ?";
            statement = conexao.prepareStatement(query);
            statement.setString(1, novoNome);
            statement.setInt(2, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Nome do cliente atualizado com sucesso!");
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar o nome do cliente: " + ex.getMessage());
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


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


//////////////ADICIONAR ITEM 

class AdicionarItemFrame extends JFrame {
    private JTextField txtDescricao, txtPreco;
    private JButton btnAdicionar;
    private Connection conexao;
    private JFrame frame; // Declare frame as a class-level field

    public AdicionarItemFrame(Connection conexao) {
        this.conexao = conexao;

        frame = new JFrame("Adicionar Item"); // Initialize frame
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel lblDescricao = new JLabel("Descrição:");
        JLabel lblPreco = new JLabel("Preço:");

        txtDescricao = new JTextField();
        txtPreco = new JTextField();

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> adicionarItem());

        panel.add(lblDescricao);
        panel.add(txtDescricao);
        panel.add(lblPreco);
        panel.add(txtPreco);
        panel.add(btnAdicionar);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void adicionarItem() {
        String descricao = txtDescricao.getText().trim();
        String precoStr = txtPreco.getText().trim();

        if (descricao.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos não podem estar vazios.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr);
            inserirItem(descricao, preco);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Preço inválido.");
        }
    }
    

    private void inserirItem(String descricao, double preco) {
        try {
            String sql = "INSERT INTO produtos (descricao, preco, ativo) VALUES (?, ?, true)";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, descricao);
            stmt.setDouble(2, preco);
            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Item adicionado com sucesso.");
            frame.dispose(); // Close the frame
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


//////////////REMOVER ITEM 

class RemoverItem extends JFrame {
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
             PreparedStatement statement = conexao.prepareStatement("UPDATE produtos SET Ativo = FALSE WHERE Id = ?")) {

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



//////////////EDITAR ITEM 

class EditarItemFrame extends JFrame {
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JTextField txtAtivo;
    private Object[] itemSelecionado;

    public EditarItemFrame(Object[] itemSelecionado) {
        this.itemSelecionado = itemSelecionado;

        setTitle("Editar Item");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        JLabel lblDescricao = new JLabel("Descrição:");
        JLabel lblValor = new JLabel("Valor:");
        JLabel lblAtivo = new JLabel("Ativo:");

        txtDescricao = new JTextField(itemSelecionado.length > 1 ? itemSelecionado[1].toString() : "");
        txtValor = new JTextField(itemSelecionado.length > 2 ? itemSelecionado[2].toString() : "");
        txtAtivo = new JTextField(itemSelecionado.length > 3 ? itemSelecionado[3].toString() : "");

        panel.add(lblDescricao);
        panel.add(txtDescricao);
        panel.add(lblValor);
        panel.add(txtValor);
        panel.add(lblAtivo);
        panel.add(txtAtivo);

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
        String novoAtivoStr = txtAtivo.getText();

        int id = (int) itemSelecionado[0];

        Connection conexao = null;
        PreparedStatement statement = null;
        try {
            conexao = DataBaseConnection.conectar();
            String query = "UPDATE Produtos SET Descricao = ?, Preco = ?, Ativo = ? WHERE Id = ?";
            statement = conexao.prepareStatement(query);
            statement.setString(1, novaDescricao);
            statement.setString(2, novoValor);
            
            // Convertendo para booleano se for "true" ou "false"
            boolean novoAtivo = Boolean.parseBoolean(novoAtivoStr);
            statement.setBoolean(3, novoAtivo);
            
            statement.setInt(4, id);
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



//////////////LISTAR ITENS 

class ListagemDoBanco {
    private Connection conexao;

    public ListagemDoBanco(Connection conexao) {
        this.conexao = conexao;
    }

    public DefaultTableModel listarItens() throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conexao.createStatement();
            rs = stmt.executeQuery("SELECT * FROM produtos WHERE ativo = 1");
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




//////////////FILTRAR ITEM

class FiltrarItemFrame extends JFrame implements ActionListener {
    JTextField txtId, txtDescricao;
    JButton btnFiltrar;
    Connection conexao;
    JTable table;

    public FiltrarItemFrame(Connection conexao, JTable table) {
        super("Filtrar Item");
        this.conexao = conexao;
        this.table = table;

        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2));

        txtId = new JTextField();
        txtDescricao = new JTextField();

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(this);

        add(new JLabel("ID:"));
        add(txtId);
        add(new JLabel("Descrição:"));
        add(txtDescricao);
        add(new JLabel()); // Espaço em branco para alinhar o botão
        add(btnFiltrar);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFiltrar) {
            try {
                String idStr = txtId.getText().trim();
                String descricao = txtDescricao.getText().trim();
                int id = idStr.isEmpty() ? 0 : Integer.parseInt(idStr);

                DefaultTableModel model = filtrarItens(id, descricao);
                table.setModel(model);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        }
    }

    private DefaultTableModel filtrarItens(int id, String descricao) {
        DefaultTableModel model = new DefaultTableModel();
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        try {
            // Verifica se o ID ou a descrição estão vazios
            if (descricao.isEmpty() && id == 0) {
                JOptionPane.showMessageDialog(null, "ID ou Descrição do produto precisam ser informados");
                return model; // Retorna o modelo vazio, pois não há critérios para filtragem
            }
            
            // Prepara a consulta SQL com os critérios de filtragem
            String query = "SELECT * FROM produtos WHERE (Id = ? OR ? = 0) AND (Descricao LIKE CONCAT('%', ?, '%') OR ? = ''); ";
            statement = conexao.prepareStatement(query);
            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setString(3, descricao);
            statement.setString(4, descricao);
            
            // Executa a consulta
            rs = statement.executeQuery();
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "Nenhum produto encontrado.");
                return model; // Retorna o modelo vazio
            }
         // Adiciona as colunas ao modelo
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Adiciona as linhas ao modelo
            while (rs.next()) {
                Object[] row = new Object[metaData.getColumnCount()];
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Fecha os recursos
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return model;
    }
}

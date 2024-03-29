import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;


public class Produto extends JFrame implements ActionListener, WindowListener {
    JButton btnListar, btnAdicionar, btnAtualizar, btnRemover, btnFiltrar;
    JTable table;
    Connection conexao;
    ListagemDoBanco listagemDoBanco;

    public Produto(Connection conexao) {
        super("Exemplo de JFrame com Botões e JTable");
        this.conexao = conexao;
        this.listagemDoBanco = new ListagemDoBanco(conexao);

        // Configurações do JFrame
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout para organizar os componentes
        setLayout(new BorderLayout());

        // Panel para os botões
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        // Inicializa os botões
        btnListar = new JButton("Listar");
        btnAdicionar = new JButton("Adicionar");
        btnAtualizar = new JButton("Atualizar");
        btnRemover = new JButton("Remover");
        btnFiltrar = new JButton("Filtrar");
        // Adiciona ActionListener aos botões
        btnListar.addActionListener(this);
        btnAdicionar.addActionListener(this);
        btnAtualizar.addActionListener(this);
        btnRemover.addActionListener(this);
        btnFiltrar.addActionListener(this);
        // Adiciona os botões ao painel
        buttonPanel.add(btnListar);
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnFiltrar);
        // Adiciona o painel de botões ao JFrame
        add(buttonPanel, BorderLayout.NORTH);

        // Inicializa a JTable
        table = new JTable();
        // Adiciona a JTable a um JScrollPane para possibilitar a rolagem
        JScrollPane scrollPane = new JScrollPane(table);
        // Adiciona o JScrollPane ao JFrame
        add(scrollPane, BorderLayout.CENTER);

        // Adiciona o WindowListener ao JFrame
        addWindowListener(this);

        // Exibe o JFrame
        setVisible(true);
    }

    // Implementação do método actionPerformed para tratar eventos de botão
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnListar) {
            try {
                DefaultTableModel model = listagemDoBanco.listarItens();
                table.setModel(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == btnAdicionar) {
            new AdicionarItemFrame(conexao);
        } else if (e.getSource() == btnAtualizar) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Object[] itemSelecionado = new Object[3];
                for (int i = 0; i < 3; i++) {
                    itemSelecionado[i] = table.getValueAt(selectedRow, i);
                }
                new EditarItemFrame(itemSelecionado);
            }
        } else if (e.getSource() == btnRemover) {
            new RemoverItem(conexao);
        } else if (e.getSource() == btnFiltrar) {
            JOptionPane.showMessageDialog(this, "Ação do botão Filtrar");
        }
    }

    // Implementação do método windowClosing para fechar a conexão quando o JFrame for fechado
    public void windowClosing(WindowEvent e) {
        DataBaseConnection.fecharConexao(conexao);
    }

    // Implementações dos outros métodos de WindowListener (não são usados neste caso, mas precisam ser implementados)
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}

class AdicionarItemFrame extends JFrame implements ActionListener {
    JTextField txtDescricao, txtPreco;
    JButton btnAdicionar;
    Connection conexao;

    public AdicionarItemFrame(Connection conexao) {
        super("Adicionar Item");
        this.conexao = conexao;

        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2));

        txtDescricao = new JTextField();
        txtPreco = new JTextField();

        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(this);

        add(new JLabel("Descrição:"));
        add(txtDescricao);
        add(new JLabel("Preço:"));
        add(txtPreco);
        add(btnAdicionar);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdicionar) {
            String descricao = txtDescricao.getText().trim();
            String precoStr = txtPreco.getText().trim();
            if (descricao.isEmpty() || precoStr.isEmpty()) {
                System.out.println("Os campos não podem estar vazios.");
            } else {
                try {
                    double preco = Double.parseDouble(precoStr);
                    inserirItem(descricao, preco);
                    dispose();
                } catch (NumberFormatException ex) {
                    System.out.println("Preço inválido.");
                }
            }
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
            System.out.println("Item adicionado com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




class EditarItemFrame extends JFrame {
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
             PreparedStatement statement = conexao.prepareStatement("DELETE FROM produtos WHERE Id = ?")) {

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
            rs = stmt.executeQuery("SELECT * FROM produtos");

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                model.addColumn(rs.getMetaData().getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row[i - 1] = rs.getObject(i);
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
            String query = "SELECT * FROM produtos WHERE (Id = ? OR ? = 0) AND (Descricao = ? OR ? = '')";
            statement = conexao.prepareStatement(query);
            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.setString(3, descricao);
            statement.setString(4, descricao);
            
            // Executa a consulta
            rs = statement.executeQuery();
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "Nenhum produto encontrado.");
                return model; // Retorna o modelo vazio, pois nenhum produto foi encontrado
            }
            // Adiciona as colunas ao modelo
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                model.addColumn(rs.getMetaData().getColumnName(i));
            }

            // Adiciona as linhas ao modelo
            while (rs.next()) {
                Object[] row = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
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

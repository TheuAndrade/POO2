import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class VendasFrame extends JFrame implements ActionListener {
    private Connection conexao;
    private JComboBox<String> comboClientes;
    private JComboBox<String> comboProdutos;
    private JTextField txtQuantidade;
    private JTable tablePedidos;
    private DefaultTableModel tableModel;

    public VendasFrame(Connection conexao) {
        super("Registrar Venda");
        this.conexao = conexao;

        // Configurações do JFrame
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel para a seleção de cliente e produto
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblCliente = new JLabel("Cliente:");
        comboClientes = new JComboBox<>(getClientes());
        selectionPanel.add(lblCliente);
        selectionPanel.add(comboClientes);

        JLabel lblProduto = new JLabel("Produto:");
        comboProdutos = new JComboBox<>(getProdutos());
        selectionPanel.add(lblProduto);
        selectionPanel.add(comboProdutos);

        JLabel lblQuantidade = new JLabel("Quantidade:");
        txtQuantidade = new JTextField();
        selectionPanel.add(lblQuantidade);
        selectionPanel.add(txtQuantidade);

        add(selectionPanel, BorderLayout.NORTH);

        // Botão para adicionar venda
        JButton btnAdicionar = new JButton("Adicionar Venda");
        btnAdicionar.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdicionar);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(buttonPanel, BorderLayout.CENTER);

        // Tabela para listar pedidos da data corrente
        tableModel = new DefaultTableModel(new String[]{"Data", "Produto", "Cliente", "Preço", "Quantidade", "Total"}, 0);
        tablePedidos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePedidos);
        add(scrollPane, BorderLayout.SOUTH);

        // Carregar pedidos da data corrente
        listarPedidos();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Adicionar Venda")) {
            adicionarVenda();
        }
    }

    private String[] getClientes() {
        ArrayList<String> clientes = new ArrayList<>();
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nome FROM clientes")) {
            while (rs.next()) {
                clientes.add(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return clientes.toArray(new String[0]);
    }

    private String[] getProdutos() {
        ArrayList<String> produtos = new ArrayList<>();
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT descricao FROM produtos")) {
            while (rs.next()) {
                produtos.add(rs.getString("descricao"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return produtos.toArray(new String[0]);
    }

    private void adicionarVenda() {
        String cliente = (String) comboClientes.getSelectedItem();
        String produto = (String) comboProdutos.getSelectedItem();
        int quantidade = Integer.parseInt(txtQuantidade.getText());

        if (cliente == null || produto == null || quantidade <= 0) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos corretamente.");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            // Inserir pedido
            try (PreparedStatement stmt = conexao.prepareStatement("INSERT INTO pedidos (dtCadastro, clienteID, quantidade, produtoId) VALUES (?, ?, ?, ?)")) {
                stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setInt(2, getClienteId(cliente));
                stmt.setInt(3, quantidade);
                stmt.setInt(4, getProdutoId(produto));
                stmt.executeUpdate();
            }

            conexao.commit();
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso.");
            listarPedidos();
        } catch (SQLException ex) {
            try {
                conexao.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao registrar a venda: " + ex.getMessage());
        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private int getClienteId(String nomeCliente) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement("SELECT id FROM clientes WHERE nome = ?")) {
            stmt.setString(1, nomeCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Cliente não encontrado.");
                }
            }
        }
    }

    private int getProdutoId(String descricaoProduto) throws SQLException {
        try (PreparedStatement stmt = conexao.prepareStatement("SELECT id FROM produtos WHERE descricao = ?")) {
            stmt.setString(1, descricaoProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Produto não encontrado.");
                }
            }
        }
    }

    private void listarPedidos() {
    	
    	LocalDate hoje = LocalDate.now();
        tableModel.setRowCount(0);
        String query = "SELECT p.dtCadastro, pr.descricao AS produto, c.nome AS cliente, pr.preco AS preco, p.quantidade, (pr.preco * p.quantidade) AS total " +
                       "FROM pedidos p " +
                       "JOIN produtos pr ON p.produtoId = pr.id " +
                       "JOIN clientes c ON p.clienteID = c.id " +
                       "WHERE DATE(p.dtCadastro) = ? " +
                       "ORDER BY p.dtCadastro, pr.descricao, c.nome";

        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getTimestamp("dtCadastro"),
                            rs.getString("cliente"),
                            rs.getString("produto"),
                            rs.getDouble("preco"),
                            rs.getInt("quantidade"),
                            rs.getDouble("total")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

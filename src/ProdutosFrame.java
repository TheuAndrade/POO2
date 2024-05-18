import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;

public class ProdutosFrame extends JFrame implements ActionListener {
    private JTable table;
    private Connection conexao;
    private ListagemDoBanco listagemDoBanco;

    public ProdutosFrame(Connection conexao) {
        super("Produtos");
        this.conexao = conexao;
        this.listagemDoBanco = new ListagemDoBanco(conexao);

        // Configurações do JFrame
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fechar apenas esta janela ao invés de sair da aplicação
        setLocationRelativeTo(null);

        // Layout para organizar os componentes
        setLayout(new BorderLayout());

        // Iniciar o JTable
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Botão para listar produtos
        JButton btnListar = new JButton("Listar Produtos");
        btnListar.addActionListener(this);
        buttonPanel.add(btnListar);

        // Botão para adicionar produto
        JButton btnAdicionar = new JButton("Adicionar Produto");
        btnAdicionar.addActionListener(this);
        buttonPanel.add(btnAdicionar);

        // Botão para remover produto
        JButton btnRemover = new JButton("Remover Produto");
        btnRemover.addActionListener(this);
        buttonPanel.add(btnRemover);

        // Botão para editar produto
        JButton btnEditar = new JButton("Editar Produto");
        btnEditar.addActionListener(this);
        buttonPanel.add(btnEditar);
        
        // Botão para filtrar produtos
        JButton btnFiltrar = new JButton("Filtrar Produtos");
        btnFiltrar.addActionListener(this);
        buttonPanel.add(btnFiltrar);

        add(buttonPanel, BorderLayout.NORTH);

        // Exibir o JFrame
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Listar Produtos")) {
            listarProdutos();
        } else if (e.getActionCommand().equals("Adicionar Produto")) {
            adicionarProduto();
        } else if (e.getActionCommand().equals("Remover Produto")) {
            removerProduto();
        } else if (e.getActionCommand().equals("Editar Produto")) {
            editarProduto();
        } else if (e.getActionCommand().equals("Filtrar Produtos")) {
            filtrarProdutos();
        }
    }

    private void listarProdutos() {
        try {
            DefaultTableModel model = listagemDoBanco.listarItens();
            table.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void adicionarProduto() {
        // Implementar lógica para abrir uma nova janela de adicionar produto
        new AdicionarItemFrame(conexao);
    }

    private void removerProduto() {
        // Implementar lógica para remover produto
        new RemoverItem(conexao);
    }

    private void editarProduto() {
        // Implementar lógica para abrir uma nova janela de editar produto
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Object[] itemSelecionado = new Object[table.getColumnCount()];
            for (int i = 0; i < table.getColumnCount(); i++) {
                itemSelecionado[i] = table.getValueAt(selectedRow, i);
            }
            new EditarItemFrame(itemSelecionado);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
        }
    }
    
    private void filtrarProdutos() {
        // Abrir o diálogo de filtragem
        new FiltrarItemFrame(conexao, table);
    }
}

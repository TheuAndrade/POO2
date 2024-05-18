import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class VendarFrame extends JFrame {
    private JTextField txtId;
    private JComboBox<String> comboBoxClientes;
    private JComboBox<String> comboBoxProdutos;
    private JTextField txtQuantidade;
    private JButton btnAdicionar;
    private JTable table;

    public VendarFrame(List<String> clientes, List<String> produtos) {
        setTitle("Registrar Venda");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Painel para os campos de cima
        JPanel topPanel = new JPanel(new GridLayout(1, 4));
        JLabel lblId = new JLabel("ID:");
        txtId = new JTextField();
        comboBoxClientes = new JComboBox<>(clientes.toArray(new String[0]));
        topPanel.add(lblId);
        topPanel.add(txtId);
        topPanel.add(comboBoxClientes);

        // Painel para os campos de baixo
        JPanel bottomPanel = new JPanel(new GridLayout(1, 4));
        JLabel lblProduto = new JLabel("Produto:");
        comboBoxProdutos = new JComboBox<>(produtos.toArray(new String[0]));
        JLabel lblQuantidade = new JLabel("Quantidade:");
        txtQuantidade = new JTextField();
        btnAdicionar = new JButton("Adicionar");
        bottomPanel.add(lblProduto);
        bottomPanel.add(comboBoxProdutos);
        bottomPanel.add(lblQuantidade);
        bottomPanel.add(txtQuantidade);
        bottomPanel.add(btnAdicionar);

        // Configurar a tabela
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Adicionar os painéis ao painel principal
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    // Método para atualizar a lista de clientes
    public void atualizarClientes(List<String> novosClientes) {
        comboBoxClientes.setModel(new DefaultComboBoxModel<>(novosClientes.toArray(new String[0])));
    }

    // Método para atualizar a lista de produtos
    public void atualizarProdutos(List<String> novosProdutos) {
        comboBoxProdutos.setModel(new DefaultComboBoxModel<>(novosProdutos.toArray(new String[0])));
    }

    // Método para limpar os campos de entrada após a adição
    public void limparCampos() {
        txtId.setText("");
        txtQuantidade.setText("");
        comboBoxClientes.setSelectedIndex(-1);
        comboBoxProdutos.setSelectedIndex(-1);
    }

    // Método para adicionar um listener ao botão Adicionar
    public void adicionarListenerBtnAdicionar(ActionListener listener) {
        btnAdicionar.addActionListener(listener);
    }

    // Método para obter o ID inserido pelo usuário
    public String getId() {
        return txtId.getText();
    }

    // Método para obter o cliente selecionado no JComboBox
    public String getClienteSelecionado() {
        return (String) comboBoxClientes.getSelectedItem();
    }

    // Método para obter o produto selecionado no JComboBox
    public String getProdutoSelecionado() {
        return (String) comboBoxProdutos.getSelectedItem();
    }

    // Método para obter a quantidade inserida pelo usuário
    public String getQuantidade() {
        return txtQuantidade.getText();
    }
}

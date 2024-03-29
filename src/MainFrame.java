import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;

public class MainFrame extends JFrame implements ActionListener, WindowListener {
    JButton btnListar, btnAdicionar, btnAtualizar, btnRemover, btnFiltrar; // Novo botão adicionado
    JTable table;
    Connection conexao;
    ListagemDoBanco listagemDoBanco;

    public MainFrame(Connection conexao) {
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
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5)); // Alteração no layout do painel de botões
        // Inicializa os botões
        btnListar = new JButton("Listar");
        btnAdicionar = new JButton("Adicionar");
        btnAtualizar = new JButton("Atualizar");
        btnRemover = new JButton("Remover");
        btnFiltrar = new JButton("Filtrar"); // Novo botão adicionado
        // Adiciona ActionListener aos botões
        btnListar.addActionListener(this);
        btnAdicionar.addActionListener(this);
        btnAtualizar.addActionListener(this);
        btnRemover.addActionListener(this);
        btnFiltrar.addActionListener(this); // Novo botão adicionado
        // Adiciona os botões ao painel
        buttonPanel.add(btnListar);
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnFiltrar); // Novo botão adicionado
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
            // Chama o método para listar os itens do banco de dados
            try {
                DefaultTableModel model = listagemDoBanco.listarItens();
                table.setModel(model);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == btnAdicionar) {
            // Abre a janela para adicionar um novo item
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
            // Lógica para filtrar os itens
            JOptionPane.showMessageDialog(this, "Ação do botão Filtrar");
        }
    }

    // Implementação do método windowClosing para fechar a conexão quando o JFrame for fechado
    public void windowClosing(WindowEvent e) {
        // Fechar conexão com o banco de dados
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

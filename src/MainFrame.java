import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener, WindowListener {
    JButton btnProdutos, btnClientes, btnVendas; // Novos botões
    Connection conexao;

    public MainFrame(Connection conexao) {
        super("Menu Principal");
        this.conexao = conexao;

        // Configurações do JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); // Defina o tamanho aqui ou use pack() após adicionar os componentes
        setLocationRelativeTo(null);

        // Layout principal do JFrame (BorderLayout)
        setLayout(new BorderLayout());

        // Cabeçalho com 15% do tamanho da página
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(getWidth(), (int) (getHeight() * 0.15))); // 15% do tamanho da página
        headerPanel.setBackground(new Color(64, 64, 64)); // Fundo cinza escuro
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centralizar os componentes com espaçamento

        // Rótulo para o texto principal no cabeçalho
        JLabel lblPrincipal = new JLabel("Principal");
        lblPrincipal.setForeground(Color.WHITE); // Texto em branco
        lblPrincipal.setFont(new Font("Arial", Font.BOLD, 24)); // Fonte e tamanho

        // Adicionar o rótulo ao cabeçalho
        headerPanel.add(lblPrincipal);

        // Adicionar o cabeçalho ao topo do JFrame
        add(headerPanel, BorderLayout.NORTH);

        // JPanel para os botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 160)); // Layout dos botões
        setLayout(new BorderLayout());

        // Inicializa os botões
        btnProdutos = new JButton("Produtos");
        btnClientes = new JButton("Clientes");
        btnVendas = new JButton("Vendas");

        // Adiciona ActionListener aos botões
        btnProdutos.addActionListener(this);
        btnClientes.addActionListener(this);
        btnVendas.addActionListener(this);

        // Adiciona os botões ao JPanel
        buttonPanel.add(btnProdutos);
        buttonPanel.add(btnClientes);
        buttonPanel.add(btnVendas);

        // Adiciona o JPanel dos botões ao centro do JFrame
        add(buttonPanel, BorderLayout.CENTER);

        // Adiciona o WindowListener ao JFrame
        addWindowListener(this);
        
        // Exibir o JFrame
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnProdutos) {
            // Abrir a tela de produtos
            new ProdutosFrame(conexao);
        } else if (e.getSource() == btnClientes) {
            // Abrir uma nova instância do MainFrame para clientes
            new ClientesFrame(conexao);
        } else if(e.getSource() == btnVendas) {
        	new VendasFrame(conexao);
        }
        }

    // Métodos da interface WindowListener
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}

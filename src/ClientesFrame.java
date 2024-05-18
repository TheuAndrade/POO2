import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;



public class ClientesFrame extends JFrame implements ActionListener {
    private JTable table;
    private Connection conexao;
    private ListagemClientes listagemClientes;

    public ClientesFrame(Connection conexao) {
        super("Clientes");
        this.conexao = conexao;
        this.listagemClientes = new ListagemClientes(conexao);

        // Configurações do JFrame
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal do JFrame (BorderLayout)
        setLayout(new BorderLayout());

        // Iniciar o JTable
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Layout dos botões

        // Botão para listar clientes
        JButton btnListar = new JButton("Listar Clientes");
        btnListar.addActionListener(this);
        buttonPanel.add(btnListar);

        // Botão para adicionar cliente
        JButton btnAdicionar = new JButton("Adicionar Cliente");
        btnAdicionar.addActionListener(this);
        buttonPanel.add(btnAdicionar);

        // Botão para remover cliente
        JButton btnRemover = new JButton("Remover Cliente");
        btnRemover.addActionListener(this);
        buttonPanel.add(btnRemover);

        // Botão para editar cliente
        JButton btnEditar = new JButton("Editar Cliente");
        btnEditar.addActionListener(this);
        buttonPanel.add(btnEditar);

        // Adicionar o JPanel dos botões ao JFrame
        add(buttonPanel, BorderLayout.NORTH);

        // Exibir o JFrame
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Listar Clientes")) {
            listarClientes();
        } else if (e.getActionCommand().equals("Adicionar Cliente")) {
            adicionarCliente();
        } else if (e.getActionCommand().equals("Remover Cliente")) {
            removerCliente();
        } else if (e.getActionCommand().equals("Editar Cliente")) {
            editarCliente();
        }
    }
//////Frames 
    
    private void listarClientes() {
        try {
            DefaultTableModel model = listagemClientes.listarItens();
            table.setModel(model);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void adicionarCliente() {
    	new AdicionarClienteFrame(conexao);
    }

    private void removerCliente() {
        new RemoverCliente(conexao);
    }

    private void editarCliente() {
    	 int selectedRow = table.getSelectedRow();
         if (selectedRow != -1) {
             Object[] itemSelecionado = new Object[table.getColumnCount()];
             for (int i = 0; i < table.getColumnCount(); i++) {
                 itemSelecionado[i] = table.getValueAt(selectedRow, i);
             }
             new EditarClienteFrame(itemSelecionado);
         } else {
             JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
         }
     }
 }
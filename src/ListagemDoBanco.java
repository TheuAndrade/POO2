import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListagemDoBanco {
    private Connection conexao;

    public ListagemDoBanco(Connection conexao) {
        this.conexao = conexao;
    }

    public DefaultTableModel listarItens() throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // Criar uma declaração SQL
            stmt = conexao.createStatement();
            // Executar a consulta SQL para obter os dados
            rs = stmt.executeQuery("SELECT * FROM produtos");

            // Adicionar as colunas ao modelo
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                model.addColumn(rs.getMetaData().getColumnName(i));
            }

            // Adicionar as linhas ao modelo
            while (rs.next()) {
                Object[] row = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } finally {
            // Fechar ResultSet e Statement
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

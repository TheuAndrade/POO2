import java.sql.Connection;
import javax.swing.SwingUtilities;

public class main {
    public static void main(String[] args) {
        // Estabelecer conexão com o banco de dados
        Connection conexao = DataBaseConnection.conectar();

        // Criar uma instância de MainFrame e passar a conexão com o banco de dados para ele
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame(conexao);
            }
        });
    }
}
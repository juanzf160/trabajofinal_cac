package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.ConexionDB;


@WebServlet("/vistas/LoginServlet")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (autenticarUsuario(username, password)) {
            
            HttpSession session = request.getSession();
            session.setAttribute("usuario", username);
            response.sendRedirect("gestionOradores.jsp"); // Redirigir a la página de gestion
        } else {
           
            request.setAttribute("error", "Nombre de usuario o contraseña inválidos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private boolean autenticarUsuario(String username, String password) {
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        
        
        try {
            conn = ConexionDB.conectar(); // Asume que tienes un método estático getConnection en tu clase Conexion
            String sql = "SELECT * FROM login WHERE usuario = ? AND contraseña = ?"; // Asegúrate de que esta consulta coincida con tu esquema de base de datos
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            return rs.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }
}

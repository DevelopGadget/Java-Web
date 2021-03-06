package Controller;

import Model.Equipo;
import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Actualizaciones", urlPatterns = {"/Actualizaciones"})
public class Actualizaciones extends HttpServlet {

    EquipoController _Service = new EquipoController();
    Equipo _Equipo;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Editar.jsp");
        _Equipo = new Equipo(request.getParameter("Id"), request.getParameter("Nombre"), request.getParameter("Estadio"),
                request.getParameter("UrlEscudo"), request.getParameter("UrlEstadio"));
        if (request.getParameter("Metodo").equals("Eliminar")) {
            try {
                dispatcher = getServletContext().getRequestDispatcher("/");
                _Service.Delete(_Equipo.getId());
                request = _Service.RequestAtributos(request, "success", "Eliminado", "Eliminado correctamente de la base de datos", _Equipo);
                
                dispatcher.forward(request, response);
            } catch (SQLException ex) {
                request = _Service.RequestAtributos(request, "error", "Error con la Base de Datos", ex.toString().replace("'", ""), _Equipo);
                dispatcher.forward(request, response);
            }
        } else {
            if (_Service.VerificarImagen(_Equipo.getUrlEscudo()) && _Service.VerificarImagen(_Equipo.getUrlEstadio())) {
                try {
                    _Service.Update(_Equipo);
                    request.setAttribute("Estado", "success");
                    request.setAttribute("Titulo", "Editado");
                    request.setAttribute("Mensaje", "Editado correctamente a la base de datos");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    request = _Service.RequestAtributos(request, "success", "Editado", "Editado correctamente de la base de datos", _Equipo);
                    dispatcher.forward(request, response);
                }
            } else {
                request = _Service.RequestAtributos(request, "error", "Error en las imagenes", "Imagen no encontrada", _Equipo);
                dispatcher.forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Actualizaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Actualizaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import command.*;
import command.chemieboxcommand.ChemieboxControllerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ldap.Authenticator;
import service.*;

/**
 *
 * @author r0431118
 */
@WebServlet(name = "ChemieboxServlet", urlPatterns = {"/ChemieboxController"})
public class ChemieboxServlet extends HttpServlet {

    private FacadeInterface facade;
    private ChemieboxControllerFactory chemieboxControllerFactory;

    public void init() {
        facade = (FacadeInterface) getServletContext().getAttribute("facade");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        chemieboxControllerFactory = new ChemieboxControllerFactory(facade);
        String action = request.getParameter("action");
        
        HttpSession session = request.getSession();

        if(("login").equals(action)){
            
        }
        else if(Authenticator.isLoggedIn(request)==false){
            action="getHomePage";
        }
        Logger.getAnonymousLogger().log(Level.SEVERE, "action="+action);
        Command command = chemieboxControllerFactory.getController(action);
        
        if(session.getAttribute("level")==null&&command.getLevel()>0){
            
            command = chemieboxControllerFactory.getController("startTest");
            
        }
        
        String destination = command.execute(request,response);
        
        RequestDispatcher view = request.getRequestDispatcher(destination);
        try{
            if(!destination.equals("")){
                view.forward(request, response);
            }
        
        }
        catch(java.lang.IllegalStateException e){
            
             Logger.getAnonymousLogger().log(Level.SEVERE, null, e.getMessage());
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
        
            processRequest(request, response);
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
        
        processRequest(request, response);
       
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

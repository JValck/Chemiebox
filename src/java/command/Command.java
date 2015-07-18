/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author r0431118
 */
public interface Command {    
    public String execute(HttpServletRequest request, HttpServletResponse response);  
    public int getLevel();
}

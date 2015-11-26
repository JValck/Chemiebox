/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ldap;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Fery
 */
public class Authenticator {
    
    public boolean authenticate(String studnr, String password){
        //return true;
        boolean authenticated;
        String domain = "UCLL";
        try {
            Hashtable env = new Hashtable(11);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://ad.ucll.be:636");
            env.put(Context.SECURITY_PROTOCOL, "ssl");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, domain + "\\" + studnr);
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put("java.naming.ldap.factory.socket", "ldap.MySSLSocketFactory");
            LdapContext ctx = new InitialLdapContext(env, null);
            authenticated = true;
            
            //System.out.println("Successful SSL authentication with server.");
        } catch (Exception e) {
            e.printStackTrace();
            authenticated = false;
        }
        return authenticated;
    }
    
    public static boolean isLoggedIn(HttpServletRequest request){
        HttpSession session = request.getSession();
        boolean correct=session.getAttribute("userId")!=null;
        
       return correct;
    }
}

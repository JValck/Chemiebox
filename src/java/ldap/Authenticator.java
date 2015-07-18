/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ldap;

import com.novell.ldap.LDAPSocketFactory;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author Fery
 */
public class Authenticator {
    
    public boolean authenticate(String studnr, String password){
        //return true;
        boolean authenticated;
        String server = "rolf.khleuven.be";
        int port = 636;
        String domain = "KHLEUVEN";
        LDAPSocketFactory ssf;
        try {
            Hashtable env = new Hashtable(11);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://rolf.khleuven.be:636");
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

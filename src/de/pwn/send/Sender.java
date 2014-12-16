/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.send;

import de.pwn.client.Client;
import de.pwn.client.ClientBuilder;
import de.pwn.transport.Delivery;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pwn
 */
public class Sender
{
    private Client client;
    
    public Sender(String ip, int port)
    {       
        client      = ClientBuilder.buildTCPClient(ip, port);
    }
        
    public void send(Delivery delivery)
    {
        try 
        {
            client.send(delivery);
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean checkAuthority()
    {
        boolean isValid = false;
        try 
        {
            isValid     = client.checkAuthority();
        } 
        catch (Exception ex) 
        {
            //Do nothing
            //Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, "not allowed");
        }
        return isValid;
    }
    
    public void logout()
    {
        try 
        {
            client.logout();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}

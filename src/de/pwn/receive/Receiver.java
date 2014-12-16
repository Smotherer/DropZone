/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.receive;

import de.pwn.server.Server;
import de.pwn.server.ServerBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pwn
 */
public class Receiver
{
    private Server server;
    
    public Receiver(int port)
    {       
        server = ServerBuilder.buildTCPServer(port);
    }
    
    public void startServer()
    {
        try 
        {
            server.startServer();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

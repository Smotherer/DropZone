/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.gui;

import de.pwn.send.Sender;
import de.pwn.send.SenderBuilder;
import de.pwn.settings.Settings;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author pwn
 */
public class DropZoneWindowListener implements WindowListener
{

    @Override
    public void windowOpened(WindowEvent e) 
    {
        //Do Noting
    }

    @Override
    public void windowClosing(WindowEvent e) 
    {     
        for (String existingDevice : DropZoneGui.getInstance().getAllowedDevices()) 
        {
            Sender sender = SenderBuilder.buildSender(existingDevice, Settings.SOCKET_PORT);
            sender.logout();
        }  
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) 
    {
        //Do Noting
    }

    @Override
    public void windowIconified(WindowEvent e) 
    {
        //Do Noting
    }

    @Override
    public void windowDeiconified(WindowEvent e) 
    {
       //Do Noting
    }

    @Override
    public void windowActivated(WindowEvent e) 
    {
       //Do Noting
    }

    @Override
    public void windowDeactivated(WindowEvent e) 
    {
        //Do Noting
    }
    
}

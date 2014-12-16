/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.network;

import de.pwn.gui.DropZoneGui;
import de.pwn.send.Sender;
import de.pwn.send.SenderBuilder;
import de.pwn.settings.Settings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pwn
 */
public class NetworkUtils 
{
    private static String ownIp ;
    static
    {
        try 
        {
            NetworkUtils.ownIp = searchOwnIpWindows();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(NetworkUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public static void searchAllowedDevices() throws Exception
    {
        List<String> reachAbleDevices   = searchReachableAddressesWindows();
        List<String> allowedDevices     = new ArrayList<>();
        
        for (String reachAbleDevice : reachAbleDevices) 
        {
            try 
            {
                Sender sender = SenderBuilder.buildSender(reachAbleDevice, Settings.SOCKET_PORT);
                if (sender.checkAuthority()) 
                {
                    DropZoneGui.getInstance().addDeviceSilent(reachAbleDevice);
                }
            }
            catch(Exception ex)
            {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static List<String> searchReachableAddressesWindows() throws IOException
    {
        List<String> reachAbleAdresses  = new ArrayList<>();
        
        Process p                       = Runtime.getRuntime().exec ("net view /all");  
        InputStream in                  = p.getInputStream();  
        InputStreamReader reader        = new InputStreamReader(in);
        BufferedReader bufferedReader   = new BufferedReader(reader);
        String line;
        while((line = bufferedReader.readLine()) != null)
        {
            if(line.startsWith("\\\\"))
            {
                String deviceId = line.substring(2, line.indexOf(" "));
                if(deviceId.contains(NetworkUtils.ownIp))
                {
                }
                else
                {
                    reachAbleAdresses.add(deviceId);
                }                
            }
        }
        return reachAbleAdresses;
    }

    private static String searchOwnIpWindows() throws IOException
    {
        Process p                       = Runtime.getRuntime().exec ("hostname");  
        InputStream in                  = p.getInputStream();  
        InputStreamReader reader        = new InputStreamReader(in);
        BufferedReader bufferedReader   = new BufferedReader(reader);
        String line                     = bufferedReader.readLine();
        return line;
    }
}

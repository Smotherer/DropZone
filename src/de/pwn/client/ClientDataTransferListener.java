/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.client;

import de.pwn.gui.DropZoneGui;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import java.io.File;

/**
 *
 * @author pwn
 */
public class ClientDataTransferListener implements FTPDataTransferListener
{
    private final long absolutLength;       
    private long currentLength;

    public ClientDataTransferListener(File fileToUpload) 
    {
        this.absolutLength  = getSize(fileToUpload);
        this.currentLength  = 0;
    }

    public static long getSize(File file) 
    {
        long size;
        if (file.isDirectory()) 
        {
            size = 0;
            for (File child : file.listFiles())
            {
                size += getSize(child);
            }
        } 
        else 
        {
            size = file.length();
        }
        return size;
    }

    @Override
    public void started() 
    {
        System.out.println("Started");
    }

    @Override
    public void transferred(int i) 
    {
        currentLength = currentLength + i;
        DropZoneGui.getInstance().updateDownloadProgressbar((int)((currentLength * 100) / this.absolutLength));
    }

    @Override
    public void completed() 
    {
        System.out.println("Completed");
    }

    @Override
    public void aborted() 
    {
        System.out.println("Aborted");
    }

    @Override
    public void failed() 
    {
        System.out.println("Faild");
    }  
}

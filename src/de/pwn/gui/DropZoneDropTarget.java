/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.gui;

import de.pwn.send.Sender;
import de.pwn.send.SenderBuilder;
import de.pwn.settings.Settings;
import de.pwn.transport.DeliveryBuilder;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Patrick
 */
public class DropZoneDropTarget extends DropTarget
{
    private final DropZoneGui parent;

    public DropZoneDropTarget(DropZoneGui parent)
    {
        this.parent = parent;
    }

    @Override
    public synchronized void drop(DropTargetDropEvent evt) 
    {
        try 
        {
            evt.acceptDrop(DnDConstants.ACTION_COPY);
            Transferable transfer           = evt.getTransferable();
            final List<File> droppedFiles   = (List<File>) transfer.getTransferData(DataFlavor.javaFileListFlavor);
            Runnable sendFiles = new Runnable() {

                @Override
                public void run() 
                {
                    for (File file : droppedFiles) 
                    {
                        parent.setAct(DropZoneGui.ACT_3);
                        for (String device : parent.getAllowedDevices()) 
                        {
                            Sender sender = SenderBuilder.buildSender(device, Settings.SOCKET_PORT);
                            sender.send(DeliveryBuilder.buildDeliveryFilePart(file));
                        }
                        parent.setAct(DropZoneGui.ACT_2);
                    }
                }
            };
            new Thread(sendFiles).start();
        } 
        catch (UnsupportedFlavorException | IOException ex) 
        {
            Logger.getLogger(DropZoneGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.transport;

import java.io.File;

/**
 *
 * @author pwn
 */
public class DeliveryFile implements Delivery
{
    private File file;
    
    public DeliveryFile(File file) 
    {
        this.file = file;
    }

    public File getFile() 
    {
        return file;
    }
}

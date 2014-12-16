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
public class DeliveryBuilder 
{    
    public static Delivery buildDeliveryLogout()
    {
        return new DeliveryLogout();
    }
    
    public static Delivery buildDeliveryAuthority()
    {
        return new DeliveryAuthority();
    }
    
    public static Delivery buildDeliveryDirectory(File file)
    {
        return buildDeliveryDirectory(file.getAbsolutePath());
    }
    
    public static Delivery buildDeliveryDirectory(String directory)
    {
        return new DeliveryDirectory(directory);
    }
    
    public static Delivery buildDeliveryValue(String value)
    {
        return new DeliveryValue(value);
    }
    
    public static Delivery buildDeliveryCommand(String command)
    {
        return new DeliveryCommand(command);
    }
    
    public static Delivery buildDeliveryFilePart(File file)
    {
        return new DeliveryFile(file);
    }
    
    public static Delivery buildDeliveryFilePart(byte[] fileName, byte[] content, int part, int size)
    {
        return new DeliveryFilePart(fileName, content, part, size);
    }
}

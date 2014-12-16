/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.transport;

/**
 *
 * @author pwn
 */
public class DeliveryDirectory implements Delivery
{
    private String directory;
    
    public DeliveryDirectory(String directory) 
    {
        this.directory = directory;
    }

    public String getDirectory() 
    {
        return directory;
    }
}

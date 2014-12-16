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
public class DeliveryCommand implements Delivery
{
    private String command;
    
    public DeliveryCommand(String command) 
    {
        this.command = command;
    }

    public String getCommand() 
    {
        return command;
    }
}

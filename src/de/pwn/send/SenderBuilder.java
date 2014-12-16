/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.send;

/**
 *
 * @author pwn
 */
public class SenderBuilder 
{    
    public static Sender buildSender(String ip, int port)
    {
        return new Sender(ip, port);
    }
}

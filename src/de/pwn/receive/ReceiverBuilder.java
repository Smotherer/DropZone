/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.receive;

/**
 *
 * @author pwn
 */
public class ReceiverBuilder 
{
    public static Receiver buildReceiver(int port)
    {
        return new Receiver(port);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.client;

/**
 *
 * @author pwn
 */
public class ClientBuilder 
{
    public static Client buildTCPClient(String ip, int port)
    {
        return new TCPClient(ip, port);
    }
}

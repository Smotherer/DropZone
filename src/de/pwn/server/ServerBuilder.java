/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.server;

/**
 *
 * @author pwn
 */
public class ServerBuilder 
{
    public static Server buildTCPServer(int port)
    {
        return new TCPServer(port);
    }
}

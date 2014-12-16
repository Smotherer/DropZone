/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.client;

import de.pwn.transport.Delivery;

/**
 *
 * @author pwn
 */
public interface Client 
{
    public void send(Delivery delivery) throws Exception;
    public boolean checkAuthority() throws Exception;
    public void logout() throws Exception;
}

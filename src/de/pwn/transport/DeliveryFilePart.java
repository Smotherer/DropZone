/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.transport;

/**
 *
 * @author Patrick
 */
public class DeliveryFilePart implements Delivery
{
    private byte[] fileName;
    private byte[] content;
    private int part;
    private int size;
    
    public DeliveryFilePart(byte[] fileName, byte[] content, int part, int size) 
    {
        this.fileName = fileName;
        this.content = content;
        this.part = part;
        this.size = size;
    }

    public byte[] getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public int getPart() {
        return part;
    }

    public int getSize() {
        return size;
    }    
}

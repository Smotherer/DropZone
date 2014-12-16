/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.client;

import de.pwn.gui.DropZoneGui;
import de.pwn.settings.Settings;
import de.pwn.transport.Delivery;
import de.pwn.transport.DeliveryAuthority;
import de.pwn.transport.DeliveryBuilder;
import de.pwn.transport.DeliveryFile;
import it.sauronsoftware.ftp4j.FTPClient;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author pwn
 */
public class TCPClient implements Client
{
    private final String ip;
    private final int port;
    private Socket socket;
    private FTPClient ftpClient;
    
    public TCPClient(String ip, int port) 
    {
        this.ip             = ip;
        this.port           = port;
    }
    
    private void connect() throws Exception
    {
        socket      = new Socket(ip, port);
    }
    
    private void connectFtp() throws Exception
    {
        ftpClient   = new FTPClient();
        ftpClient.connect(ip, (port + 1));
        ftpClient.login(Settings.ANONYMOUS, Settings.ANONYMOUS);
    }
    
    private void shortConnect() throws Exception
    {
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), Settings.SOCKET_TIMEOUT);
    }
    
    public void disconnect() throws Exception
    {
        if(null != socket)
        {
            socket.close();
        }
    }
    
    public void disconnectFtp() throws Exception
    {
        if(ftpClient != null)
        {
            ftpClient.logout();
        }
    }
    
    @Override
    public void send(Delivery delivery) throws Exception
    {
        /*
        connect();
        if(null != socket && null != ftpClient)
        {
            try (ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream())) 
            {
                if(delivery instanceof DeliveryFile)
                {
                    DeliveryFile deliveryFile   = (DeliveryFile)delivery;
                    uploadFile(deliveryFile, ftpClient);
                    DropZoneGui.getInstance().reNewUploadProgressbar();
                }
                
                else if(delivery instanceof DeliveryDirectory)
                {
                    DeliveryDirectory deliveryDirectory = (DeliveryDirectory) delivery;
                    createDirectory(deliveryDirectory, ftpClient);
                    DropZoneGui.getInstance().reNewUploadProgressbar();
                }
                
                objOut.reset();
                objOut.flush();
                objOut.close();
            }
        }
        else
        {
           throw new NullPointerException("Connection not initialized!"); 
        }
        close();
        */
        connectFtp();
        if(null != ftpClient)
        {
            if(delivery instanceof DeliveryFile)
            {
                DeliveryFile deliveryFile   = (DeliveryFile)delivery;
                uploadFile(deliveryFile, ftpClient);
                DropZoneGui.getInstance().reNewUploadProgressbar();
            }
        }
        disconnectFtp();
    }

    @Override
    public boolean checkAuthority() throws Exception
    {    
        shortConnect();
        boolean isValid = false;
        if(null != socket)
        {
            try (ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream())) 
            {
                DeliveryAuthority deliveryAuthority = (DeliveryAuthority)DeliveryBuilder.buildDeliveryAuthority();
                isValid                             = checkAuthority(deliveryAuthority, objOut, socket);
                objOut.flush();
                objOut.close();
            }
        }
        disconnect();
        return isValid;
    }
    
    @Override
    public void logout() throws Exception 
    {
        connect();
        if(null != socket)
        {
            
            try (ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream())) 
            {
                Delivery logout = DeliveryBuilder.buildDeliveryLogout();
                objOut.writeObject(logout);
                objOut.reset();
                objOut.flush();
                objOut.close();
            }
        }
        else
        {
           throw new NullPointerException("Connection not initialized!"); 
        }
        disconnect();
    }
    
    private boolean checkAuthority(DeliveryAuthority deliveryAuthority, ObjectOutputStream objOut, Socket socket) throws Exception 
    {
        boolean isValid = false;
        objOut.writeObject(deliveryAuthority);
        try (ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream())) 
        {
            Delivery delivery           = (Delivery)objIn.readObject();
            if(delivery instanceof DeliveryAuthority)
            {
                isValid = true;
            }
            objIn.close();
        }
        return isValid;
    }
    
    private void uploadFile(DeliveryFile deliveryFile, FTPClient ftpClient) throws Exception
    {
        File fileToUpload                               = deliveryFile.getFile();
        ClientDataTransferListener dataTransferListener = new ClientDataTransferListener(fileToUpload);
        if(fileToUpload.isDirectory())
        {
            uploadMultipleFiles(ftpClient, fileToUpload, dataTransferListener);
        }
        else
        {
            uploadSingleFile(ftpClient, fileToUpload, dataTransferListener);
        }
        DropZoneGui.getInstance().reNewUploadProgressbar();
    }
    
    private void uploadMultipleFiles(FTPClient ftpClient, File directoryToUpload, ClientDataTransferListener dataTransferListener) throws Exception
    {
        if(directoryToUpload.isDirectory())
        {
            ftpClient.createDirectory(directoryToUpload.getName());
            StringBuilder directoryBuilder = new StringBuilder();
            directoryBuilder.append(ftpClient.currentDirectory());
            directoryBuilder.append(Settings.FILE_PATH_SEPERATOR);
            directoryBuilder.append(directoryToUpload.getName());
            ftpClient.changeDirectory(directoryBuilder.toString());
        }
        
        File[] filesToUpload = directoryToUpload.listFiles();
        for (File fileToUpload : filesToUpload) 
        {
            if(fileToUpload.isDirectory())
            {
                uploadMultipleFiles(ftpClient, fileToUpload, dataTransferListener);
            }
            else
            {
                uploadSingleFile(ftpClient, fileToUpload, dataTransferListener); 
            }
        }
    }
    
    private void uploadSingleFile(FTPClient ftpClient, File fileToUpload, ClientDataTransferListener dataTransferListener) throws Exception
    {
        ftpClient.upload(fileToUpload, dataTransferListener); 
    }
    /*
    private void createDirectory(DeliveryDirectory deliveryDirectory, FTPClient ftpClient) throws Exception 
    {
        ftpClient.createDirectory(deliveryDirectory.getDirectory());
    }
    */
}

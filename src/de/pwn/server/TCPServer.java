/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.server;

import de.pwn.gui.DropZoneGui;
import de.pwn.settings.Settings;
import de.pwn.transport.Delivery;
import de.pwn.transport.DeliveryAuthority;
import de.pwn.transport.DeliveryBuilder;
import de.pwn.transport.DeliveryLogout;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

class TCPServer extends Observable implements Server
{    
    private final int port;
    private ServerSocket serverSocket;
    private FtpServer ftpServer;
    
    public TCPServer(int port) 
    {
        this.port = port;
    }
    
    @Override
    public void startServer() throws Exception
    {
        this.serverSocket  = new ServerSocket(port);
        
        //ServerSocket
        Runnable accepting = new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {  
                    while(true)
                    {
                        final Socket socket = serverSocket.accept();
                        Runnable listening = new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                try 
                                {
                                    try (ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream())) 
                                    {
                                        Delivery delivery                   = (Delivery)objIn.readObject();
                                        /*
                                        if(delivery instanceof DeliveryCommand)
                                        {
                                            DeliveryCommand command         = (DeliveryCommand) delivery;
                                            if(null != command.getCommand())
                                            {
                                                switch (command.getCommand()) 
                                                {
                                                    case Commands.FILE_UPLOAD_START:
                                                        DropZoneGui.getInstance().setAct(DropZoneGui.ACT_4);
                                                        downloadFile(objIn);
                                                        DropZoneGui.getInstance().reNewDownloadProgressbar();
                                                        break;
                                                    case Commands.DIRECTORY_UPLOAD_START:
                                                        DropZoneGui.getInstance().setAct(DropZoneGui.ACT_4);
                                                        downloadDirectory(objIn);
                                                        DropZoneGui.getInstance().reNewDownloadProgressbar();
                                                        break;
                                                    case Commands.CREATE_DIRECTORY:
                                                        createDirectory(objIn);
                                                        break;
                                                }
                                                DropZoneGui.getInstance().setAct(DropZoneGui.ACT_2);
                                            }
                                        }
                                        */
                                        if(delivery instanceof DeliveryAuthority)
                                        {
                                            checkAuthority(socket);
                                        }
                                        else if(delivery instanceof DeliveryLogout)
                                        {
                                            logout(socket);
                                        }
                                        objIn.close();
                                    }
                                } 
                                catch (Exception ex) 
                                {
                                    Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        };
                        Thread listeningToCurrentPort = new Thread(listening);
                        listeningToCurrentPort.start();
                    }
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Thread acceptingToCurrentPort = new Thread(accepting);
        acceptingToCurrentPort.start();
        
        //Start FTP
        ListenerFactory factory         = new ListenerFactory();
        factory.setPort((port + 1));

        FtpServerFactory serverFactory  = new FtpServerFactory();
        serverFactory.addListener(Settings.FTP_LISTENER_TYPE, factory.createListener());

        UserManager userManager         = createUserFile(Settings.ANONYMOUS, Settings.ANONYMOUS, Settings.UPLOAD_DIRECTORY);
        serverFactory.setUserManager(userManager);

        ftpServer = serverFactory.createServer();
        ftpServer.start();
    }
    
    public static UserManager createUserFile(final String username, final String password, final String ftproot) throws Exception 
    {        
        WritePermission writePermission                 = new WritePermission();
        List authorities                                = new ArrayList();
        authorities.add(writePermission);
        
        BaseUser user                                   = new BaseUser();
        user.setName(username);
        user.setPassword(password);
        user.setHomeDirectory(ftproot);
        user.setAuthorities(authorities);
        
        File userFile                                   = new File(Settings.USER_PROPTERIES);
        userFile.createNewFile();
        
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(userFile);
        userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
        
        UserManager um                                  = userManagerFactory.createUserManager();
        um.save(user);
        
        return um;
    }
    
    /*
    private void createDirectory(ObjectInputStream objIn) throws Exception 
    {
        DeliveryValue directoryPath     = (DeliveryValue)objIn.readObject();
        File directory                  = new File(Settings.UPLOAD_DIRECTORY + directoryPath.getValue());   
        if(!directory.exists())
        {
            directory.mkdirs();
        }
    }
    */
    
    private void checkAuthority(Socket socket) throws Exception 
    {
        try (ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream())) 
        {
            objOut.writeObject(DeliveryBuilder.buildDeliveryAuthority());
            objOut.flush();
            objOut.close();
            DropZoneGui.getInstance().addDevice(socket.getInetAddress().getHostName());
        }
    }
    
    private void logout(Socket socket)
    {
        DropZoneGui.getInstance().removeDevice(socket.getInetAddress().getHostName());
    }
}

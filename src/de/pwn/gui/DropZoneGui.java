/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.gui;

import de.pwn.network.NetworkUtils;
import de.pwn.server.Server;
import de.pwn.server.ServerBuilder;
import de.pwn.settings.Settings;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

/**
 *
 * @author Patrick
 */
public class DropZoneGui extends JFrame
{
    public static final String ACT_1 = "1";
    public static final String ACT_2 = "2";
    public static final String ACT_3 = "3";
    public static final String ACT_4 = "4";
    public static final String ACT_5 = "5";
    
    private Server server;
    
    private JPanel activities;
    private JButton openfileChooser;
    private JFileChooser fileChooser;
    private JButton startSearch;
    private JProgressBar uploadBar;
    private JProgressBar downloadBar; 
    
    private final List<String> allowedDevices = new ArrayList<>();
    
    private static DropZoneGui instance;
    
    private DropZoneGui()
    {
        initServer();
        setupGui();
        setVisible(true);
    }
    
    public static DropZoneGui getInstance()
    {
        if(null == instance)
        {
            instance = new DropZoneGui();
        }
        return instance;
    }

    private void setupGui()
    {
        addWindowListener(new DropZoneWindowListener());
        setTitle("DropZone");

        try 
        {       
            setIconImage(ImageIO.read(new File("./pics/icon.png")));
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(DropZoneGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        setResizable(false);
        setSize(290, 165);
        setGuiToCenter();
        setActivities();
    }

    private void setGuiToCenter() 
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
    }
    
    private void setActivities() 
    {
        try 
        {
            Border emptyBorder = BorderFactory.createEmptyBorder();
            activities  = (JPanel)getContentPane();
            activities.setLayout(new CardLayout());
            activities.setBackground(Color.WHITE);
            
            JPanel settings = new JPanel();
            settings.setLayout(new GridLayout(1, 2));
            settings.setBackground(Color.WHITE);
            openfileChooser = new JButton();            
            openfileChooser.setBorder(emptyBorder);
            openfileChooser.setBackground(Color.WHITE);
            ImageIcon fileChooserIcon = new ImageIcon(ImageIO.read(new File("./pics/filechooser.png")));
            openfileChooser.setIcon(fileChooserIcon);
            fileChooser     = new JFileChooser();
            openfileChooser.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    fileChooser.setCurrentDirectory(new File(Settings.UPLOAD_DIRECTORY));
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    while(true)
                    {
                        int chosen = fileChooser.showOpenDialog(DropZoneGui.getInstance());
                        if(chosen == JFileChooser.APPROVE_OPTION)
                        {
                            File selectedFile = fileChooser.getSelectedFile();
                            if(selectedFile.canWrite())
                            {
                                Settings.UPLOAD_DIRECTORY = fileChooser.getSelectedFile().getAbsolutePath();
                                break;
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(DropZoneGui.getInstance(), "You dont have write right in this directory.\n Please choose an other one.");
                            }
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            });
            settings.add(openfileChooser);
            startSearch                 = new JButton();
            startSearch.setBackground(Color.WHITE);
            startSearch.setBorder(emptyBorder);
            ImageIcon startSearchIcon   = new ImageIcon(ImageIO.read(new File("./pics/play.png")));
            startSearch.setIcon(startSearchIcon);
            startSearch.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    initAllowedDevices();
                }
            });
            settings.add(startSearch);
            
            activities.add(settings, ACT_1);
            
            JPanel drop = new JPanel();
            drop.setLayout(new GridLayout(1, 1));
            drop.setBackground(Color.WHITE);
            ImageIcon dropIcon      = new ImageIcon(ImageIO.read(new File("./pics/drop.png")));
            JLabel dropBackground   = new JLabel(dropIcon);
            dropBackground.setBounds(0, 0, 300, 300);
            drop.add(dropBackground);
            drop.setDropTarget(new DropZoneDropTarget(this));
            activities.add(drop, ACT_2);
            
            JPanel upload                   = new JPanel();
            upload.setLayout(new GridLayout(1, 2));
            upload.setBackground(Color.WHITE);
            ImageIcon uploadIcon            = new ImageIcon(ImageIO.read(new File("./pics/upload.png")));
            JLabel uploadBackground         = new JLabel(uploadIcon);
            upload.add(uploadBackground);
            uploadBar = new JProgressBar();
            uploadBar.setStringPainted(true);
            upload.add(uploadBar);
            activities.add(upload, ACT_3);
            
            JPanel download                 = new JPanel();
            download.setLayout(new GridLayout(1, 2));
            download.setBackground(Color.WHITE);
            ImageIcon downloadIcon          = new ImageIcon(ImageIO.read(new File("./pics/download.png")));
            JLabel downloadBackground       = new JLabel(downloadIcon);
            download.add(downloadBackground);
            downloadBar = new JProgressBar();
            downloadBar.setStringPainted(true);
            download.add(downloadBar);
            activities.add(download, ACT_4);
            
            JPanel searchNetwork                     = new JPanel();
            searchNetwork.setLayout(new GridLayout(1, 1));
            searchNetwork.setBackground(Color.WHITE);
            ImageIcon searchNetworkIcon              = new ImageIcon(ImageIO.read(new File("./pics/network.png")));
            JLabel searchNetworkBackground           = new JLabel(searchNetworkIcon);
            searchNetwork.add(searchNetworkBackground);
            activities.add(searchNetwork, ACT_5);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(DropZoneGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void setAct(String act)
    {
        CardLayout cl   = (CardLayout)activities.getLayout();
        cl.show(activities, act);
    }

    private void initAllowedDevices() 
    {
        Runnable checkFirstTimeDevices = new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    setAct(ACT_5);
                    NetworkUtils.searchAllowedDevices();
                } 
                catch (Exception ex) 
                {
                    setAct(ACT_1);
                    Logger.getLogger(DropZoneGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(allowedDevices.isEmpty())
                {
                    JOptionPane.showMessageDialog(DropZoneGui.getInstance(), "No devices found!");
                    setAct(ACT_1);
                }
                else
                {
                    if(Settings.DEBUG)
                    {
                        showAllowedDevices();
                    }
                    setAct(ACT_2);
                }                                
            }
        };
        new Thread(checkFirstTimeDevices).start();
    }

    public List<String> getAllowedDevices() 
    {
        return allowedDevices;
    }
    
    public void addDeviceSilent(String device)
    {
        for (String existingDevice : allowedDevices) 
        {
            if(device.equals(existingDevice))
            {
                return;
            }
        }  
        allowedDevices.add(device);
    }
        
    public void addDevice(String device)
    {
        boolean wasFirst    = false;
        if(allowedDevices.isEmpty())
        {
            wasFirst        = true;
        }
        
        addDeviceSilent(device);
        
        if(wasFirst)
        {
            if(Settings.DEBUG)
            {
                JOptionPane.showMessageDialog(DropZoneGui.getInstance(), device + " logged in as first device!");
            }
            else
            {
                JOptionPane.showMessageDialog(DropZoneGui.getInstance(), "A device logged in!");
            }
            setAct(ACT_2);
        }
    }
    
    public void removeDevice(String device) 
    {
        for (String existingDevice : allowedDevices) 
        {
            if(device.equals(existingDevice))
            {
                allowedDevices.remove(existingDevice);
                break;
            }
        } 
        if(allowedDevices.isEmpty())
        {
            if(Settings.DEBUG)
            {
                JOptionPane.showMessageDialog(DropZoneGui.getInstance(), device + " logged out as last device!");
            }
            else
            {
                JOptionPane.showMessageDialog(DropZoneGui.getInstance(), "All devices logged out!");
            }            
            setAct(ACT_1);
        }
    }

    public void updateUploadProgressbar(int progress) 
    {
        uploadBar.setValue(progress);
        uploadBar.update(uploadBar.getGraphics());
    }
    
    public void reNewUploadProgressbar()
    {
        uploadBar = new JProgressBar();
    }
    
    public void updateDownloadProgressbar(int progress) 
    {
        downloadBar.setValue(progress);
        downloadBar.update(downloadBar.getGraphics());
    }
    
    public void reNewDownloadProgressbar()
    {
        downloadBar = new JProgressBar();
    }
      
    private void initServer() 
    {
        server = ServerBuilder.buildTCPServer(Settings.SOCKET_PORT);
        try 
        {
            server.startServer();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(DropZoneGui.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void showAllowedDevices()
    {
        StringBuilder devicesBuilder = new StringBuilder();
        devicesBuilder.append("Devices found:");
        devicesBuilder.append(System.lineSeparator());
        for (String allowedDevice : allowedDevices) 
        {
            devicesBuilder.append(allowedDevice);
            devicesBuilder.append(System.lineSeparator());
        }                    
        JOptionPane.showMessageDialog(DropZoneGui.getInstance(), devicesBuilder.toString());
    }
}

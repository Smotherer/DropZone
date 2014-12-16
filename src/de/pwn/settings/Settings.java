/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.settings;

/**
 *
 * @author pwn
 */
public class Settings 
{    
    public static boolean DEBUG                             = false;
    public static final String FILE_PATH_SEPERATOR          = "\\";
    public static final String CHAR_SET                     = "UTF-8";
    public static final int SOCKET_PACKAGE_SIZE             = 4096;
    public static final int SOCKET_TIMEOUT                  = 50;    
    public static final int SOCKET_PORT                     = 1623;
    public static final int MAX_NUMBER_OF_IPS_TO_CHECK      = 255;    
    public static final String ANONYMOUS                    = "anonymous";
    public static String UPLOAD_DIRECTORY                   = System.getProperty("user.home") + FILE_PATH_SEPERATOR + "Downloads";
    public static String USER_PROPTERIES                    = "settings/users.properties";
    public static String FTP_LISTENER_TYPE                  = "default";
}

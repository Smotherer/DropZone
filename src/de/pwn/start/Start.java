/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.pwn.start;

import de.pwn.gui.DropZoneGui;
import de.pwn.settings.Settings;

/**
 *
 * @author Patrick
 */
public class Start 
{
    public static void main(String[] args) throws Exception
    {
        if(args.length > 0)
        {
            if(args[0].equalsIgnoreCase("debug"))
            {
                Settings.DEBUG = true;
            }
        }
        DropZoneGui.getInstance();             
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author arun
 */
public class imageresize {
    
    public ImageIcon get(ImageIcon i)
    {
        Image ij=i.getImage();
       Image s=ij.getScaledInstance(30, 30,Image.SCALE_SMOOTH);
       ImageIcon f=new ImageIcon(s);
       return f;
    }
}

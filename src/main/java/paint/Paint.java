package paint;

import com.mysql.jdbc.PreparedStatement;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author arun
 */
public class Paint extends JFrame implements MouseListener, ActionListener,MouseMotionListener {
    
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    ArrayList<JButton> tempbutton = new ArrayList<JButton>();
    int ovalclick = 0, lineclick = 0,rectclick=0;
    ArrayList<oval> oval = new ArrayList<oval>();
    ArrayList<oval> tempoval = new ArrayList<oval>();
    ArrayList<line> line = new ArrayList<line>();
    ArrayList<line> templine = new ArrayList<line>();
    ArrayList<rect> rectangle = new ArrayList<rect>();
    ArrayList<rect> temprectangle = new ArrayList<rect>();
    ArrayList<JButton> sidebar2button = new ArrayList<JButton>();
    int ovalcounter = 0, linecounter = 0;
    ArrayList<String> ovalsidebar = new ArrayList<String>();
    ArrayList<String> linesidebar = new ArrayList<String>();
    ArrayList<String> rectanglesidebar = new ArrayList<String>();
    ArrayList<oval> tempdragoval=new ArrayList<oval>();
    ArrayList<line> tempdragline=new ArrayList<line>();
    ArrayList<rect> tempdragrect=new ArrayList<rect>();
    StringBuilder filebuilder;
    //functions menu starts
    JMenuBar menu;
    JMenu file,about;
    JMenuItem New,open,save, delete,Details,addfeedback,exit;
    FileWriter fw = null;
    static boolean fileopen = false;
    static String f = null;
    JFileChooser fileChooser,openchooser;

    public void filebulder() {
        
    }
    
    public void setMenubarVariables() {
        menu = new JMenuBar();
        file = new JMenu("File");
        New=new JMenuItem("New");
        open=new JMenuItem("Open");
        delete = new JMenuItem("Delete");
        save = new JMenuItem("Save");
        about=new JMenu("about");
        Details=new JMenuItem("Deatails");
        addfeedback=new JMenuItem("addfeedback");
        exit=new JMenuItem("exit");
    }
   
                
    public void setMenubar() {
        file.add(New);
        file.add(open);
        file.add(save);
        file.add(delete);
        
        
        Action saveAction = new AbstractAction("Save") {
             @Override
            public void actionPerformed(ActionEvent e) {
                
                if (!fileopen) {
                    fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Specify a file to save");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("html files", "html"));
                    int userSelection = fileChooser.showSaveDialog(p);
                    
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        //File fileToSave = fileChooser.getSelectedFile();
                        //System.out.println("Save as file: " + fileToSave.getAbsolutePath());

                        try {
                            f = fileChooser.getSelectedFile().toString();
                            System.out.println(f + "endwith=" + f.endsWith(".html"));
                            boolean bt = f.endsWith(".html");
                            if (bt) {
                                File fg=fileChooser.getSelectedFile();
                                if(!fg.exists())
                                {
                                        fw = new FileWriter(fileChooser.getSelectedFile(), false);
                                        StringBuffer ss=new StringBuffer();
                                        filebuilder=new StringBuilder();
                                        filebuilder.append("<html>");
                                        filebuilder.append("<head>");
                                        filebuilder.append("<body>");
                                        filebuilder.append("<svg width='1440' height='972' >");
                                        for (oval o : oval) {
                                            ss.append("oval\n");
                                            ss.append(o.getX());
                                            ss.append("\n");
                                            ss.append(o.getY());
                                            ss.append("\n");
                                            ss.append(o.getZ());
                                            ss.append("\n");
                                            ss.append(o.getK());
                                            ss.append("\n");
                                            filebuilder.append("<ellipse cx='").append(((o.getX()*2)+o.getZ())/2).append("' cy='").append(((o.getY()*2)+o.getK())/2 + 20).append("' rx='").append(o.getZ()/2).append("' ry= '").append(o.getK()/2).append("' style='fill:none;stroke:black;stroke-width:2'/>");

                                        }
                                        for (line l : line) {
                                            ss.append("line\n");
                                            ss.append(l.getX());
                                            ss.append("\n");
                                            ss.append(l.getY());
                                            ss.append("\n");
                                            ss.append(l.getZ());
                                            ss.append("\n");
                                            ss.append(l.getK());
                                            ss.append("\n");
                                            filebuilder.append("<line x1='").append(l.getX()).append("' y1='").append(l.getY() + 20).append("' x2='").append(l.getZ()).append("' y2='").append(l.getK() + 20).append("' style='stroke:rgb(0,0,0);stroke-width:2' />");                                    
                                        }
                                        for (rect l : rectangle) {
                                            ss.append("rectangle\n");
                                            ss.append(l.getX());
                                            ss.append("\n");
                                            ss.append(l.getY());
                                            ss.append("\n");
                                            ss.append(l.getZ());
                                            ss.append("\n");
                                            ss.append(l.getK());
                                            ss.append("\n");
                                            filebuilder.append("<rect x='").append(l.getX()).append("' y='").append(l.getY() + 20).append("' width='").append(l.getZ()).append("' height='").append(l.getK()).append("' style='fill:none;stroke:black;stroke-width:2' />");                                    
                                        }
                                        filebuilder.append("</svg>");
                                        filebuilder.append("</body>");
                                        filebuilder.append("</head>");
                                        filebuilder.append("</body>");
                                        fw.write(filebuilder.toString());
                                        fw.flush();
                                        fileopen = true;
                                        savetxt(ss);
                                        
                                }
                                else
                                {
                                   JOptionPane.showMessageDialog(p, "File already exists Error saving please choose another name","Dialog",JOptionPane.INFORMATION_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(p, "File must be saved with .html extension", "Dialog", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    }
                } else {
                    System.out.println("else");
                    try {
                        fw = new FileWriter(fileChooser.getSelectedFile(), false);
                        StringBuilder filebuilder2 = new StringBuilder();
                        StringBuffer ss=new StringBuffer();
                        filebuilder2.append("<html>");
                        filebuilder2.append("<head>");
                        filebuilder2.append("<body>");
                        filebuilder2.append("<svg width='1440' height='972'>");
                        for (oval o : oval) {
                            ss.append("oval\n");
                            ss.append(o.getX());
                            ss.append("\n");
                            ss.append(o.getY());
                            ss.append("\n");
                            ss.append(o.getZ());
                            ss.append("\n");
                            ss.append(o.getK());
                            ss.append("\n");
                            //filebuilder2.append("<circle cx='").append(o.getX()+screenSize.width/9).append("' cy='").append(o.getY()+20).append("' r='").append((o.getZ()+o.getK())/2).append("' style='fill:none;stroke:black;stroke-width:2' />");
                            //filebuilder2.append("<ellipse cx='").append((o.getX()*2)+o.getZ()).append("' cy='").append((o.getY()*2)+o.getK() + 20).append("' rx='").append(o.getZ()).append("' ry= '").append(o.getK()).append("' style='fill:none;stroke:black;stroke-width:2'/>");
                            filebuilder2.append("<ellipse cx='").append(((o.getX()*2)+o.getZ())/2).append("' cy='").append(((o.getY()*2)+o.getK())/2 + 20).append("' rx='").append(o.getZ()/2).append("' ry= '").append(o.getK()/2).append("' style='fill:none;stroke:black;stroke-width:2'/>");
                        }
                        for (line l : line) {
                            ss.append("line\n");
                            ss.append(l.getX());
                            ss.append("\n");
                            ss.append(l.getY());
                            ss.append("\n");
                            ss.append(l.getZ());
                            ss.append("\n");
                            ss.append(l.getK());
                            ss.append("\n");
                            filebuilder2.append("<line x1='").append(l.getX()).append("' y1='").append(l.getY() + 20).append("' x2='").append(l.getZ()).append("' y2='").append(l.getK() + 20).append("' style='stroke:rgb(0,0,0);stroke-width:2' />");
                        }
                        for (rect l : rectangle) 
                        {
                            ss.append("rectangle\n");
                            ss.append(l.getX());
                            ss.append("\n");
                            ss.append(l.getY());
                            ss.append("\n");
                            ss.append(l.getZ());
                            ss.append("\n");
                            ss.append(l.getK());
                            ss.append("\n");
                            filebuilder2.append("<rect x='").append(l.getX()).append("' y='").append(l.getY() + 20).append("' width='").append(l.getZ()).append("' height='").append(l.getK()).append("' style='fill:none;stroke:black;stroke-width:2' />");                                    
                        }
                        filebuilder2.append("</svg>");
                        filebuilder2.append("</body>");
                        filebuilder2.append("</head>");
                        filebuilder2.append("</body>");
                        System.out.println(filebuilder2.toString());
                        fw.write(filebuilder2.toString());
                        fw.flush();
                        savetxt(ss);
                    } catch (IOException ex) {
                        Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        };
 
saveAction.putValue(Action.ACCELERATOR_KEY,
        KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
save.setAction(saveAction);

KeyStroke keyStrokeToOpen
    = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
open.setAccelerator(keyStrokeToOpen);
KeyStroke keyStrokeToOpen2
    = KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);
delete.setAccelerator(keyStrokeToOpen2);

KeyStroke keyStrokeToOpen22
    = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
exit.setAccelerator(keyStrokeToOpen22);
exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
file.add(exit);
KeyStroke keyStrokeToOpen3
    = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
New.setAccelerator(keyStrokeToOpen3);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!tempoval.isEmpty()) {
                    oval o = tempoval.get(0);
                    int i = o.getIndex() - 1;
                    oval.remove(i);
                    tempoval.clear();
                    ovalsidebar.remove(i);
                    setsidebar2();
                } else if (!templine.isEmpty()) {
                    line l = templine.get(0);
                    int i1 = l.getIndex() - 1;
                    line.remove(i1);
                    templine.clear();
                    linesidebar.remove(i1);
                    setsidebar2();
                }
                else if (!temprectangle.isEmpty()) {
                    rect l = temprectangle.get(0);
                    int i1 = l.getIndex() - 1;
                    rectangle.remove(i1);
                    temprectangle.clear();
                    rectanglesidebar.remove(i1);
                    setsidebar2();
                }
                drawingarea.repaint();
            }
        });
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                fileChooser=new JFileChooser();
                fileChooser.setDialogTitle("select a file to open");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("html files", "html"));
                    int userSelection = fileChooser.showOpenDialog(p);
                    if (userSelection == JFileChooser.APPROVE_OPTION) 
                    {
                        f=fileChooser.getSelectedFile().toString();
                        String path=f.substring(0,f.length()-5);
                        System.out.println("path="+path);
                        boolean bb=f.endsWith(".html");
                        if(!bb)
                        {
                            JOptionPane.showMessageDialog(p, "not a valid html file","Dialaog",JOptionPane.INFORMATION_MESSAGE);
                        }
                        else if(new File(path+".txt").exists())
                        {
                        try 
                        {
                        BufferedReader bf=new BufferedReader(new FileReader(path+".txt"));
                        String text=bf.readLine();
                            System.out.println(text);
                        while(text!=null)
                        {
                            if(text.equalsIgnoreCase("oval"))
                            {
                                System.out.println("inside oval");
                                oval o=new oval();
                                o.setX(Integer.parseInt(bf.readLine()));
                                o.setY(Integer.parseInt(bf.readLine()));
                                o.setZ(Integer.parseInt(bf.readLine()));
                                o.setK(Integer.parseInt(bf.readLine()));
                                ovalsidebar.add("oval");
                                oval.add(o);
                            }
                            else if(text.equalsIgnoreCase("line"))
                            {
                                //System.out.println("inside oval");
                                line o=new line();
                                o.setX(Integer.parseInt(bf.readLine()));
                                o.setY(Integer.parseInt(bf.readLine()));
                                o.setZ(Integer.parseInt(bf.readLine()));
                                o.setK(Integer.parseInt(bf.readLine()));
                                linesidebar.add("line");
                                line.add(o);
                            }
                            else if(text.equalsIgnoreCase("rectangle"))
                            {
                                //System.out.println("inside oval");
                                rect o=new rect();
                                o.setX(Integer.parseInt(bf.readLine()));
                                o.setY(Integer.parseInt(bf.readLine()));
                                o.setZ(Integer.parseInt(bf.readLine()));
                                o.setK(Integer.parseInt(bf.readLine()));
                                rectanglesidebar.add("line");
                                rectangle.add(o);
                            }
                            text=bf.readLine();
                            
                        }
                            fileopen=true;
                            setsidebar2();
                            p.setTitle(f);
                            p.validate();
                            p.repaint();
                            drawingarea.repaint();
                        } 
                        catch (Exception ex) 
                        {
                         ex.printStackTrace();
                        } 
                        }
                        else{
                            JOptionPane.showMessageDialog(p, "File does not exisits","Dialog",JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                    }
                

            }
        });
        New.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tempbutton.clear();
                oval.clear();
                tempoval.clear();
                line.clear();
                templine.clear();
                sidebar2button.clear();
                ovalsidebar.clear();
                linesidebar.clear();
                temprectangle.clear();
                rectangle.clear();
                rectanglesidebar.clear();
                f="";
                try 
                {
                    if(fileopen)
                    {
                    fw.close();
                    }
                } 
                catch (IOException ex) {
                    Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
                }
                fileopen=false;
                sidebar2.removeAll();
                sidebar2.repaint();
                p.setTitle(f);
                p.validate();
                p.repaint();
            }
        });
        Details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(p, "Created by Msc(It) Students","About us",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        addfeedback.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username=JOptionPane.showInputDialog(p, "enter your name", "Add Feedback",JOptionPane.QUESTION_MESSAGE);
                String feedbacktext=null;
                if(username==null)
                {
                    System.out.println("dialog closed by user");
                }
                else if(username.isEmpty())
                {
                    JOptionPane.showMessageDialog(p, "username is empty");
                }
                else{
                    JTextArea feedback=new JTextArea("enter feedback");
                    feedback.addFocusListener(new FocusListener() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            feedback.setText("");
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            
                        }
                    });
                    feedback.setRows(5);
                    feedback.setColumns(5);
                    int i=JOptionPane.showConfirmDialog(p, feedback, "add feedback", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                     if(i==0)
                     {
                         feedbacktext=feedback.getText();
                         if(!feedbacktext.isEmpty())
                          {
                                    try 
                                    {
                                        Class.forName("com.mysql.jdbc.Driver");
                                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/paint", "root", "admin");
                                        PreparedStatement pp=(PreparedStatement) con.prepareStatement("insert into feedback values(?,?)");
                                        pp.setString(1, username);
                                        pp.setString(2, feedbacktext);
                                        pp.executeUpdate();
                                        JOptionPane.showMessageDialog(p , "feedback added successfully");
                                    } 
                                    catch (Exception ex) {
                                        JOptionPane.showMessageDialog(p, "error adding feedback in database");
                                    } 
                           }
                           else
                           {
                             JOptionPane.showMessageDialog(p, "feedback is empty");
                           }
                     }
                  }
                 
            }
        });
        about.add(addfeedback);
        about.add(Details);
        menu.add(file);
        menu.add(about);
        
        this.setJMenuBar(menu);
    }
    //ends

    public void savetxt(StringBuffer s)
    {
        String path=f.substring(0,f.length()-5);
        System.out.println(path);
        FileWriter ff;
        try {
            ff = new FileWriter(path+".txt",false);
            ff.write(s.toString());
            ff.flush();
        } catch (IOException ex) {
            Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        p.setTitle(f);
        p.validate();
        p.repaint();
    }
    public void clear()
    {
                tempbutton.clear();
                oval.clear();
                tempoval.clear();
                line.clear();
                templine.clear();
                sidebar2button.clear();
                ovalsidebar.clear();
                linesidebar.clear();
                rectangle.clear();
                temprectangle.clear();
                rectanglesidebar.clear();
                f="";
                try 
                {
                    if(fileopen)
                    {
                    fw.close();
                    }
                } 
                catch (IOException ex) {
                    Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
                }
                fileopen=false;
                sidebar2.removeAll();
                sidebar2.repaint();
    }
    //function sidebar starts
    JPanel sidebar, firstrow,secondrow;
    JButton circle, line1,rect;
    imageresize r = new imageresize();
    ImageIcon i = new ImageIcon(this.getClass().getResource("circle.png"));
    ImageIcon circlei = r.get(i);
    ImageIcon i2 = new ImageIcon(this.getClass().getResource("line.png"));
    ImageIcon linei = r.get(i2);
    ImageIcon i3 = new ImageIcon(this.getClass().getResource("rectangle.png"));
    ImageIcon recti = r.get(i3);

//Icon icon = IconFontSwing.buildIcon(FontAwesome.FLOPPY_O, 15);
    public void setSidebarVariables() {
        sidebar = new JPanel();
        firstrow = new JPanel(new GridLayout(1, 2));
        secondrow=new JPanel(new GridLayout(1,2));
        circle = new JButton(circlei);
        line1 = new JButton(linei);
        rect=new JButton(recti);
    }
    
    public void setSidebar() {
        sidebar.setSize(screenSize.width / 10, screenSize.height);
        sidebar.setBackground(Color.GRAY);
        firstrow.setSize(screenSize.width / 10, screenSize.height / 90);
        circle.setBackground(Color.white);
        circle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ovalclick == 0) {
                    if (!attached) {
                        attach();
                        attached = true;
                        cleartemp();
                    }
                    circle.setBackground(Color.LIGHT_GRAY);
                    ovalclick = 1;
                    lineclick = 0;
                    rectclick=0;
                    line1.setBackground(Color.WHITE);
                    rect.setBackground(Color.WHITE);
                } else {
                    if (attached) {
                        dettach();
                        attached = false;
                    }
                    circle.setBackground(Color.white);
                    ovalclick = 0;
                }
            }
        });
        firstrow.add(circle);
        line1.setBackground(Color.white);
        line1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lineclick == 0) {
                    if (!attached) {
                        attach();
                        attached = true;
                        cleartemp();
                    }
                    line1.setBackground(Color.LIGHT_GRAY);
                    lineclick = 1;
                    ovalclick = 0;
                    rectclick=0;
                    circle.setBackground(Color.WHITE);
                    rect.setBackground(Color.WHITE);
                } else {
                    if (attached) {
                        dettach();
                        attached = false;
                    }
                    line1.setBackground(Color.white);
                    lineclick = 0;
                }
            }
        });
        firstrow.add(line1);
        secondrow.setSize(screenSize.width / 10, screenSize.height / 90);
        rect.setBackground(Color.white);
        rect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rectclick == 0) {
                    if (!attached) {
                        attach();
                        attached = true;
                        cleartemp();
                    }
                    rect.setBackground(Color.LIGHT_GRAY);
                    rectclick = 1;
                    ovalclick = 0;
                    lineclick = 0;
                    circle.setBackground(Color.WHITE);
                    line1.setBackground(Color.WHITE);
                } else {
                    if (attached) {
                        dettach();
                        attached = false;
                    }
                    rect.setBackground(Color.white);
                    rectclick = 0;
                }
            }
        });
        secondrow.add(rect);
        sidebar.add(firstrow);
        sidebar.add(secondrow);
        this.add(sidebar);
    }
    //end
public void cleartemp()
{
    templine.clear();
    tempoval.clear();
    temprectangle.clear();
    drawingarea.repaint();
}
    //drawing area functions start
    static JPanel drawingarea, sidebar2;
    int x = 40, y = 40, z = 100, k = 100;
    
    public void setDrawingareaVariables() {
        drawingarea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                //g.drawOval(x, y, z, k);
                //g.setColor(Color.red);
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
               
                //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//                g2d.setColor(Color.GREEN);
                //g2d.drawOval(10,10,100,100);//I like fill :P

                for (oval p : oval) {
                    g2d.drawOval(p.getX(), p.getY(), p.getZ(), p.getK());
                    
                }
                for (line l : line) {
                    g2d.drawLine(l.getX(), l.getY(), l.getZ(), l.getK());
                }
                for (rect l : rectangle) {
                    g2d.drawRect(l.getX(), l.getY(), l.getZ(), l.getK());
                }
                for (oval p : tempoval) {
                    g2d.setColor(Color.CYAN);
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawOval(p.getX(), p.getY(), p.getZ(), p.getK());
                }
                for (line l : templine) {
                    g2d.setColor(Color.CYAN);
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawLine(l.getX(), l.getY(), l.getZ(), l.getK());
                    
                }
                for (rect l : temprectangle) {
                    g2d.setColor(Color.CYAN);
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawRect(l.getX(), l.getY(), l.getZ(), l.getK());
                    
                }
                for(oval o:tempdragoval)
                {
                    g2d.drawOval(o.getX(), o.getY(), o.getZ(), o.getK());
                }
                for(line o:tempdragline)
                {
                    g2d.drawLine(o.getX(), o.getY(), o.getZ(), o.getK());
                }
                for(rect o:tempdragrect)
                {
                    g2d.drawRect(o.getX(), o.getY(), o.getZ(), o.getK());
                }
       
            }
            
        };
        sidebar2 = new JPanel();
        //sidebar2.add(new JLabel("hello"));

    }
    
    public void setDrawingarea() {
        int width = screenSize.width - (screenSize.width / 6);
        drawingarea.setBackground(Color.white);
        drawingarea.setBounds(screenSize.width / 9, 20, screenSize.width / 2 + screenSize.width / 4, screenSize.height - (screenSize.height / 10));
        this.add(drawingarea);
        sidebar2.setBackground(Color.white);
        sidebar2.setBounds((screenSize.width / 2 + screenSize.width / 4 + screenSize.width / 9) + 20, 0, screenSize.width / 9, screenSize.height);
        sidebar2.setBackground(Color.GRAY);
        sidebar2.setLayout(new GridLayout(0, 1));
        //JScrollPane js=new JScrollPane(sidebar2);
        System.out.println(drawingarea.getWidth() + "" + drawingarea.getHeight());
        this.add(sidebar2);
    }
    boolean attached = false;
    public void attach() {
        drawingarea.addMouseListener(this);
        attachdrag();
    }
    public void attachdrag()
    {
        drawingarea.addMouseMotionListener(this);
    }
    public void dettachdrag()
    {
        drawingarea.removeMouseMotionListener(this);
    }
    public void dettach() {
        drawingarea.removeMouseListener(this);
        dettachdrag();
    }
    //end
    static Paint p;
    
    public static void main(String[] args) {
        p = new Paint();
        p.filebulder();
        p.setMenubarVariables();
        p.setMenubar();
        p.setSidebarVariables();
        p.setSidebar();
        p.setDrawingareaVariables();
        p.setDrawingarea();
        p.setSize(screenSize.width, screenSize.height);
        p.setLayout(new BorderLayout());
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.setVisible(true);
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        x = 0;
        y = 0;
        z = 0;
        k = 0;
        x = e.getX();
        y = e.getY();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        tempdragoval.clear();
        tempdragline.clear();
        tempdragrect.clear();
        if (ovalclick == 1) {
            z = e.getX() - x;
            k = e.getY() - y;
            oval o = new oval();
            o.setX(x);
            o.setY(y);
            o.setZ(z);
            o.setK(k);
            oval.add(o);
            ovalsidebar.add("oval");
            System.out.println("added oval x="+o.getX()+"y="+o.getY()+"z="+o.getZ()+"k="+o.getK());
        } else if (lineclick == 1) {
            z = e.getX();
            k = e.getY();
            line o = new line();
            o.setX(x);
            o.setY(y);
            o.setZ(z);
            o.setK(k);
            line.add(o);
            linesidebar.add("line");
        }
        else if (rectclick == 1) {
            z = e.getX()-x;
            k = e.getY()-y;
            rect o = new rect();
            o.setX(x);
            o.setY(y);
            o.setZ(z);
            o.setK(k);
            rectangle.add(o);
            rectanglesidebar.add("line");
        }
        //sidebar2.revalidate();
        setsidebar2();
        drawingarea.repaint();
        
    }
    
    public void setsidebar2() {
        sidebar2.removeAll();
        System.out.println(ovalsidebar.size());
        for (int i = 1; i <= ovalsidebar.size(); i++) {
            JButton j = new JButton("oval" + i);
            j.addActionListener(this);
            j.setBackground(Color.white);
            sidebar2.add(j);
        }
        for (int i = 1; i <= linesidebar.size(); i++) {
            JButton j = new JButton("line" + i);
            j.addActionListener(this);
            j.setBackground(Color.white);
            sidebar2.add(j);
        }
        for (int i = 1; i <= rectanglesidebar.size(); i++) {
            JButton j = new JButton("rectangle" + i);
            j.addActionListener(this);
            j.setBackground(Color.white);
            sidebar2.add(j);
        }
        sidebar2.revalidate();
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!tempbutton.isEmpty()) {
            JButton j1 = tempbutton.get(0);
            j1.setBackground(Color.white);
            tempbutton.clear();
        }
        JButton j = (JButton) e.getSource();
        j.setBackground(Color.LIGHT_GRAY);
        tempbutton.add(j);
        String text = j.getText();
        String obj = ""; //text.substring(0, text.length() - 1);
        String i2 = ""; //Integer.parseInt(text.substring(text.length() - 1));
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(text);
        while (m.find()) {
            i2 += m.group();
        }
        int i = Integer.parseInt(i2);
        Pattern p2 = Pattern.compile("[a-z]");
        Matcher m2 = p2.matcher(text);
        System.out.println(text);
        while (m2.find()) {
            obj += m2.group();
        }
        
        System.out.println(obj);
        if (obj.equalsIgnoreCase("oval")) {
            tempoval.clear();
            templine.clear();
            temprectangle.clear();
            clearsidebar();
            attachdrag();
            System.out.println("oval no=" + i);
            oval o = oval.get(i - 1);
            oval o1 = new oval();
            o1.setX(o.getX());
            o1.setY(o.getY());
            o1.setZ(o.getZ());
            o1.setK(o.getK());
            o1.setIndex(i);
            tempoval.add(o1);
        } else if (obj.equalsIgnoreCase("line")) {
            templine.clear();
            tempoval.clear();
            temprectangle.clear();
            clearsidebar();
            attachdrag();
            line o = line.get(i - 1);
            line o1 = new line();
            o1.setX(o.getX());
            o1.setY(o.getY());
            o1.setZ(o.getZ());
            o1.setK(o.getK());
            o1.setIndex(i);
            templine.add(o1);
        }
        else if (obj.equalsIgnoreCase("rectangle")) {
            templine.clear();
            tempoval.clear();
            temprectangle.clear();
            clearsidebar();
            attachdrag();
            rect o = rectangle.get(i - 1);
            rect o1 = new rect();
            o1.setX(o.getX());
            o1.setY(o.getY());
            o1.setZ(o.getZ());
            o1.setK(o.getK());
            o1.setIndex(i);
            temprectangle.add(o1);
        }
        drawingarea.repaint();
    }
   public void clearsidebar()
   {
            ovalclick=0;
            lineclick=0;
            rectclick=0;
            attached=false;
            circle.setBackground(Color.WHITE);
            line1.setBackground(Color.WHITE);
            rect.setBackground(Color.WHITE);
            dettach();
   }
    @Override
    public void mouseDragged(MouseEvent e) {
        if(!tempoval.isEmpty())
        {
            oval o=tempoval.get(0);
            int index=o.getIndex()-1;
            oval o1=oval.get(index);
            oval.remove(index);
            o1.setX(e.getX());
            o1.setY(e.getY());
            o1.setIndex(o.getIndex());
            oval.add(index, o1);
            tempoval.clear();
            tempoval.add(o1);
            drawingarea.repaint();    
        }
        else if(!templine.isEmpty())
        {
            line o=templine.get(0);
            int index=o.getIndex()-1;
            line o1=line.get(index);
            line.remove(index);
            o1.setX(e.getX());
            o1.setY(e.getY());
            o1.setIndex(o.getIndex());
            line.add(index, o1);
            templine.clear();
            templine.add(o1);
            drawingarea.repaint();
        }
        else if(!temprectangle.isEmpty())
        {
            rect o=temprectangle.get(0);
            int index=o.getIndex()-1;
            rect o1=rectangle.get(index);
            rectangle.remove(index);
            o1.setX(e.getX());
            o1.setY(e.getY());
            o1.setIndex(o.getIndex());
            rectangle.add(index, o1);
            temprectangle.clear();
            temprectangle.add(o1);
            drawingarea.repaint();
        }
        else if(ovalclick==1)
        {
            tempdragoval.clear();
            z=e.getX()-x;
            k=e.getY()-y;
            oval o=new oval();
            o.setX(x);
            o.setY(y);
            o.setZ(z);
            o.setK(k);
            tempdragoval.add(o);
            drawingarea.repaint();
        }
        else if(lineclick==1)
        {
            tempdragoval.clear();
            tempdragline.clear();
            tempdragrect.clear();
            z=e.getX();
            k=e.getY();
            line o=new line();
            o.setX(x);
            o.setY(y);
            o.setZ(z);
            o.setK(k);
            tempdragline.add(o);
            drawingarea.repaint();
        }
        else if(rectclick==1)
        {
            tempdragoval.clear();
            tempdragline.clear();
            tempdragrect.clear();
            z=e.getX()-x;
            k=e.getY()-y;
            rect o=new rect();
            o.setX(x);
            o.setY(y);
            o.setZ(z);
            o.setK(k);
            tempdragrect.add(o);
            drawingarea.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
    
}

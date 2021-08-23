

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;


class Peer implements ActionListener{

    int port;
    DatagramSocket ds;
	JFrame f;
	JTextField tf1,tf2,tf3;
	JLabel l1,l2,l3;
	JPanel p1,p2,p3,answerPanel;
	JButton send,Enter;
	JTextArea ta;
	int destPort=0;
	String str="",ans="";
    Peer () {

		f=new JFrame("Peer to Peer");
		f.setSize(500,500);
		f.setLayout(new GridLayout(1,1));
		ta=new JTextArea(15,40);
		l1=new JLabel("Enter port for this Peer");
		l1.setBounds(50,100,150,30);
		Enter = new JButton("ENTER");
		Enter.setBounds(180,180,100,30);
		Enter.addActionListener(this);
		l2=new JLabel("Enter message for another Peer");
		l2.setBounds(50,50,200,30);
		l3=new JLabel("Enter destination Port");
		l3.setBounds(50,120,150,30);
		tf1=new JTextField();
		tf1.setBounds(210,100,200,30);
		tf2=new JTextField();
		tf2.setBounds(260,50,200,30);
		tf3=new JTextField();
		tf3.setBounds(260,120,200,30);
		send=new JButton("Send");
		send.setBounds(215,180,70,30);
		send.addActionListener(this);
		answerPanel=new JPanel();
		answerPanel.setLayout(new GridLayout(2,1));
		p1=new JPanel();
		p1.setLayout(null);
		p2=new JPanel();
		p2.setLayout(null);
		p3=new JPanel();
		p3.setLayout(new GridLayout(1,1));
		JScrollPane js = new JScrollPane(ta,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		p3.add(js);
		p1.add(l1);
		p1.add(tf1);
		p1.add(Enter);
		p2.add(l2);
		p2.add(tf2);
		p2.add(l3);
		p2.add(tf3);
		p2.add(send);
		answerPanel.add(p2);
		answerPanel.add(p3);
		f.add(p1);
		f.setVisible(true);
		ta.setEditable(false);
		f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		
    }
	

    class Receiver extends Thread {

        int port;
        

        Receiver(int port) {

            this.port = port;
        }

        public void run() {

        try {
                System.out.println("Client connected");

                while (true)
               {					
                   byte[] b = new byte[1024];
	       DatagramPacket dp = new DatagramPacket(b, b.length);
                   ds.receive(dp);
                   String str = new String(dp.getData());
                   ans="Message from " + dp.getPort() + " is " + str.trim();

                    if (str.trim().equalsIgnoreCase("bye")) 
                  {
                        ta.setText(ta.getText()+"\n"+ Integer.toString(dp.getPort()) + " left");
                        this.run();
                        break;
                    }
                    ta.setText(ta.getText()+"\n"+ans);
                }
                
            }
            catch (Exception e)
           {
                System.out.println(e);
            }
        }
    }
    public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == Enter)
		{			
			port =Integer.parseInt(tf1.getText());
		            f.remove(p1);
			f.setContentPane(answerPanel);
			f.validate();
			f.repaint();
			try {
				ds = new DatagramSocket(port);
				System.out.println("Receiver created!!!");
				JOptionPane.showMessageDialog(f, "Peer created", "Info", JOptionPane.INFORMATION_MESSAGE);
				f.setTitle(Integer.toString(port));
				Receiver r = new Receiver(this.port);
				r.start();
			} 
                                    catch (Exception ex)
                                   {
                                       //TODO: handle exception
			}
		}
		if(e.getSource()==send)
		{
			try {
				str=tf2.getText();
				destPort=Integer.parseInt(tf3.getText());
				byte b[] = new byte[1024];
				b = str.getBytes();
				DatagramPacket dp1 = new DatagramPacket(b, b.length, InetAddress.getByName("127.0.0.1"), destPort);
				ds.send(dp1);				
				JOptionPane.showMessageDialog(f, "Message sent", "Info", JOptionPane.INFORMATION_MESSAGE);
				
				if (str.equalsIgnoreCase("bye")) 
                                               {
		                          System.exit(0);
				}
			}
                                    catch (Exception E)
                                    {
							
			}			
		}
	}
   public static void main(String[] args) throws Exception
 {
        Peer p = new Peer();
  }
}


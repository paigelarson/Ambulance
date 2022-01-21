

import java.io.File;
import java.lang.Number;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.util.Random;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.TextArea;
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;


public class myAmbulanceFrame extends Applet
{

// private instance variables

private JFrame frame;
private JPanel panel;
private JLabel JL_NumAmbulances;
private int numAmbulances;
private JLabel JL_NumAmbulancesOut;
private int numAmbulancesOut;
private JLabel JL_NumAmbulancesIn;
private int numAmbulancesIn;
private JLabel JL_NumCallsWaiting;
private int numCallsWaiting;
private JLabel JL_AmbulancesOut;
private JComboBox JC_AmbulancesOut;
private JButton JB_CallReceived;
private JButton JB_DispatchAmbulance;
private TextArea JT_Output;
private JScrollPane JS_Output;
private GridBagLayout layout;
private int PANEL_WIDTH, PANEL_HEIGHT;
private ArrayList <String> outputMsg = new ArrayList();
private int numRows = 20;
private String strNumAmbulancesIn, strNumAmbulancesOut, strNumAmbulances;
public CircularArrayQueue ambulancesIn;
private ArrayList <String> ambulancesOut = new ArrayList();
private boolean JC_AmbulancesOutShouldReact;
private DefaultComboBoxModel cbmodel;
private BufferedImage img;

// constructor
public myAmbulanceFrame()
{
strNumAmbulances = JOptionPane.showInputDialog("How many ambulances are there?");
numAmbulances = Integer.parseInt(strNumAmbulances);
ambulancesIn = new CircularArrayQueue(numAmbulances);
PANEL_WIDTH = 800;
PANEL_HEIGHT = 600;


numCallsWaiting = 0;
numAmbulancesOut = 0;
JC_AmbulancesOutShouldReact = true;
cbmodel = new DefaultComboBoxModel();

setUpPanel();

createAmbulances();

// set up initial values for simulator
JL_NumAmbulances.setText("Ambulances: " + numAmbulances);
JL_NumAmbulancesOut.setText("Ambulances Out: " + ambulancesOut.size());
JL_NumAmbulancesIn.setText("Ambulances In: " + ambulancesIn.size());
JL_NumCallsWaiting.setText("911 Calls Waiting: 0");
JB_CallReceived.setText("911 Call Received");
JB_DispatchAmbulance.setText("Dispatch Ambulance");
}

public void init()
{
}

public void createAmbulances()
{
int j;
String s;
for(j=0; j<numAmbulances; j++)
{
    int num=j+1;
    ambulancesIn.enqueue("Ambulance "+num);    
}

}

public void setUpPanel()
{
// create main panel
frame = new JFrame();
panel = new JPanel();
panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
frame.getContentPane().add(panel,BorderLayout.CENTER);
frame.setTitle("Metuchen Emergency Services");

JL_NumAmbulances = new JLabel();
JL_NumAmbulancesOut = new JLabel();
JL_NumAmbulancesIn = new JLabel();
JL_NumCallsWaiting = new JLabel();
JL_AmbulancesOut = new JLabel("Returning Ambulance:");
JC_AmbulancesOut = new JComboBox(cbmodel);
JC_AmbulancesOut.setEditable(false);
JB_CallReceived = new JButton();
JB_DispatchAmbulance = new JButton();
JT_Output = new TextArea(" ", 20,50,1);

Container contentPane = frame.getContentPane();
layout = new GridBagLayout();
contentPane.setLayout(layout);
GridBagConstraints constraints = new GridBagConstraints();

constraints.weightx = 100;
constraints.weighty = 100;

// column 1
add(JL_NumAmbulances, constraints,    0, 0, 1, 1);
add(JL_NumAmbulancesOut, constraints, 0, 1, 1, 1);
add(JL_NumAmbulancesIn, constraints,  0, 2, 1, 1);
add(JL_NumCallsWaiting, constraints,  0, 3, 1, 1);

// column 2
constraints.anchor = GridBagConstraints.SOUTH;
add(JL_AmbulancesOut, constraints,      1, 0, 1, 1);
constraints.anchor = GridBagConstraints.NORTH;
add(JC_AmbulancesOut, constraints,      1, 1, 1, 1);
constraints.anchor = GridBagConstraints.CENTER;
add(JB_DispatchAmbulance, constraints,  1, 2, 1, 1);
add(JB_CallReceived, constraints,       1, 3, 1, 1);
add(JT_Output, constraints,  2, 1, 1, 4);

// construct & add action listeners
// an action listener is assigned here to buttons and a combo box,
// and the listener runs when the button is clicked
ActionListener outAmbulanceReturnsListener = new outAmbulanceReturns();
JC_AmbulancesOut.addActionListener(outAmbulanceReturnsListener);

ActionListener dispatchAmbulanceListener = new dispatchAmbulance();
JB_DispatchAmbulance.addActionListener(dispatchAmbulanceListener);

ActionListener Call911ReceivedListener = new Call911Received();
JB_CallReceived.addActionListener(Call911ReceivedListener);

refreshGUI();
frame.pack();
frame.show();

}

private String getTime()
{
int minutes, hour, am_pm;
String AM_PM;
String strMinutes;
Calendar c = Calendar.getInstance();	
minutes = c.get(Calendar.MINUTE);
hour = c.get(Calendar.HOUR);
am_pm = c.get(Calendar.AM_PM);
if (am_pm == 0)
	AM_PM = " a.m.";
else
	AM_PM = " p.m.";
strMinutes = String.valueOf(minutes);
if (minutes < 10)
	strMinutes = "0" + strMinutes;
String time = String.valueOf(hour) + ":" + strMinutes;
time += AM_PM;
return time;
}

public void paint(Graphics g)
{
Graphics2D g2 = (Graphics2D)g;
}

public String updateOutput()
{
while (outputMsg.size() > numRows)
{	outputMsg.remove(0);
}
String fff = "";
for (int k = 0; k < outputMsg.size(); k++)
{	fff += outputMsg.get(k);	
}
return fff;	
}

// update the GUI with new information
private void refreshGUI()
{
JB_CallReceived.setText("911 Call Received");
JB_DispatchAmbulance.setText("Dispatch Ambulance");
JL_NumAmbulances.setText("Ambulances: " + numAmbulances);
JL_NumAmbulancesIn.setText("Ambulances In: " + ambulancesIn.size());
JL_NumAmbulancesOut.setText("Ambulances Out: " + ambulancesOut.size());
JL_NumCallsWaiting.setText("911 Calls Waiting: " + numCallsWaiting);
String msg = updateOutput();
JT_Output.setText(msg);
repaint();
}

// respond to ambulance returning
private class outAmbulanceReturns implements ActionListener
{
	// this method lays out what should happen when an ambulance that was out returns
	public void actionPerformed(ActionEvent event)
	{
		if(JC_AmbulancesOutShouldReact)
		{
			if (ambulancesOut.size() > 0)
			{
                            String ambulanceout=(String)cbmodel.getSelectedItem();
                            outputMsg.add(ambulanceout+" returned at " + getTime()+"\n");
                            cbmodel.removeElement(ambulanceout);
                            ambulancesOut.remove(ambulanceout);
                            JC_AmbulancesOut.removeAllItems();
                            cbmodel.removeAllElements();
                            for(int i=0; i<ambulancesOut.size();i++)
                            {   String a=ambulancesOut.get(i);
                                cbmodel.addElement(a);
                            }
                                JC_AmbulancesOut.setModel(cbmodel);
                                ambulancesIn.enqueue(ambulanceout);
                                refreshGUI();
                                
                            
			}	
		}
	}
}

// respond to click of dispatch ambulance button
private class dispatchAmbulance implements ActionListener
{
	// this method lays out what should happen if the dispatch ambulance button is clicked
	public void actionPerformed(ActionEvent event)
	{	
		// TO DO:
		// write an IF statement that runs if there is at least 1 ambulance in ambulancesIn
		// and there is at least 1 911 call waiting for an ambulance
		if(ambulancesIn.isEmpty()==false && numCallsWaiting>0 )
		{	
			       
                    String asd= (String)ambulancesIn.dequeue();
                    boolean JC_AmbulancesShouldReact;
                    JC_AmbulancesShouldReact=false;
                    cbmodel.addElement(asd);
                    ambulancesOut.add(asd);
                    JC_AmbulancesOut.setModel(cbmodel);
                    JC_AmbulancesShouldReact=true;
                    numCallsWaiting--;
                    outputMsg.add(asd+" was dispatched at "+ getTime()+"\n");
                    refreshGUI();
                    
                    
		}
		                else if(ambulancesIn.isEmpty()==true)
                {
                    JOptionPane.showMessageDialog(panel, "I'm sorry, we have no ambulances available");
                }
			                else if(numCallsWaiting==0)
                {
                    JOptionPane.showMessageDialog(panel, "There are no 911 calls coming in right now");
                }
				
	}
}

// respond to click of 911 call received button
private class Call911Received implements ActionListener
{
	// this method lays out what should happen if a 911 call is received
	public void actionPerformed(ActionEvent event)
	{	
            numCallsWaiting++;
            outputMsg.add("A 911 call was recieved at "+ getTime()+"\n");
            refreshGUI();
	}
}

public void add(Component c, GridBagConstraints constraints, int x, int y, int w, int h)
{
	constraints.gridx = x;
	constraints.gridy = y;
	constraints.gridwidth = w;
	constraints.gridheight = h;	
	frame.getContentPane().add(c, constraints);
}
	
}
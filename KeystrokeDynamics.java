import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

class KeystrokeDynamics
{

    private static String testPassword="";
    private static int testTimings[]=new int[15];
    
    private static String password="";
    private static String temptimings="";
    private static int timings[][]=new int[6][15];
    
    private static JButton button;
    private static JPanel panel;
    private static JPasswordField field1;
    private static JPasswordField field2;
    private static JPasswordField field3;
    //private static JTextArea area;
    static int counter=0;
    
    private String encode(String word1,String word2)
    {
        String encoded="";int index=0;char ch;
        for(int i=0;i<word1.length();i++)
        {
            index=index>=word2.length()?0:index;
            ch=(char)(word1.charAt(i)^word2.charAt(index));
            encoded+=ch;
            index++;
        }
        return encoded;
    }    
    public void Write(String fname,String encoder)
    {
        try
        {
            String s;
            FileWriter fw=new FileWriter(fname);
            PrintWriter pw=new PrintWriter(fw);
            pw.println(encode(password,encoder));
            for(int i=0;i<6;i++)
            {
                for(int j=0;j<15;j++)
                {

                    pw.println(encode(String.valueOf(timings[i][j]),encoder));

                }

            }
            pw.close();
            fw.close();
        }
        catch(Exception e)
        {
        
        }
    }
    public void Read(String fname,String encoder)
    {
        try
        {
            FileReader fr=new FileReader(fname);
            BufferedReader br=new BufferedReader(fr);
            password=encode(br.readLine(),encoder);
            for(int i=0;i<6;i++)
            {
                for(int j=0;j<15;j++)
                {
                    timings[i][j]=Integer.parseInt(encode(br.readLine(),encoder));

                }
            }
            br.close();
            fr.close();
        }
        catch(Exception e)
        {
        
        }        
    }
    public void createNewPassword()
    {
        field1=new JPasswordField(20);
        field2=new JPasswordField(20);
        button=new JButton("Ok");
        panel=new JPanel();
        //area=new JTextArea(6,5);
            for(int i=0;i<6;i++)
            {
                for(int j=0;j<15;j++)
                {
                    timings[i][j]=0;
                }
               
            }  
        counter=0;
        //area.setText("");
        temptimings="";
        field1.setEnabled(true);
        field2.setEnabled(true);
        field1.setText("");
        field2.setText("");
        field2.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                temptimings=temptimings+" "+String.valueOf(System.currentTimeMillis());
            }
        });
        //JScrollPane scroller=new JScrollPane(area);
        panel.add(field2);
        panel.add(button);
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //if(area.getLineCount()<=6)
                if(counter<=6)
                {
                    if(field1.getText().compareTo(field2.getText())==0)
                    {
                        //area.append(field1.getText()+"\n");
                        counter=counter+1;
                        StringTokenizer st=new StringTokenizer(temptimings);
                        long s1=0,s2=0;
                        int index=0;
                        if(st.hasMoreTokens())
                            s1=Long.parseLong(st.nextToken());
                        while(st.hasMoreTokens())
                        {
                            s2=Long.parseLong(st.nextToken());
                            //timings[area.getLineCount()-1][index]=(int)(s2-s1);
                            timings[counter-1][index]=(int)(s2-s1);
                            s1=s2;
                            index=index+1;
                        }
                        index=0;
                        temptimings="";
                        field2.setText("");
                        field2.grabFocus();
                        //if(area.getLineCount()==6)
                        if(counter==6)
                        {
                            field1.setEnabled(false);
                            field2.setEnabled(false);
                            //area.setEnabled(false);
                        }
                    }
                    else
                    { 
                        temptimings="";
                        field2.setText("");
                        field2.grabFocus();
                    }
                }
            }
        });            
        Object message[]={"Input Password",field1,"Input same password six times",panel};//,scroller};
        String []options={"Ok","Cancel"};        
        int result=JOptionPane.showOptionDialog(null,message, "CreateNewPassword", 
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
        null, options, options[0]);
        if(result==0)
        {
            password=field1.getText();

        }
    }
    public void inputPassword()
    {
        field3=new JPasswordField(20);
        temptimings="";
        for(int i=0;i<15;i++)
        {
            testTimings[i]=0;
        }
        field3.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                temptimings=temptimings+" "+String.valueOf(System.currentTimeMillis());
            }
        });
        Object message[]={"Input Password",field3};
        String []options={"Ok","Cancel"};        
        int result=JOptionPane.showOptionDialog(null,message, "InputPassword", 
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
        null, options, options[0]);
        if(result==0)
        {
            testPassword=field3.getText();
            StringTokenizer st=new StringTokenizer(temptimings);
            long s1=0,s2=0;
            int index=0;
            if(st.hasMoreTokens())
                s1=Long.parseLong(st.nextToken());
            while(st.hasMoreTokens())
            {
                s2=Long.parseLong(st.nextToken());
                testTimings[index]=(int)(s2-s1);
                //timings[counter-1][index]=(int)(s2-s1);
                s1=s2;
                index=index+1;
            }            
        }        
    }
    public boolean isReliable()
    {
        int len=password.length();
        double mean=0.0,sd=0.0,cv=0.0;
        for(int j=0;j<len-1;j++)
        {
            for(int i=0;i<6;i++)
            {
                mean+=timings[i][j];
            }
            mean=mean/6.0;
            for(int i=0;i<6;i++)
            {
                sd+=(mean-timings[i][j])*(mean-timings[i][j]);
            }
            sd=Math.sqrt(sd/6.0);
            cv=(sd*100.0)/mean;
            //System.out.println(cv);
            if(cv>15)
            {   
                break;
            }
            mean=0;sd=0;cv=0;
        }
        return (cv<15);
    }
    public boolean isDispersionOk()
    {
        double mean=0.0,sd=0.0,cv=0.0;
        double cvs[]=new double[6];
        for(int i=0;i<6;i++)
        {
            for(int j=0;j<password.length()-1;j++)
            {
                mean+=timings[i][j];
            }
            mean=mean/(password.length()-1);
            for(int j=0;j<password.length()-1;j++)
            {
                sd+=(mean-timings[i][j])*(mean-timings[i][j]);
            }
            sd=Math.sqrt(sd/(password.length()-1));
            cv=(sd*100)/mean;
            cvs[i]=cv;
            //System.out.println(cv);
            mean=0;sd=0;cv=0;
        }
        for(int i=0;i<testPassword.length()-1;i++)
        {
            mean+=testTimings[i];
        }
        mean=mean/(testPassword.length()-1);
        for(int i=0;i<testPassword.length()-1;i++)
        {
            sd+=(mean-testTimings[i])*(mean-testTimings[i]);
        }
        sd=Math.sqrt(sd/(testPassword.length()-1));
        cv=(sd*100)/mean;
        
        double cvMax=cvs[0],cvMin=cvs[0];
        for(int i=0;i<6;i++)
        {
            //
                cv+=cvs[i];
            //
            if(cvs[i]>cvMax)
                cvMax=cvs[i];
            if(cvs[i]<cvMin)
                cvMin=cvs[i];
        }
        cv=cv/7;
        //System.out.println(cvMax+" "+cvMin+" "+cv);
        return ((cv>=cvMin)&&(cv<=cvMax));
    }
    public double coeffOfCorrelation()
    {
        double mean=0.0;
        double sigma_xy=0.0;
        double sigma_x=0.0;
        double sigma_y=0.0;
        double sigma_x_square=0.0;
        double sigma_y_square=0.0;
        double coeff_of_correlation=0.0;
        int len=testPassword.length();
        for(int j=0;j<len-1;j++)
        {
            for(int i=0;i<6;i++)
            {
                mean+=timings[i][j];
            }
            mean=mean/6;
            sigma_x+=mean;
            sigma_y+=testTimings[j];
            sigma_xy+=mean*testTimings[j];
            sigma_x_square+=mean*mean;
            sigma_y_square+=testTimings[j]*testTimings[j];
            mean=0;
        }
        coeff_of_correlation=(sigma_xy-((sigma_x*sigma_y)/(len-1)))/(Math.sqrt(sigma_x_square-((sigma_x*sigma_x)/(len-1)))*Math.sqrt(sigma_y_square-((sigma_y*sigma_y)/(len-1))));
        return coeff_of_correlation;
    }
	public boolean isPasswordOK()
	{
		return (testPassword.compareTo(password)==0);
	}
    public void display()
    {
        System.out.println("Password: "+password);
        
        System.out.println("\nTimings recorded(Password):\n"); 
        for(int i=0;i<6;i++)
        {
            for(int j=0;j<15;j++)
            {
                System.out.print(timings[i][j]+"\t");
            }
            System.out.println();
        }
        
        System.out.println("\nCoeff. of variation(Column wise):\n");
        int len=password.length();
        double mean=0.0,sd=0.0,cv=0.0;
        for(int j=0;j<len-1;j++)
        {
            for(int i=0;i<6;i++)
            {
                mean+=timings[i][j];
            }
            mean=mean/6;
            for(int i=0;i<6;i++)
            {
                sd+=(mean-timings[i][j])*(mean-timings[i][j]);
            }
            sd=Math.sqrt(sd/6);
            cv=(sd*100)/mean;
            System.out.println(cv);
            mean=0;sd=0;cv=0;
        }
        
        System.out.println("\nTestPassword: "+testPassword);
        System.out.println("\nTimings of TestPassword:\n");
        for(int i=0;i<testPassword.length()-1;i++)
        {
            System.out.print(testTimings[i]+"\t");
        }
        
        System.out.println("\nCoeff. of variation of password timings(row wise):\n");
        for(int i=0;i<6;i++)
        {
            for(int j=0;j<password.length()-1;j++)
            {
                mean+=timings[i][j];
            }
            mean=mean/password.length()-1;
            for(int j=0;j<password.length()-1;j++)
            {
                sd+=(mean-timings[i][j])*(mean-timings[i][j]);
            }
            sd=Math.sqrt(sd/password.length()-1);
            cv=(sd*100)/mean;
            System.out.println(cv);
            mean=0;sd=0;cv=0;
        }
        
        System.out.println("\nCoeff. of variation of TestPassword timings(row wise):\n");
        for(int i=0;i<testPassword.length()-1;i++)
        {
            mean+=testTimings[i];
        }
        mean=mean/testPassword.length()-1;
        for(int i=0;i<testPassword.length()-1;i++)
        {
            sd+=(mean-testTimings[i])*(mean-testTimings[i]);
        }
        sd=Math.sqrt(sd/testPassword.length()-1);
        cv=(sd*100)/mean;
        System.out.println(cv);
        
        System.out.println("Data reliable:"+isReliable());
        System.out.println("Is dispersion ok:"+isDispersionOk());
        System.out.println("Coeff. of correlation:"+coeffOfCorrelation());     
    }

    public void graph()
    {
        JFrame frame1=new JFrame("KD Analyser");   
        JPanel p1=new drawPanel(timings,password.length());
        frame1.getContentPane().add(p1);

        JFrame frame2=new JFrame("KD Analyser");   
        JPanel p2=new drawPanel2(timings,password.length(),testTimings);
        frame2.getContentPane().add(p2);    
        

        frame1.pack();
		frame2.pack();
        final int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame1.setBounds ( 10,15,709,535);
        frame1.setVisible(true);
		frame2.setBounds(10,15,709,535);
		frame2.setVisible(true);
    
    }
}

class drawPanel extends JPanel
{
    int t[][]=new int[6][15];
    int len;
    drawPanel(int t1[][],int l)
    {
        for(int i=0;i<6;i++)
        {
            for(int j=0;j<15;j++)
            {
                t[i][j]=t1[i][j];
            }     
        }
        len=l;
    }
    public void paintComponent(Graphics g)
    {
        int x=700;
        int y=500;
        int a=x/(len);
        int b=2*a;
        int temp=0;
        Color c[]={Color.red,Color.blue,Color.green,Color.yellow,Color.black,Color.white};
        super.paintComponent(g);
        
        g.fillRect(0,0,700,500);


        g.setColor(Color.white);
        
        for(int i=0;i<len;i++)
        g.drawLine(i*a,0,i*a,500);
        
        g.setColor(Color.white);
        g.drawLine(0,100,700,100);
        g.drawLine(0,200,700,200);
        g.drawLine(0,300,700,300);
        g.drawLine(0,400,700,400);

        g.drawString("0 mille sec.",10,500-2);
        g.drawString("100 mille sec.",10,400-2);
        g.drawString("200 mille sec.",10,300-2);
        g.drawString("300 mille sec.",10,200-2);
        g.drawString("400 mille sec.",10,100-2);
        for(int i=0;i<6;i++)
        {   
            a=x/len;
            b=2*a;
            temp=b;
            g.setColor(c[i]);
            for(int j=0;j<len-2;j++)
            {
                g.drawLine(a,y-t[i][j],b,y-t[i][j+1]);
                
                a+=(x/len);
                b+=(x/len);
            }
        }
        g.setColor(Color.red);
        g.drawString("x-axis:Time in mille sec",510,15);
        g.drawString("           1 unit=100 mille sec",510,30);
    }
}
    
class drawPanel2 extends JPanel
{
    int t[][]=new int[6][15];
	int tt[]=new int[15];
	int avg[]=new int[15];
    int len;
	int mean;
    drawPanel2(int t1[][],int l,int t2[])
    {
        for(int i=0;i<6;i++)
        {
            for(int j=0;j<15;j++)
            {
                t[i][j]=t1[i][j];
            }     
        }
        len=l;
		for(int j=0;j<len-1;j++)
		{
			mean=0;
			for(int i=0;i<6;i++)
			{
				mean+=t[i][j];
			}
			mean=mean/6;
			avg[j]=mean;
		}
		for(int i=0;i<15;i++)
		{
			tt[i]=t2[i];
		} 
    }
    public void paintComponent(Graphics g)
    {
        int x=700;
        int y=500;
        int a=x/(len);
        int b=2*a;
        int temp=0;
        Color c[]={Color.red,Color.blue,Color.green,Color.yellow,Color.black,Color.white};
        super.paintComponent(g);
        
        g.fillRect(0,0,700,500);


        g.setColor(Color.white);
        
        for(int i=0;i<len;i++)
        g.drawLine(i*a,0,i*a,500);
        
        g.setColor(Color.white);
        g.drawLine(0,100,700,100);
        g.drawLine(0,200,700,200);
        g.drawLine(0,300,700,300);
        g.drawLine(0,400,700,400);

        g.drawString("0 mille sec.",10,500-2);
        g.drawString("100 mille sec.",10,400-2);
        g.drawString("200 mille sec.",10,300-2);
        g.drawString("300 mille sec.",10,200-2);
        g.drawString("400 mille sec.",10,100-2);
        
            a=x/len;
            b=2*a;
            temp=b;
           
            for(int j=0;j<len-2;j++)
            {
				g.setColor(Color.red);
                g.drawLine(a,y-avg[j],b,y-avg[j+1]);
				g.setColor(Color.green);
                g.drawLine(a,y-tt[j],b,y-tt[j+1]);
                a+=(x/len);
                b+=(x/len);
            }
        
        g.setColor(Color.red);
        g.drawString("x-axis:Time in mille sec",510,15);
        g.drawString("           1 unit=100 mille sec",510,30);
    }
}


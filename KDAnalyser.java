import java.io.*;
class KDAnalyser
{
    public static void main(String []args)throws Exception
    {
        KeystrokeDynamics kd=new KeystrokeDynamics();
        int choice;
        InputStreamReader ir=new InputStreamReader(System.in);
        BufferedReader br=new BufferedReader(ir);        
        while(true)
        {
            System.out.println("\nKeystroke Dynamics Analyser\n");
            System.out.println("1.Read from file");
            System.out.println("2.Create New Password");
            System.out.println("3.Write to file");
            System.out.println("4.Is reliable");
            System.out.println("5.Input TestPassword");
            System.out.println("6.Is Dispersion Ok");
            System.out.println("7.Coeff. of correlation");
            System.out.println("8.Display");
            System.out.println("9.Display graph");
 	    System.out.println("10.Exit");

            choice=Integer.parseInt(br.readLine());
            if(choice==1)
            {
                String text1,text2;
                System.out.println("Input file path to read from.");
                text1=br.readLine();
                kd.Read(text1,"ankur");
            }
            else if(choice==2)
            {
                kd.createNewPassword();
            }
            else if(choice==3)
            {
                String text1,text2;
                System.out.println("Input file path to write to.");
                text1=br.readLine();
                kd.Write(text1,"ankur");
            }
            else if(choice==4)
            {
                System.out.println(kd.isReliable());
            }
            else if(choice==5)
            {
                kd.inputPassword();
            }
            else if(choice==6)
            {
                System.out.println(kd.isDispersionOk());
            }
            else if(choice==7)
            {
                System.out.println(kd.coeffOfCorrelation());
            }
            else if(choice==8)
            {
                kd.display();
            }
            else if(choice==9)
            {
                kd.graph();
            }
	    else if(choice==10)
	    {	
		System.exit(0);
	    }
        
        
        }
   }
}

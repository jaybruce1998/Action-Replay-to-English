import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;

public class ActionReplayToEnglish
{
	public static void main(String[] arg) throws FileNotFoundException
	{
	    Scanner scanner=new Scanner(new File(arg[0]));
	    String line=scanner.nextLine(), tabs="", x;
	    boolean e=false;
	    int n=0, a=0;
	    while(line!=null)
	    {
	    	line=line.trim().toUpperCase();//System.out.print(line+": ");
	    	if(line.length()==0)
	    	{
	    		line=scanner.hasNextLine()?scanner.nextLine():null;
	    		System.out.println();
	    		continue;
	    	}
	    	x=line.substring(1, 8).equals("0000000")?"offset":line.substring(1, 8);
	    	if(e)
	    	{
	    		System.out.println(tabs+"memory["+Integer.toHexString(a)+"+offset]="+line.substring(0, 8));
	    		a+=4;
	    		n-=4;
	    		if(n>3)
	    		{
	    			System.out.println(tabs+"memory["+Integer.toHexString(a)+"+offset]="+line.substring(9));
	    			a+=4;
	    			n-=4;
	    		}
	    		if(n==0)
	    		{
	    			e=false;
	    			System.out.println();
	    		}
	    	}
	    	else if(line.startsWith("0"))
	    		System.out.println(tabs+"memory["+line.substring(1, 8)+"+offset]="+line.substring(9));
	    	else if(line.startsWith("1"))
	    		System.out.println(tabs+"memory["+line.substring(1, 8)+"+offset]="+line.substring(13));
	    	else if(line.startsWith("2"))
	    		System.out.println(tabs+"memory["+line.substring(1, 8)+"+offset]="+line.substring(15));
	    	else if(line.startsWith("3"))
	    	{
	    		System.out.println(tabs+"if(memory["+x+"]<"+line.substring(9)+")\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("4"))
	    	{
	    		System.out.println(tabs+"if(memory["+x+"]>"+line.substring(9)+")\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("5"))
	    	{
	    		System.out.println(tabs+"if(memory["+x+"]=="+line.substring(9)+")\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("6"))
	    	{
	    		System.out.println(tabs+"if(memory["+x+"]!="+line.substring(9)+")\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("7"))
	    	{
	    		System.out.println(tabs+"if(memory["+x+"]&(!"+line.substring(9, 13)+")<"+line.substring(13)+")\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("8"))
	    	{
	    		System.out.println(tabs+"if(memory["+x+"]&(!"+line.substring(9, 13)+")>"+line.substring(13)+")\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("9"))
	    	{
	    		System.out.println(tabs+"if(memory["+x+"]&(!"+line.substring(9, 13)+")=="+line.substring(13)+")\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("A"))
	    	{
	    		System.out.println(tabs+"if(memory["+x+"]&(!"+line.substring(9, 13)+")!="+line.substring(13)+")\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("B"))
	    		System.out.println(tabs+"offset=memory["+line.substring(1, 8)+"+offset]");
	    	else if(line.startsWith("C"))
	    	{
	    		System.out.println(tabs+"for(int i=0; i<"+line.substring(9)+"; i++)\n"+tabs+"{");
	    		tabs+="\t";
	    	}
	    	else if(line.startsWith("D0")||line.startsWith("D1"))
	    	{
	    		if(tabs.length()>0)
	    		{
		    		tabs=tabs.substring(1);
		    		System.out.println(tabs+"}");
		    	}
		    	else
		    		throw new RuntimeException("\nError at line: "+line+"\nNo more ifs/loops (did you already put a D2?)");
	    	}
	    	else if(line.startsWith("D2"))
	    		while(tabs.length()>0)
	    		{
		    		tabs=tabs.substring(1);
		    		System.out.println(tabs+"}");
	    		}
	    	else if(line.startsWith("D3"))
	    		System.out.println(tabs+"offset="+line.substring(9));
	    	else if(line.startsWith("D4"))
	    		System.out.println(tabs+"stored+="+line.substring(9));
	    	else if(line.startsWith("D5"))
	    		System.out.println(tabs+"stored="+line.substring(9));
	    	else if(line.startsWith("D6"))
	    		System.out.println(tabs+"memory["+line.substring(9)+"+offset]=stored\n"+tabs+"offset+=4");
	    	else if(line.startsWith("D7"))
	    		System.out.println(tabs+"memory["+line.substring(9)+"+offset]=stored&FFFF\n"+tabs+"offset+=2");
	    	else if(line.startsWith("D8"))
	    		System.out.println(tabs+"memory["+line.substring(9)+"+offset]=stored&FF\n"+tabs+"offset++");
	    	else if(line.startsWith("D9"))
	    		System.out.println(tabs+"stored=memory["+line.substring(9)+"+offset]");
	    	else if(line.startsWith("DA"))
	    		System.out.println(tabs+"stored=memory["+line.substring(9)+"+offset]&FFFF");
	    	else if(line.startsWith("DB"))
	    		System.out.println(tabs+"stored=memory["+line.substring(9)+"+offset]&FF");
	    	else if(line.startsWith("DC"))
	    		System.out.println(tabs+"offset+="+line.substring(9));
	    	else if(line.startsWith("E"))
	    	{
	    		a=Integer.parseInt(line.substring(1, 8), 16);
	    		n=Integer.parseInt(line.substring(9), 16);
	    		e=true;
	    		System.out.println("\n"+tabs+"//direct memory writes");
	    	}
	    	else if(line.startsWith("F"))
	    		System.out.println("\n"+tabs+"//memory copy\n"+tabs+"address="+line.substring(1, 8)+"\n"+tabs+"off=offset\n"
	    			+tabs+"\nfor(int i=0; i<address; i++)\n{\n\t"
	    			+tabs+"memory[address]=off\n"+tabs+"address++\n"+tabs+"off++");
	    	else
	    		throw new RuntimeException(line);
	    	line=scanner.hasNextLine()?scanner.nextLine():null;
	    }
	}
}
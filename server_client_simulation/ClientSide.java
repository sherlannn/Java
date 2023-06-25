import java.io.*;
import java.net.*;

//Ehsan Mokhtari - 955367025 - 2019-1-4

class ClientSide{
	//the main class
	public static void main(String args[]) throws Exception
	{
		//matching the client and server input/output ports = 20
		Socket mySocket=new Socket("127.0.0.1",20);
		ClientTF clientTF=new ClientTF(mySocket);
		clientTF.show();}}
//client transfor file class that handels user in/out put
class ClientTF{
	Socket cSocket;
    DataInputStream dataInputStream;
	DataOutputStream dataOutputStream;
	BufferedReader bufferReader;
	ClientTF(Socket mySocket)
	{   try{
			cSocket = mySocket;
			dataInputStream=new DataInputStream(cSocket.getInputStream());
			dataOutputStream=new DataOutputStream(cSocket.getOutputStream());
			bufferReader=new BufferedReader(new InputStreamReader(System.in));}
		catch(Exception ex){}		}
		//a function that handels directry changes in server side
	void cDirectory() throws Exception
	{		String directory;
			String b;
			System.out.print("Do you want to go to back directory? #yes or no#");
			b=bufferReader.readLine();
		    if (b.equals("yes")) 
			 {		dataOutputStream.writeUTF("backDir");
					System.out.print("... you moved to back directory ..."); }
			else if(b.equals("no"))
			{        System.out.print("enter the directory path that you want to move in ...");
					 directory=bufferReader.readLine();
					 dataOutputStream.writeUTF(directory);
					 String changeD=dataInputStream.readUTF();
					 if(changeD.equals("directoryChange"))
				{    System.out.print("... Directory changed ...");}
					 else if(changeD.equals("directoryCreate"))
					 {System.out.print("... Directory created ...");}}	}
	void fileReceiver() throws Exception {
		String fn;
		System.out.print("enter your file name ... \n");
		fn=bufferReader.readLine();
		dataOutputStream.writeUTF(fn);
		String rmfs=dataInputStream.readUTF();
	    if(rmfs.compareTo("The file doesnt exists probably ...")==0)
		{ System.out.println("The file doesnt exists probably ...");
			return;}
		else if(rmfs.compareTo("... ready ...")==0)
		{ System.out.println("... ready ...\n receiving ...");
			File a=new File(fn);
			if(a.exists())
			{   String o;
				System.out.println("you have this file ... download anyway? yes or no");
				o=bufferReader.readLine();			
				if(o=="no")	{//flushing the datastream
					         dataOutputStream.flush();
					         return;	}				}
			FileOutputStream fileOutputStream=new FileOutputStream(a);
			int x;
			String y;
			do{ y=dataInputStream.readUTF();
				x=Integer.parseInt(y);
				if(!(x==-1))
				{fileOutputStream.write(x);	}
			}while(!(x==-1));
			fileOutputStream.close();
			System.out.println(dataInputStream.readUTF());}}
    void List() throws Exception
	{   String op;
		String list=dataInputStream.readUTF();
		System.out.println("your directory includes ... \n");
		System.out.println(list);
		while(1==1){
		System.out.println("continue yes or no ?");
		op=bufferReader.readLine();
		if(op.equals("yes")){return;} 
		else{/*loop again...*/}}}
    public void show() throws Exception
	{while(1==1)	{	
			System.out.println("... options ... \n ");
			System.out.println("1 for changing directory");
			System.out.println("2 for get");
			System.out.println("3 for list");
			System.out.println("4 for disconnect");
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
		    String finalc = bufferReader.readLine();
		    if(finalc.equals("1")){
		    dataOutputStream.writeUTF("changing directory...\n");
		    cDirectory();}
			else if(finalc.equals("2"))
			{ dataOutputStream.writeUTF("getting the file...\n");
				fileReceiver();}
			else if(finalc.equals("3"))
			{ dataOutputStream.writeUTF("listing ... \n");
				List();}
			else if(finalc.equals("4"))
			{   dataOutputStream.writeUTF("disconnecting ... \n");
				this.cSocket.close();
				System.exit(1);}
			else
			{System.out.println("please enter correct input");}}}}
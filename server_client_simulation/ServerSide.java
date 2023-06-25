import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Author: Eshan Mokhtari - 955367025 - 2019-1-3

//main class
public class ServerSide{
	public static void main(String args[]) throws Exception
	{
		//initializing to port 20
		ServerSocket mySocket = new ServerSocket(20);
		System.out.println("the 20th port of server is enabled");
		boolean a = true;
		while(a)
		{
			System.out.println("... waiting ...");
			FileTransfer fileTransfer = new FileTransfer(mySocket.accept());
			}}}
			
			
//file transform class
class FileTransfer extends Thread
{
	Socket clSoc;
	DataInputStream dataInputStream;
	DataOutputStream dataOutStream;
	File currentDirectory = new File(".");
	String directory = currentDirectory.getAbsolutePath();
	String cd;
	//file transfer instructure function with one argument
	FileTransfer(Socket mySocket)
	{try	{
			clSoc=mySocket;						
			dataInputStream =new DataInputStream(clSoc.getInputStream());
			dataOutStream =new DataOutputStream(clSoc.getOutputStream());
			System.out.println("The client is connected successfuly ...");
			//starting the thread
			start();
			  cd= directory.substring(0, directory.length() - currentDirectory.getCanonicalPath().length());
		}
		catch(Exception ex){}		}
		
		//public void
	void send() throws Exception
	{		
		String fn=dataInputStream.readUTF();
		//File newF=new File(fn);
		File newF=new File(cd+fn);
		System.out.println(newF);
		if(!(newF.exists()))
		{
			dataOutStream.writeUTF("The file doesnt exists probably ...");
			return;}
		else
		{dataOutStream.writeUTF("... ready ...");
			FileInputStream fileInputs = new FileInputStream(newF);
			int x;
			//an do while loop for operation 
			do
			{			x=fileInputs.read();
				dataOutStream.writeUTF(String.valueOf(x));}
			while(x!=-1);
//closes the input stream if file ends			
			fileInputs.close();	
			dataOutStream.writeUTF("... the file download was successfuly ... ");	}}
	
	//change directory public void
	void CD() throws Exception
	{String readedInput = dataInputStream.readUTF();
		if(readedInput.equals("backDir")) {
            Path bDirectory = Paths.get(cd);
			//getting parent dir = back dir
			bDirectory = bDirectory.getParent();
			cd=bDirectory.toString();}
		else {
                    //using / for the directory pathes
		String directory=readedInput+"/";
		Path bDirectory = Paths.get(directory);
	    if(Files.exists(bDirectory))
		{		cd=cd+directory;
			    dataOutStream.writeUTF("directoryChange");}
		else
		{Files.createDirectory(bDirectory);
			cd=cd+directory;
			dataOutStream.writeUTF("directoryCreate");}}}
	 void LIST() throws IOException {
				File directory = new File(cd);
			String[] files = directory.list();
			String fl="";
			if (files.length == 0) {
			    System.out.println("... empty one ...");
			} else {for(String afile :files) {
					  fl=fl+afile+"\n";}}
				System.out.println(fl);
			    dataOutStream.writeUTF(fl);}

	public void run()
	{
		while(true)
		{
			try{
			System.out.println("... please enter your task command ...");
			String taskCommand = dataInputStream.readUTF();
			if(taskCommand.compareTo("2")==0)
			{System.out.println("... done ...");
				send();
				continue;}
			else if(taskCommand.compareTo("1")==0)
			{   System.out.println("... done ...");				
				CD();
				continue;}
			else if(taskCommand.compareTo("3")==0)
			{   System.out.println("... done ...");				
				LIST();
				continue;	}
			else if(taskCommand.compareTo("DISCONNECT")==0)
			{
				System.out.println("... done ...");
				this.clSoc.close();
				System.exit(1);}}
			catch(Exception ex){}          }}}
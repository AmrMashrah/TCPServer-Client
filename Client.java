import java.io.*; 
import java.net.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner; 

public class Client 
{ 
	final static int ServerPort = 6651; 
	public static void main(String args[]) throws UnknownHostException, IOException 
	{

				// setting ip address of clients
				InetAddress ip = InetAddress.getByName("localhost"); 
				Socket s = new Socket(ip, ServerPort);
				// setting data streams to send and recieve data
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 			
				boolean login = false;
				Scanner scanner = new Scanner(System.in); 	
	
				while (true)
				{
				try {
					// creating menu for a user
				String Menu = "\n1 - LOG-IN\n2 - SOLVE\n3 - LIST\n4 - LOG-OUT\n5 - SHUTDOWN\nEnter Your Choice for Client  : ";	
				System.out.print(Menu);
				String choice = scanner.nextLine();
				String command = "";

				// user chooses for  log in 
				if (choice.equals("1"))
				{
					command ="LOGGIN";
					//alerting server with sending LOGIN to server to detect
					dos.writeUTF(command);
					// select ID and password and send to server to get log in results
					System.out.print("\nEnter ID = ");
					String ID = scanner.nextLine();
					dos.writeUTF(ID);
					System.out.print("\nEnter Password = ");
					String PASS = scanner.nextLine();
					dos.writeUTF(PASS);
					String responce = dis.readUTF(); 
					System.out.println(responce); 
					if (responce.equals("SUCCESS"))
					{
						//enabling another modes after login 
						login = true;
					}
				}
				else if (choice.equals("2"))
				{
					if (!login)
					{
						// checking if loged in or not
						System.out.print("\nPlease Log In First");					
						continue;
					}
					command ="SOLVE";					
					// alerting server to get ready for reecieving command
					dos.writeUTF(command);
					System.out.print("Sample Command = (SOLVE -c 3 OR SOLVE -r 2 5)\nEnter Your Command : ");					
					String ID = scanner.nextLine();										
					// sending command
					dos.writeUTF(ID);
					// server responce on that command
					String responce = dis.readUTF(); 
					System.out.println(responce); 
				}
				else if (choice.equals("3"))
				{
					// checkking if its already logged in or not
					if (!login)
					{
						System.out.print("\nPlease Log In First");					
						continue;
					}
					command ="LIST";
					dos.writeUTF(command);
					// sending LIST command to server to get ready for recieving server
					System.out.print("Command Sample = (LIST / LIST -all)\nEnter Your Commad = ");					
					String forward = scanner.nextLine();	
					dos.writeUTF(forward);
					// recieving responce from server
					String responce = dis.readUTF(); 
					System.out.println(responce); 
				}
				else if (choice.equals("4"))
				{
					// checking if already logged in or not
					if (!login)
					{
						System.out.print("\nPlease Log In First");					
						continue;
					}
					// sending logout command to suspend this client connection
					command ="LOGOUT";
					dos.writeUTF(command);
					String responce = dis.readUTF(); 
					System.out.println(responce); 
					s.close();
					break;
				}
				else if (choice.equals("5"))
				{
					// checkking if its already logged in or not
					if (!login)
					{
						System.out.print("\nPlease Log In First");					
						continue;
					}
					
					// sending shot down command to server to suspend server itslef
					command ="SHUTDOWN";
					dos.writeUTF(command);

					
					// recieving responce from server
					String responce = dis.readUTF(); 
					System.out.println(responce); 
					s.close();
					break;
				}
				else 
				{	command ="Invalid";
				dos.writeUTF(command);
					System.out.print("\nInvalid Command\n");
				}				

			}
				catch (IOException e) {
					System.out.print(e);					
				}
				
	}
		
	
	}
} 

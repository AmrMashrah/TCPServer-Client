import java.io.*; 
import java.util.*; 
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant; 


public class Server 
{ 
	static Instant start = Instant.now();

	static int i = 0; 
	public static void main(String[] args) throws IOException 
	{
		ArrayList<String>Commands = new ArrayList<String>();
		ArrayList<String>User = new ArrayList<String>();
		ArrayList<String>Answer = new ArrayList<String>();
		ArrayList<String>LogIn = new ArrayList<String>();
		ArrayList<String>Pass = new ArrayList<String>();
		 FileWriter myWriter = null;
	
		ReadLogIn(LogIn,Pass,"login.txt");
		// manipulating data to the server 

		int user = 0;
		String Name="";
		// creating server socket to accept client connections
		ServerSocket ss = new ServerSocket(6651); 
		Socket s; 
		boolean suspend = false;
		while (true)
		{
			System.out.println("\nWaiting For New Client "); 
			// accepting client connection
			s = ss.accept();
			user = user + 1;
			System.out.println("Client Connected");
			// setting data streams to send and recieve data
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
		
			while (true)
			{
				String command;  
					try
					{ 
						// reciving command form client
						command = dis.readUTF(); 						
						System.out.println("\nCommand Recieved : " + command); 

						if (command.equals("LOGGIN"))
						{
							// in LOG in checking the validity of the credential provided

							Name = dis.readUTF(); 						
							System.out.println("\nID Recieved : " + command); 
							String Password = dis.readUTF(); 						
							System.out.println("\nPassword Recieved : " + Password); 							
							boolean found = false;							
							for (int i=0;i<Pass.size();i++)
							{
								// sending alert back to client if logged in or not
								if (Name.equals(LogIn.get(i))  && Password.equals(Pass.get(i)))
								{
									dos.writeUTF("SUCCESS");
									found = true;
									myWriter = new FileWriter(Name+"-solutions.txt");
									break;
								}
							}							
							if (!found)
							{
								dos.writeUTF("LOGIN "+Name+" "+Password+"\nFAILED");
							}
						}
						else if (command.equals("SOLVE"))
						{
							
							// in SOLVE command detecting if its circle or Rectangle request and solve accordingly
							String ID = dis.readUTF(); 						
							System.out.println("\nCommand Recieved : " + ID); 
							
						     myWriter.write("\nClient  : " + ID);

						     User.add(Name);
						     Commands.add(ID);


						     // parsing command sent to detect necessary things
					        StringTokenizer st = new StringTokenizer(ID," ");  
					        int i = 0;
					        boolean circle = true;
					        int radius =0;
					        int len =0;
					        // traversing all data sent in command
					        while (st.hasMoreTokens()) { 
					        	if (i==1)
					        	{
					        		if (st.nextToken().equals("-c"))
					        		{
					        			
										System.out.println("\nWe Found Circle"); 
					        		}
					        		if (st.nextToken().equals("-r"))
					        		{
										System.out.println("\nWe Found RECTANGLE"); 
										circle=false;
					        		}
					        	}
					        	// calculating output of data once after separation detection
					        	if (i==2)
					        	{
					        		radius = Integer.parseInt(st.nextToken());
					        	}
					        	if (i==3)
					        	{
					        		len = Integer.parseInt(st.nextToken());
					        	}
					        	
					            i=i+1;
					        }  
					        i=i-1;
					        
					        // sending responces back to client after processing its SOLVE command
					        if (circle && i==2)
					        {				
					        	String reply = "\nServer : Circle's Circumference is "+Double.toString(2*3.141592*radius)+ " and area is "+Double.toString(3.141592*radius*radius);
								dos.writeUTF("Circle's Circumference is "+Double.toString(2*3.141592*radius)+ " and area is "+Double.toString(3.141592*radius*radius)); 
								myWriter.write(reply);
							    Answer.add(reply);
					        }
					        else if (!circle && i==2)
					        {					        	
					        	String reply = "\nServer : Rectangle's Circumference is "+Double.toString(4*radius)+ " and area is "+Double.toString(radius*radius);
								dos.writeUTF("Rectangle's Circumference is "+Double.toString(4*radius)+ " and area is "+Double.toString(radius*radius)); 	
								myWriter.write(reply);
							     Answer.add(reply);
					        }
					        else if (!circle && i==3)
					        {					        	
								dos.writeUTF("Rectangle's Circumference is "+Double.toString((2*radius)+(2*len) )+ " and area is "+Double.toString(radius*len)); 	
								String reply ="\nServer : Rectangle's Circumference is "+Double.toString((2*radius)+(2*len) )+ " and area is "+Double.toString(radius*len);
								myWriter.write(reply);
							     Answer.add(reply);
					        }
					        else if (i==1)
					        {					        	
								dos.writeUTF("Missing Values"); 	
								String reply = "\nServer : Missing Values";
								myWriter.write(reply);
							    Answer.add(reply);
					        }
					        else
					        {
					        	
								dos.writeUTF("Invalid Attempt"); 	

					        }
					        
						}
						else if (command.equals("LOGOUT"))
						{
							// suspending connected client connection
							dos.writeUTF("200 OK"); 	
							myWriter.write("\nServer : 200 OK");
							myWriter.close();							
							s.close();
							break;
						}
						else if (command.equals("SHUTDOWN"))
						{
							// suspending Server
							dos.writeUTF("200 OK"); 	
							myWriter.write("\nServer : 200 OK");
							myWriter.close();							
							s.close();
							suspend = true;
							break;
						}
						else if (command.equals("LIST"))
						{		
							// checking if LIST command recieved and processing to get details required to send back
							String forward = dis.readUTF(); 						
					        StringTokenizer st = new StringTokenizer(forward," ");
					        int k = 0;
					        String Title="";
					        String all=""; 
					        while (st.hasMoreTokens())
					        {	
					        	if (k==0)
					        	{
					        		Title=st.nextToken();
					        	}
					        	if (k==1)
					        	{
					        		all=st.nextToken();
					        	}
					        	k=k+1;
					        }
					        k=k-1;

							if (Title.equals("LIST"))
							{
								// after parsing LIST COMMAND recieved sending its data
								if (k==1 && all.equals("-all") && Name.equals("root"))
								{
									// if root requested the details then sending all commands and thier responces 
									String Data = "";
									String id = "";
									id = User.get(0);
									Data = Data + id+"\n";
									for (int i=0;i<User.size();i++)
									{
										if (id.equals(User.get(i)))
										{
											Data = Data + Commands.get(i);
											Data = Data + Answer.get(i)+"\n";
										}
										else
										{
											id = User.get(i);
											Data = Data+"\n"+id+"\n";
											Data = Data + Commands.get(i);
											Data = Data + Answer.get(i)+"\n";
										}
									}
									System.out.println("\nLISTED ALL DATA = \n"+Data);
									dos.writeUTF(Data); 	
								}
								else if (k==0)
								{
									/// if simple LIST command requested then collecting data to send them all
									String Data = "";
									Data = Data + Name+"\n";
									for (int i=0;i<User.size();i++)
									{
										if (Name.equals(User.get(i)))
										{
											Data = Data + Commands.get(i)+"\n";
											Data = Data + Answer.get(i)+"\n";
										}
									}
									System.out.println("\nLISTED DATA = \n"+Data);
									dos.writeUTF(Data); 	
								}
								else
								{
										dos.writeUTF("No Data to Show for you"); 	
								}
								
							}
							else
							{
								dos.writeUTF("Invalid Attempt"); 	
							}
						}
						else
						{
							
							dos.writeUTF("\nInvalid Command\n"); 	
						}
						
					} catch (IOException e) { 
						
						e.printStackTrace(); 
					}
	}
			if (suspend)
			{
				System.out.println("Suspending Server\nServer Shutdown");
				ss.close();
				break;
			}
	}
				} 

	// functions required to get logins from file
	static void ReadLogIn(ArrayList<String> LogIn,ArrayList<String> Pass, String filename) throws IOException
	{

		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        StringTokenizer st = new StringTokenizer(line," ");  
		        int i = 0;
		        while (st.hasMoreTokens()) { 
		        	if (i==0)
		        	{LogIn.add(st.nextToken());}
		        	else if (i==1)
		        	{
		        		Pass.add(st.nextToken());
		        	}
		        	
		            i=i+1;
		        }  
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		} finally {
		    br.close();
		}
		return;

	}
	}



import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client implements Runnable{
    
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean done;
    private boolean topicFlag = false;
    private ArrayList<MFile> fileBuffer = new ArrayList<>();

    @Override
    public void run(){
        try{
            client = new Socket("localhost", 5050);
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream()); 

            InputHandler inHandler = new InputHandler();
            Thread t = new Thread(inHandler);
            t.start();
            
            String inMessage;
            while((inMessage = (String)in.readObject()) != null){ //consumer part
                if(inMessage.equals("No video with such id!")){
                    System.out.println(inMessage);
                }else if(inMessage.startsWith("SFTS")){
                    String[] split = new String[3];
                    split = inMessage.split(":");
                    int chunks = Integer.parseInt(split[1]);
                    String filename = split[2];
                    int j = 0;
                    byte[] temp;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    while(j<chunks){
                        temp = (byte[]) in.readObject();
                        outputStream.write(temp);
                        j++;
                    }
                    byte[] result = outputStream.toByteArray();
                    File video = new File(filename);
                    FileOutputStream outStream = new FileOutputStream(video);
                    int m;
                    ByteArrayInputStream bis = new ByteArrayInputStream(result);
                    while((m = bis.read())!= -1){
                        outStream.write(m);
                    }
                    bis.close();
                    outStream.close();
                    outputStream.close();
                    System.out.println("File downloaded!");
                }else if(inMessage.startsWith("redirect ")){
                    String[] messageSplit = inMessage.split(" ", 3);
                    // String username = messageSplit[1];
                    int port = Integer.parseInt(messageSplit[2]);
                    client = new Socket("localhost", port);
                    out = new ObjectOutputStream(client.getOutputStream());
                    in = new ObjectInputStream(client.getInputStream()); 
                }else{
                    System.out.println(inMessage);
                }
            }
        } catch (IOException e){
            shutdown();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean getFlag(){
        return topicFlag;
    }

    public void addToBuffer(MFile file){
        fileBuffer.add(file);
    }

    public ArrayList<MFile> getBuffer(){
        return fileBuffer;
    }

    public void shutdown(){
        done = true;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        }catch(IOException e){
            // ignore
        }
    }

    class InputHandler implements Runnable{

        @Override
        public void run(){
            try{
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while(!done){ //publisher part
                    String message = inReader.readLine();
                    if(message.equals("/quit")){
                        out.writeObject(message);
                        inReader.close();
                        shutdown();
                    }else if(message.equals("/selectfile")){
                        FileInputStream inFile = null;
                        File old = null;
                        JFileChooser chooser = new JFileChooser();
                        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                            old = chooser.getSelectedFile();
                        }
                
                            inFile = new FileInputStream(old);
                
                            
                            int split_count;
                            if(old.length()%512000 == 0){
                                split_count = (int) old.length()/512000;
                            }
                            else{
                                split_count =  (int) old.length()/512000 + 1;
                            }
                            String a = String.valueOf(old);
                            a = a.replace("\\", "/");
                            String[] tempStrings = a.split("/");
                            String b = tempStrings[tempStrings.length - 1];
                            out.writeObject("SFTS:" + split_count +":" + b);
                            try{                                   
                                byte[] chunk_size = new byte[512000];            
                                int c;
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                                while ((c = inFile.read(chunk_size)) != -1){               
                                    buffer.write(chunk_size,0,c);
                                    out.writeObject(buffer.toByteArray());
                                    buffer.reset();    
                                }
                                inFile.close();
                                buffer.close();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                    }else{
                        out.writeObject(message);
                    }
                }
            }catch (IOException e){
                shutdown();
            }
        }
    }

    public static void main(String args[]){
        Client client = new Client();
        client.run();
    }

}
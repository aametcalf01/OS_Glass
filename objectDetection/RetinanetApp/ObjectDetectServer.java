import java.io.*;
import java.net.*;
import java.util.Arrays;

public class ObjectDetectServer{
    public static void main(String args[])throws IOException,InterruptedException{
        int portNum=8000;
        int portNum2=8001;
        int filesize = 6022386;
        int bytesRead;
        int current =0;
        String picturePath = "/Users/andymetcalf/Desktop/test.jpg";
        String picturePath2 = "/Users/andymetcalf/Documents/OS_glass/objectDetection/RetinanetApp/output.jpg"; //can switch this to foo.jpg

        byte [] mybytearray = new byte [filesize];

        while(true){
            try(
                ServerSocket serverSocket = new ServerSocket(portNum);
                Socket socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                FileOutputStream fos = new FileOutputStream(picturePath);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
            ){
                System.out.println("Im in!");
                bytesRead = is.read(mybytearray,0,mybytearray.length);
                System.out.println(mybytearray.length);
                System.out.println("is.read output: "+ Integer.toString(bytesRead));

                current = bytesRead;

                do {
                    bytesRead =
                       is.read(mybytearray, current, (mybytearray.length-current));
                       System.out.println("bytesRead: "+ Integer.toString(bytesRead));
                    if(bytesRead >= 0) current += bytesRead;
                 } while(bytesRead > -1);
    
                 System.out.println("File size: "+Integer.toString(current));
        
                 bos.write(mybytearray, 0 , current);
                 System.out.println("Wrote file to desktop - executing python script");
                 final long pythonStartTime = System.nanoTime();
                Process p = Runtime.getRuntime().exec("python /Users/andymetcalf/Documents/OS_glass/objectDetection/RetinanetApp/objectDetect.py --image /Users/andymetcalf/Desktop/test.jpg");
                //Process p = Runtime.getRuntime().exec("python /Users/andymetcalf/Documents/OS_glass/objectDetection/RetinanetApp/blemishDetect.py --image /Users/andymetcalf/Desktop/test.jpg");
                p.waitFor();
                final long pythonDuration = System.nanoTime() - pythonStartTime;
                System.out.println("Finished writing python script in "+ Long.toString(pythonDuration/1000000000)+" seconds." );
                break;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        //Code to send will go here
        File pictureFile = new File(picturePath2);
        byte [] mybytearray2 = new byte[(int)pictureFile.length()];
       
        ServerSocket serverSocket2 = new ServerSocket(portNum2);
        while(true){
            try{
                System.out.println("Waiting...");
                Socket socket2 = serverSocket2.accept();
                System.out.println("Accepted connection");
                FileInputStream fis = new FileInputStream(pictureFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream os = socket2.getOutputStream();

                bis.read(mybytearray2,0,mybytearray2.length);
                os.write(mybytearray2,0,mybytearray2.length);
                os.close();
                fis.close();
                bis.close();
                socket2.close();
                serverSocket2.close();
                
                break;
            }catch(IOException e){
                e.printStackTrace();
            }
        
    }
}}
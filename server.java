import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import javax.net.ServerSocketFactory;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class server {
    public static void main(String[] args){

        try{
            System.out.println("开始运行");
            ServerSocket serverSocket = new ServerSocket(9999);
            while(true){
                Socket socket = serverSocket.accept();
                new Thread(new Server_listen(socket)).start();
                new Thread(new Server_send(socket)).start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
class Server_listen implements Runnable{
    private  Socket socket;
    Server_listen(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try{
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while(true)
                System.out.println(ois.readObject());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
class Server_send implements Runnable{
    private  Socket socket;
    Server_send(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner =new Scanner(System.in);
            while (true){
                System.out.println("请输入：");
                String string = scanner.nextLine();
                JSONObject object = new JSONObject();
                object.put("type","chat");
                object.put("msg",string);
                oos.writeObject(object);
                oos.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
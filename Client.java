package src;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
public class Client {
    private static Socket socket;
    private static boolean connection_state = false;
    public static void main(String[] args){
        connect();
        if(connection_state){
            new Thread(new Client_listen(socket)).start();
            new Thread(new Client_send(socket)).start();
        }
    }
    private static void connect(){
        try {
            socket = new Socket("127.0.0.1",9999);
            connection_state = true;
        }catch (Exception e){
            e.printStackTrace();
            connection_state = false;
        }

    }
}
class Client_listen implements Runnable{
    private Socket socket;
    Client_listen(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try{
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while(true){
                System.out.println(ois.readObject());

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class Client_send implements Runnable{
    private Socket socket;
    Client_send(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.print("请输入发送信息：");
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

package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException, IOException {
        if(args.length <= 1) {
            System.out.println("Usage: java -cp classes client.Main <server> <port>");
            System.exit(0);
        }

        // setup connection
        String host = args[0];
        Integer port = Integer.parseInt(args[1]);
        
        Socket socket = new Socket(host, port);
        NetworkIO netIO = new NetworkIO(socket);

        // instantiate calculator
        MathFn mf = new MathFn();

        // receive list of numbers
        String serverMsg = netIO.read();
        System.out.println("Received list of numbers: " + serverMsg);

        // send details
        netIO.write("Yeow Li Teng, Cheryl");
        netIO.write("cheryl.ylt@gmail.com");
        System.out.println("Name and email sent to server.");
        
        // calculate mean and stdev
        Double mean = mf.mean(serverMsg);
        System.out.println("Mean: " + mean);

        Double stdev = mf.stdev(serverMsg);
        System.out.println("Stdev: " + stdev);

        // send calculations
        netIO.writeFloat(mean.floatValue());
        netIO.writeFloat(stdev.floatValue());

        // release resources
        netIO.close();
        socket.close();
    }
}

class NetworkIO {
    private InputStream is;
    private BufferedInputStream bis;
    private DataInputStream dis;
    private ObjectInputStream ois;

    private OutputStream os;
    private BufferedOutputStream bos;
    private DataOutputStream dos;
    private ObjectOutputStream oos;

    public NetworkIO(Socket socket) throws IOException {
        is = socket.getInputStream();
        bis = new BufferedInputStream(is);
        dis = new DataInputStream(bis);
        ois = new ObjectInputStream(dis);

        os = socket.getOutputStream();
        bos = new BufferedOutputStream(os);
        dos = new DataOutputStream(bos);
        oos = new ObjectOutputStream(dos);
    }

    public String read() throws IOException {
        return ois.readUTF();
    }

    public void write(String msg) throws IOException {
        oos.writeUTF(msg);
        oos.flush();
    }

    public void writeFloat(Float f) throws IOException {
        oos.writeFloat(f);
        oos.flush();
    }

    public void close() throws IOException {
        oos.close();
        dos.close();
        bos.close();
        os.close();

        ois.close();
        dis.close();
        bis.close();
        is.close();
    }
}

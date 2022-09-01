package ch.unibas.dmi.dbis.cs108.gui;

import ch.unibas.dmi.dbis.cs108.server.Server;
import javafx.application.Application;

public class Main {

    /**
     * This is simply a wrapper to launch the {@link MainGui} class.
     * The reason this class exists is documented in {@link MainGui#main(String[])}
     */
    public static void main(String[] args) {
        String arg;
        if (args.length == 0) {
            arg = "client"; //hack to start client without parameters
        } else {
            arg = args[0];
        }

        if (arg.equals("server")) {
            Server.main(args);
        } else if (arg.equals("client")){
            Application.launch(MainGui.class, args);
        } else {
            System.out.println("For starting the server use command:");
            System.out.println("java -jar bubble-uni.jar server <PORT_NUMBER>\n");
            System.out.println("For starting the client use command:");
            System.out.println("java -jar bubble-uni.jar client <IP_ADDRESS>:<PORT_NUMBER> [player Name]\n");
            System.out.println("For starting the server or client on default settings use no parameters except server or client");
        }
    }
}

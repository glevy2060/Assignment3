package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.Database;

import java.nio.charset.StandardCharsets;

public class ReactorMain {

    public static void main(String[] args) {
        int n = (int) (Math.pow(2, 8) + 7);
        byte[] b = new byte[2];
        b[0] = (byte)((n >> 8) & 0xFF);
        b[1] = (byte)((n) & 0xFF);
        System.out.println(b[0] +" " +b[1]);

        String s11 = "09";
        byte[] s1 = s11.getBytes(StandardCharsets.UTF_8);

        String s12 = "9";
        byte[] s2 = s12.getBytes(StandardCharsets.UTF_8);

        short result = (short) ((b[0] & 0xff) <<8);
        result += (short) (b[1] & 0xff);
        System.out.println(result);
        String g = "Gal efhFGoe93";
        int gLength = g.length();
        byte[] g1 = g.getBytes(StandardCharsets.UTF_8);
        System.out.println("w");

        Database db = Database.getInstance();
        int n1= 0;

    }
}

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.*;
//import java.util.ArrayList;
//
//public class PlayerServ {
//
//    private int player, player1,player2,gid, side;
//    private PrintWriter printWriter;
//    private BufferedReader bufferedReader;
//
//    private ArrayList arrayWriter;
//    private ArrayList arrayReader;
//
//    private Connection connect = null;
//    private Statement statement = null;
//    private PreparedStatement preparedStatement = null;
//    private ResultSet resultSet = null;
//
//    private int[][] playerMoves = null;
//
//
//
//
//    //constructor for player
//    public PlayerServ(int player, int gid, BufferedReader bufferedReader, PrintWriter printWriter, int side) {
//
//        this.player = player;
//        this.gid = gid;
//        this.bufferedReader = bufferedReader;
//        this.printWriter = printWriter;
//        this.side = side;
//
//        playerMoves = new int[3][3];
//        System.out.println("array ");
//        System.out.println(playerMoves[1][1]);
//
//
//
//
//        try {
//            // This will load the MySQL driver, each DB has its own driver
//            Class.forName("com.mysql.jdbc.Driver");
//
//            // Setup the connection with the DB
//            connect = DriverManager.getConnection("jdbc:mysql://localhost/tictactoe?" + "user=root&password=");
//
//            statement = connect.createStatement();
//
//
//
//
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//
//    }
//
//
//
//
//
//
//    public  void go() {
//        if (side == 2) {
//
//            printWriter.println("Join");
//            printWriter.println(player);
//            printWriter.flush();
//            System.out.println("joined mesagge sentr ");
//
//
//
//
//        }
//
//
//        while (true) {
//
//
//                try {
//
//
//                    System.out.println("at beginning of gameserv listen");
//                    //player 1 reads and writes to the second streams
//
//                    String flag = bufferedReader.readLine();
//
//                    switch (flag) {
//
//
//                        case "Move":
//
//                            System.out.println("in side: " + side + " " + Thread.currentThread());
//
//                            int y = Integer.parseInt(bufferedReader.readLine());
//
//                            int x = Integer.parseInt(bufferedReader.readLine());
//
//                            move(gid, player, x, y);
//
//                            checkWinner();
//
//                            int[] ar = lastMove(gid);
//
//                            System.out.println(printWriter + " side " + side);
//
//                             send("Move");
//                            send(ar[0]);
//                            send(ar[1]);
//
//
//                            System.out.println(x + " and " + y);
//                            System.out.println("player " + player + " played");
//                            System.out.println("from side " + side);
//                            break;
//
//                    }
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//    }
//    private void checkWinner() {
//
//
//
//
//
//
//
//
//
//    }
//
//    public  void send(int msg ){
//
//        printWriter.println(msg);
//        System.out.println(msg +  " sent ");
//        printWriter.flush();
//
//
//
//    }
//
//    public  void send(String msg ){
//        printWriter.println(msg);
//        System.out.println(msg + " sent");
//        printWriter.flush();
//
//
//
//    }
//
//
//    public int move(int gID, int pID, int  x, int y) {
//        try {
//            String sql = "INSERT INTO moves(gID,pID,x,y) VALUES(?,?,?,?)";
//            preparedStatement = connect.prepareStatement(sql);
//            preparedStatement.setInt(1, gID);
//            preparedStatement.setInt(2, pID);
//            preparedStatement.setInt(3, x);
//            preparedStatement.setInt(4, y);
//            preparedStatement.executeUpdate();
//
//
//        } catch(Exception e) {
//            return -1;
//        }
//        System.out.println("added");
//        return 1 ;
//    }
//
//    public int[] lastMove(int gID) {
//        int [] arr = new int[2];
//
//        try {
//            String sql = "SELECT x,y FROM moves WHERE gID = ? order by autoID desc limit 1 ";
//            preparedStatement = connect.prepareStatement(sql);
//            preparedStatement.setInt(1, gID);
//            resultSet=  preparedStatement.executeQuery();
//
//
//            while (resultSet.next()) {
//
//                arr[1] = resultSet.getInt(1);
//                arr[0] = resultSet.getInt(2);
//                System.out.println(arr[0]);
//                System.out.println(arr[1]);
//            }
//
//        } catch(Exception e) {
//
//        }
//        System.out.println("added");
//        return arr;
//    }
//}

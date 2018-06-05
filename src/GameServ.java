import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

public class GameServ {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private int player1,player2, gid;
    private int moveCount = 0;

    private PrintWriter printWriter1, printWriter2;
    private BufferedReader bufferedReader1, bufferedReader2;

    private ArrayList arrayWriter = new ArrayList(2);
    private ArrayList arrayReader = new ArrayList(2);
    private ArrayList players = new ArrayList(2);
    private String side;

    private int[][] playerX = new int [3][3];
    private int[][] playerO = new int [3][3];

    private boolean b ;



    public int getGid() {
        return gid;
    }



    public GameServ(int gid) {
   b=false;
        this.gid = gid;

        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");

            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/tictactoe?" + "user=root&password=");

            statement = connect.createStatement();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }



    public void join(int player, BufferedReader bufferedReader, PrintWriter printWriter,String side ) {



        if (side == "X") {
            this.bufferedReader1 = bufferedReader;
            this.printWriter1 = printWriter;
            this.player1 = player;

        }else if (side == "O" ){
            this.bufferedReader2 = bufferedReader ;
            this.printWriter2 = printWriter ;
            this.player2 = player ;


        }
    }


    public void player(String side) {

         b = true;

        if (side == "O") {


            printWriter1.println("Enter");
            printWriter1.println(player2);
            printWriter1.flush();
            System.out.println("joined mesagge sentr ");
        }


        while (b) {

            if (side == "X"){

                try {


                    System.out.println("at beginning of gameserv listen player1 " + Thread.currentThread());
                    //player 1 reads and writes to the second streams




                    String flag = bufferedReader1.readLine();

                    if (flag == null){

                        cleanMoves(gid);
                        cleanGames(gid);

                        bufferedReader1.close();
                        printWriter1.close();
                        b=false;

                        break;


                    }

                    switch (flag) {


                        case "Move":


                            int y = Integer.parseInt(bufferedReader1.readLine());

                            int x = Integer.parseInt(bufferedReader1.readLine());

                            move(gid, player1, x, y);
                            moveCount++;
                            System.out.println(moveCount + "movecount");

                            playerX[y][x] = 1;

                            int[] ar = lastMove(gid);

                            sendAll("Move");
                            sendAll("X");
                            sendAll(ar[0]);
                            sendAll(ar[1]);



                            System.out.println(x + " and " + y);
                            System.out.println("player " + player1 + " played");
                            System.out.println("from side " + side);
                            break;

                        case "Terminate":
                            System.out.println("terminate side is "+ side);

                            //set gamestate to 2 as game is inactive and finished
                            //setFlagDb(gid);

                            b=false;
                            break;

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!checkWinner(playerX,1) ) {
                    cleanMoves(gid);
                    cleanGames(gid);
                    //setFlagDb(gid);
                    sendAll("End");
                    sendAll(side);
                    b = false;
                    System.out.println("side "+ side + " exited from loop");
                    break;

                }

                if (moveCount == 9) {
                    cleanMoves(gid);
                    cleanGames(gid);
                   // setFlagDb(gid);
                    tie();
                    b=false;
                    break;
                }

        }
        else if (side=="O"){


                    try {


                        System.out.println("at beginning of gameserv listen player2 " + Thread.currentThread());


                        //player 1 reads and writes to the second streams

                        String flag = bufferedReader2.readLine();

                        if (flag == null){

                            cleanMoves(gid);
                            cleanGames(gid);

                            bufferedReader2.close();
                            printWriter2.close();
                            b=false;

                            break;


                        }

                        switch (flag) {


                            case "Move":



                                int y = Integer.parseInt(bufferedReader2.readLine());

                                int x = Integer.parseInt(bufferedReader2.readLine());

                                move(gid, player2, x, y);
                                moveCount++;
                                playerO[y][x] = 2 ;

                                System.out.println(moveCount + "movecount");

                                int[] ar = lastMove(gid);

                                sendAll("Move");
                                sendAll("O");
                                sendAll(ar[0]);
                                sendAll(ar[1]);


                                System.out.println(x + " and " + y);
                                System.out.println("player2 " +  " played");
                                System.out.println("from side " + side);
                                break;

                            case "Terminate":
                                System.out.println("terminate side is "+ side);


                                //set gamestate to 2 as game is inactive and finished

                                b=false;
                                break;

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                if (!checkWinner(playerO,2) ) {

                    cleanMoves(gid);
                    cleanGames(gid);

                    sendAll("End");
                    sendAll(side);
                    b = false;
                    System.out.println("side "+ side + " exited from loop");

                    break;

                }

                if (moveCount == 9) {
                    cleanMoves(gid);
                    cleanGames(gid);

                    tie();
                    b=false;
                    break;
                }

            }


        }



    }



    public  void player1() {

boolean b = true;

        while (b) {


            try {


                System.out.println("at beginning of gameserv listen player1 " + Thread.currentThread());
                //player 1 reads and writes to the second streams

                String flag = bufferedReader1.readLine();

                switch (flag) {


                    case "Move":


                        int y = Integer.parseInt(bufferedReader1.readLine());

                        int x = Integer.parseInt(bufferedReader1.readLine());

                        move(gid, player1, x, y);
                        moveCount++;
                        System.out.println(moveCount + "movecount");

                        playerX[y][x] = 1 ;

                        int[] ar = lastMove(gid);

                        sendAll("Move");
                        sendAll("X");
                        sendAll(ar[0]);
                        sendAll(ar[1]);

                        if (!checkWinner(playerX,1)){

                            sendAll("End");
                            sendAll(player1 );
                            b=false;
                            break;

                        }

                        if (moveCount == 9){

                            tie();
                            //b=false;
                            break;
                        }

                        System.out.println(x + " and " + y);
                        System.out.println("player " + player1 + " played");
                        System.out.println("from side " + side);
                        break;

                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }



    }




    public  void player2() {


              printWriter1.println("Enter");
              printWriter1.println(player2);
              printWriter1.flush();
              System.out.println("joined mesagge sentr ");


        boolean b = true;
        while (b) {


            try {


                System.out.println("at beginning of gameserv listen player2 " + Thread.currentThread());


                //player 1 reads and writes to the second streams

                String flag = bufferedReader2.readLine();

                switch (flag) {


                    case "Move":



                        int y = Integer.parseInt(bufferedReader2.readLine());

                        int x = Integer.parseInt(bufferedReader2.readLine());

                        move(gid, player2, x, y);
                        moveCount++;
                        playerO[y][x] = 2 ;

                        System.out.println(moveCount + "movecount");

                        int[] ar = lastMove(gid);

                        sendAll("Move");
                        sendAll("O");
                        sendAll(ar[0]);
                        sendAll(ar[1]);

                        if (!checkWinner(playerO,2)){

                            sendAll("End");
                            sendAll( player2 );
                            b=false;
                            break;

                        }

                        if (moveCount == 9){

                            tie();
                            //b=false;
                            break;


                        }

                        System.out.println(x + " and " + y);
                        System.out.println("player " + player2 + " played");
                        System.out.println("from side " + side);
                        break;

                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }



    }
    private boolean checkWinner(int player[][],int mark) {

       //y axis
        for (int x = 0; x < 3; x++) {

            if (player[0][x]==mark && player[1][x]==mark  && player[2][x]==mark ){


                return false;

            }

        }

        // x  axis
        for (int y = 0; y < 3; y++) {

            if (player[y][0]==mark && player[y][1]==mark  && player[y][2]==mark ){


                return false;
            }

        }

        //diagonal
     if (    ( player[0][0] == mark && player[1][1] == mark && player[2][2] == mark ) ||
             ( player[2][0] == mark && player[1][1] == mark && player[0][2] == mark )    )

            return false;


        return true;

    }





    private void tie() {

        sendAll("Tie");

       // sendAll(side);

    }


    public void sendAll(String msg) {

        printWriter1.println(msg);
        printWriter1.flush();
        printWriter2.println(msg);
        printWriter2.flush();
    }

    public void sendAll(int msg) {

        printWriter1.println(msg);
        printWriter1.flush();
        printWriter2.println(msg);
        printWriter2.flush();
    }


    public  void sendto1(int msg ){

        printWriter1.println(msg);
        System.out.println(msg +  " sent ");
        printWriter1.flush();



    }

    public  void sendto1(String msg ){
        printWriter1.println(msg);
        System.out.println(msg + " sent");
        printWriter1.flush();



    }

    public  void sendto2(int msg ){

        printWriter2.println(msg);
        System.out.println(msg +  " sent ");
        printWriter2.flush();



    }

    public  void sendto2(String msg ){
        printWriter2.println(msg);
        System.out.println(msg + " sent");
        printWriter2.flush();



    }
    public void cleanGames(int gid){
        try {
            String sql = "delete from games  where autoID = ?";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, gid);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void cleanMoves(int gid){
        try {
        String sql = "delete from moves  where gID = ?";
        preparedStatement = connect.prepareStatement(sql);
        preparedStatement.setInt(1, gid);
        preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setFlagDb(int gid)  {
        try {

            String sql = "update games set gameState = 2  where autoID = ?";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, gid);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }




    public int move(int gID, int pID, int  x, int y) {
        try {
            String sql = "INSERT INTO moves(gID,pID,x,y) VALUES(?,?,?,?)";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, gID);
            preparedStatement.setInt(2, pID);
            preparedStatement.setInt(3, x);
            preparedStatement.setInt(4, y);
            preparedStatement.executeUpdate();


        } catch(Exception e) {
            return -1;
        }
        System.out.println("added");
        return 1 ;
    }

    public int[] lastMove(int gID) {
        int [] arr = new int[2];

        try {
            String sql = "SELECT x,y FROM moves WHERE gID = ? order by autoID desc limit 1 ";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, gID);
            resultSet=  preparedStatement.executeQuery();


            while (resultSet.next()) {

                arr[1] = resultSet.getInt(1);
                arr[0] = resultSet.getInt(2);
                System.out.println(arr[0]);
                System.out.println(arr[1]);
            }

        } catch(Exception e) {

        }
        System.out.println("added");
        return arr;
    }

}




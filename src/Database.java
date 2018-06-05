import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private ArrayList clientArrayWriter;
    private ArrayList clientArrayReader ;

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int pid  ;
    private String[][] data;
    private  ArrayList<String> arrayList;
    private String flag ;
    private GameServ game;
    HashMap<Integer,GameServ> gameServHashMap;




    public Database() {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");

            // Setup the connection with the DB
            connect = DriverManager.getConnection("jdbc:mysql://localhost/tictactoe?"+ "user=root&password=");

            statement = connect.createStatement();

            gameServHashMap=new HashMap<>();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupNetwork() throws IOException, ClassNotFoundException {



        serverSocket = new ServerSocket(5555);

        int c = -1;

        while (true) {

            System.out.println("about to accept client connection ");
            clientSocket = serverSocket.accept();
            //client counter
            c++;
            System.out.println("connection established");
            System.out.println(clientSocket);

            DatabaseListen dl = new DatabaseListen(clientSocket,c);
            Thread database = new Thread(dl);
            database.start();
            System.out.println("connection " +c);
            System.out.println(Thread.currentThread());

        }


    }

    private class DatabaseListen implements Runnable {
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private InputStream is;
        private ObjectInputStream ois;
        private OutputStream os;
        private ObjectOutputStream oos;
        private PrintWriter printWriter;
        private InputStreamReader inputStreamReader;
        private BufferedReader bufferedReader;
        private String name, surname, username, password, email;

        private int pid;
        private Socket socketClient;
        private int c,gid;


        private DatabaseListen(Socket socketClient,int c ) throws IOException, ClassNotFoundException {

            this.socketClient = socketClient;
            this.c = c;
            this.gid = gid;


                os = socketClient.getOutputStream();
                is = socketClient.getInputStream();


                printWriter = new PrintWriter(os);



                inputStreamReader = new InputStreamReader(is);
                bufferedReader = new BufferedReader(inputStreamReader);

               // clientArrayReader.add(bufferedReader);

                System.out.println("connection added with index " +  c);
                System.out.println(Thread.currentThread());


            System.out.println("connected" + socketClient);
        }

        @Override
        public void run() {

                boolean b = true;
                while (b) {

                    try {
                        System.out.println("at beginning current thread" + Thread.currentThread());

                        System.out.println(bufferedReader);
                        flag = bufferedReader.readLine();


                        if (flag == null){


                            socketClient.close();


                            break;


                        }


                        System.out.println(flag );

                        switch (flag) {


                            case "Registration":
                                System.out.println("in reg");
                                name = bufferedReader.readLine();
                                surname = bufferedReader.readLine();
                                username = bufferedReader.readLine();
                                password = bufferedReader.readLine();
                                email = bufferedReader.readLine();

                                System.out.println(name + surname + username + password + email);

                                //save it on database
                                pid = register(name, surname, username, password, email);
                                System.out.println(pid);

                                printWriter.println("Registration");
                                printWriter.println(pid);
                                //alert for try again
                                printWriter.flush();

                                break;

                            case "Login":
                                System.out.println("in log");
                                name = bufferedReader.readLine();
                                password = bufferedReader.readLine();

                                System.out.println(name + password);

                                pid = checkLogin(name, password);

                                printWriter.println("Login");
                                printWriter.println(pid);
                                // System.out.println(bufferedReader.readLine());

                                String[][] data = gamesList();

                                //send the size of data
                                //  printWriter.println("List");
                                printWriter.println(data.length);
                                //printWriter.flush();
                                System.out.println(data.length + "sent ");
                                //System.out.println(Thread.currentThread());

                                for (int y = 0; y < data.length; y++) {
                                    for (int x = 0; x < 2; x++) {

                                        printWriter.println(data[y][x]);

                                        //System.out.print(data[y][x]);

                                    }


                                }
                                printWriter.flush();
                                break;

                            case "Refresh":
                                System.out.println("in Refresh");
                                // System.out.println(bufferedReader.readLine());

                                data = gamesList();

                                //send the size of data
                                printWriter.println("Refresh");
                                printWriter.println(data.length);
                                //printWriter.flush();
                                System.out.println(data.length + "sent ");
                                //System.out.println(Thread.currentThread());

                                for (int y = 0; y < data.length; y++) {
                                    for (int x = 0; x < 2; x++) {

                                        printWriter.println(data[y][x]);

                                        //System.out.print(data[y][x]);

                                    }


                                }
                                printWriter.flush();
                                break;

                            case "New":
                                System.out.println("in new");
                                System.out.println(socketClient + "SOCKET IS ");
                                System.out.println(Thread.currentThread());


                                //     System.out.println(clientArrayWriter.size() + " array size ");
                                pid = Integer.parseInt(bufferedReader.readLine());
                                System.out.println(pid);

                                gid = newGame(pid);

                                printWriter.println("New");
                                printWriter.println(gid);
                                System.out.println("gid sent " + gid);
                                printWriter.flush();

                                System.out.println("game created with pid " + pid);
                                System.out.println("game created with gid " + gid);

                                // at connection number c a new game created
                                 game = new GameServ(gid);

                                 //store in a hashed map to enter the game intended
                                 gameServHashMap.put(gid,game);

                                game.join(pid,bufferedReader,printWriter,"X");
                                game.player("X");

                                System.out.println(" game id is   " + game.getGid());
                                System.out.println(socketClient.isClosed());
                                System.out.println("current thread no " + Thread.activeCount());
                                // clientArrayWriter.add(printWriter);

                                // System.out.println("clientarray size is " + clientArrayWriter.size());

                                //  playerServ = new PlayerServ(pid,gid,clientArrayWriter,clientArrayReader);

                                break;

                            case "Join":
                                System.out.println("in join");
                                System.out.println(Thread.currentThread());

                                System.out.println(socketClient + "SOCKET IS ");

                                gid = Integer.parseInt(bufferedReader.readLine());
                                pid = Integer.parseInt(bufferedReader.readLine());
                                joinGame(gid, pid);

                                printWriter.println("Join");
                                printWriter.flush();

                                System.out.println(" game id is   " + game.getGid());

                                //hashed map to enter the game you intended
                                gameServHashMap.get(gid).join(pid, bufferedReader, printWriter,"O");

                               gameServHashMap.get(gid).player("O");
                               // game.player("O");
                                // clientArrayWriter.add(printWriter);
                                //  System.out.println("clientarray size is " + clientArrayWriter.size());

                                //  playerServ = new PlayerServ(pid,gid,clientArrayWriter,clientArrayReader);



                                break;

                            case "Shutdown":
                                printWriter.println("Shutdown");
                                printWriter.flush();
                                socketClient.close();
                                b=false;
                                break;

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (socketClient.isClosed()){

                        break;

                    }

                }


            }


    }


    public void cleanDatabase(){
        try {
//
//        String sql = "INSERT INTO users(name, surname, username, password, email) VALUES(?,?,?,PASSWORD(?),?)";
//        preparedStatement = connect.prepareStatement(sql);
//        preparedStatement.setString(1, name);
//        preparedStatement.setString(2, surname);

        String sql = "delete FROM games WHERE isactive = 2;";
        preparedStatement = connect.prepareStatement(sql);
        preparedStatement.executeUpdate();


    } catch (SQLException e) {
        e.printStackTrace();
    }
    }


    public int checkLogin(String user, String pass) {
        String sql = "SELECT autoID FROM users WHERE username = ? AND password = PASSWORD(?) AND isactive = 1;";
        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()  == false) {
              
                return 0;
            } else {

                String id = resultSet.getString("autoID");
                System.out.println(id + " logged in.");
                return Integer.parseInt(id);
            }
        } catch(SQLException s) {
            System.out.println(-2);
            return -2;
        } catch(Exception i) {
            System.out.println(-1);
            i.printStackTrace();
            return -1;
        }
    }

    public int register(String name, String surname, String username, String password, String email) {
        try {
            String sql = "INSERT INTO users(name, surname, username, password, email) VALUES(?,?,?,PASSWORD(?),?)";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, email);
            preparedStatement.executeUpdate();

            sql = "SELECT autoID FROM users WHERE username = ?";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();


            if(resultSet.next() == false) {
                return 0;
            } else {

                System.out.println("User added with id number of "+ resultSet.getInt("autoID"));


                return resultSet.getInt("autoID");

            }
        } catch(Exception e) {
          e.printStackTrace();

            return -1;
        }
    }

    public int newGame(int p1)   {
        try {

            String sql = "INSERT INTO games(p1) VALUES(?)";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, p1);
            preparedStatement.executeUpdate();

            //select the last added game
            sql = "SELECT autoID FROM games WHERE p1 = ? and p2 is null order by autoID desc limit 1 ";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, p1);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next() == false) {
                return 0;
            } else {

                return resultSet.getInt("autoID");

            }



        } catch(Exception e) {
            e.printStackTrace();
            return -1;
        }


    }
    public int joinGame(int gid ,int p2) {
        try {

            String sql = "update games set p2 = ? , gameState =1 where autoID = ?";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setInt(1, p2);
            preparedStatement.setInt(2, gid);
            preparedStatement.executeUpdate();

            sql = "select p1 from games where  autoid= ? ";
            preparedStatement = connect.prepareStatement(sql);

            preparedStatement.setInt(1, gid);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next() == false) {
                return 0;
            } else {

                return resultSet.getInt("p1");

            }


        } catch(Exception e) {
            return -1;
        }

    }




    public String[][] gamesList () {



    // table.setPreferredScrollableViewportSize(new Dimension(500, 70));
      //  table.setFillsViewportHeight(true);

         arrayList = new ArrayList<>();


        try {

            String sql = " SELECT autoID,p1 FROM games where p2 is null ";
           // String sql = " SELECT name,surname FROM users";
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            String autoID, pid;


            while (resultSet.next()) {



                autoID = resultSet.getString(1);
                pid = resultSet.getString(2);

                arrayList.add(autoID);
                arrayList.add(pid);

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        //System.out.println(arrayList.size());
            data = new String[(arrayList.size()/2)][2];
            int i =0;
            for ( int y=0; y<arrayList.size()/2;y++) {
                for (int x =0 ; x<2;x++  ){

                      data[y][x] = arrayList.get(i++);

                    //System.out.print(data[y][x]);

                }
                //System.out.println();


            }
            return data;


    }




    public void shutdown() {
        try {
            resultSet = null;
            preparedStatement = null;
            statement = null;
            connect.close();
            connect = null;
        } catch(Exception e) {

        }
    }

    public static void main(String[] arg ) throws IOException, ClassNotFoundException {
        Database d = new Database();
        //System.out.println(d.register("foo","bar","foobar","fbar","foobar@gmail.com"));
       // d.gamesList();
        d.setupNetwork();



    }


}
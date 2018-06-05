import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Game implements Runnable {

    private JPanel mainPanel,infoPanel,statusPanel;
    private JFrame frame;
    private JButton[][] button = new JButton[3][3];
    private JTextArea jTextArea ;
    private JLabel infoLabel;
    private JScrollPane scrollPane ;
    private ImageIcon imageO = new ImageIcon("images/tic-tac-toe-o.png");
    private ImageIcon imageX = new ImageIcon("images/tic-tac-toe-x.png");
    private sendButton send ;




    private Socket socket;
    private InputStream is ;
    private OutputStream os;
    private PrintWriter printWriter;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private int pid,gid,opponent ;
    private String side;
    private boolean wait = true;
    private boolean run = false;

    //constructor for new game
    public Game(Socket socket, int pid, String side,int gid) throws IOException {
        this.socket = socket;
        this.side = side;
        this.gid = gid ;
        this.pid=pid;
        wait = false;



        //setup network
        synchronized (this) {
            this.socket = socket;

            os = socket.getOutputStream();
            printWriter = new PrintWriter(os);

            is = socket.getInputStream();
            inputStreamReader = new InputStreamReader(is);
            bufferedReader = new BufferedReader(inputStreamReader);
        }

        infoPanel = new JPanel();
        infoLabel = new JLabel("Player " + pid );
        //infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(infoLabel);

        statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setPreferredSize(new Dimension(450,200));
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        //go to nextline when reach end of line text wrap
        jTextArea.setLineWrap(true);
        //autoscrolling as it is written
        DefaultCaret caret = (DefaultCaret)jTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPane = new JScrollPane(jTextArea);
        statusPanel.add(scrollPane);


        //send flag and create a game in db


        jTextArea.append("You are playing as "+side +"\n");
        jTextArea.append("You created a new game with No: "+ gid+ "\n");
        jTextArea.append("Waiting for other player to enter the game...\n");


        GridLayout grid = new GridLayout(3,3,4,4);
        mainPanel = new JPanel();
        mainPanel.setLayout(grid);
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setVisible(true);

        send = new sendButton();
        for (int y=0; y<3; y++) {
            for (int x=0; x<3; x++) {

                button[y][x] = new JButton();
                mainPanel.add(button[y][x]);

//                button[y][x].repaint();
                button[y][x].addActionListener(send);

            }
        }





        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(450,600);
        frame.add(BorderLayout.CENTER,mainPanel);
        frame.add(BorderLayout.NORTH,infoPanel);
        frame.add(BorderLayout.SOUTH,statusPanel);
        frame.setVisible(true);

//        Set<Thread> threads = Thread.getAllStackTraces().keySet();
//
//        for (Thread t : threads) {
//            String name = t.getName();
//            Thread.State state = t.getState();
//            int priority = t.getPriority();
//            String type = t.isDaemon() ? "Daemon" : "Normal";
//            System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
//        }

    }
    //constructor for a game to join
    public Game(Socket socket, int pid,int opponent, String side,int gid) throws IOException {
        this.socket = socket;
        this.side = side;
        this.gid = gid ;
        this.pid = pid;
        this.opponent = opponent;
        wait=false;
        synchronized (this) {
            //setup network
            this.socket = socket;
            os = socket.getOutputStream();
            printWriter = new PrintWriter(os);

            is = socket.getInputStream();
            inputStreamReader = new InputStreamReader(is);
            bufferedReader = new BufferedReader(inputStreamReader);
        }
        infoPanel = new JPanel();
        infoLabel = new JLabel("Player " + pid );
        //infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(infoLabel);


        statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setPreferredSize(new Dimension(450,200));
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        //go to nextline when reach end of line text wrap
        jTextArea.setLineWrap(true);
        //autoscrolling as it is written
        DefaultCaret caret = (DefaultCaret)jTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPane = new JScrollPane(jTextArea);
        statusPanel.add(scrollPane);

        jTextArea.append("You are playing as "+ side +  "\n");
        jTextArea.append("You entered the game No: " + gid +"\n");
        jTextArea.append("Game started\n");
        jTextArea.append("Player " + opponent + "'s move.\n");





        GridLayout grid = new GridLayout(3,3,4,4);
        mainPanel = new JPanel();
        mainPanel.setLayout(grid);
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setVisible(true);

        send = new sendButton();
        for (int y=0; y<3; y++) {
            for (int x=0; x<3; x++) {

                button[y][x] = new JButton();
                mainPanel.add(button[y][x]);

                button[y][x].addActionListener(send);

            }
        }


        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450,600);
        frame.setLocationRelativeTo(null);
        frame.add(BorderLayout.CENTER,mainPanel);
        frame.add(BorderLayout.NORTH,infoPanel);
        frame.add(BorderLayout.SOUTH,statusPanel);





//send flag and join a game in db

        frame.setVisible(true);


    }




    public class sendButton implements ActionListener {
    public void actionPerformed(ActionEvent e) {

        if (wait) {
            printWriter.println("Move");

            //side X = 0 && O =1

            if (e.getSource() == button[0][0]) {

                printWriter.println(0);
                printWriter.println(0);
                printWriter.flush();



            } else if (e.getSource() == button[0][1]) {
                printWriter.println(0);
                printWriter.println(1);
                printWriter.flush();

            } else if (e.getSource() == button[0][2]) {
                printWriter.println(0);
                printWriter.println(2);
                printWriter.flush();

            } else if (e.getSource() == button[1][0]) {
                printWriter.println(1);
                printWriter.println(0);
                printWriter.flush();

            } else if (e.getSource() == button[1][1]) {
                printWriter.println(1);
                printWriter.println(1);
                printWriter.flush();

            } else if (e.getSource() == button[1][2]) {
                printWriter.println(1);
                printWriter.println(2);
                printWriter.flush();

            } else if (e.getSource() == button[2][0]) {
                printWriter.println(2);
                printWriter.println(0);
                printWriter.flush();

            } else if (e.getSource() == button[2][1]) {
                printWriter.println(2);
                printWriter.println(1);
                printWriter.flush();

            } else {
                printWriter.println(2);
                printWriter.println(2);
                printWriter.flush();

            }


        }

    }

}



    @Override
    public void run() {

            run = true;
            while (run){


                    try {

                        System.out.println("at the beginning");
                        System.out.println(bufferedReader);


                        String flag = bufferedReader.readLine();


                        System.out.println("game started flag is " + flag);

                        switch (flag) {
                            case "Enter":
                                opponent = Integer.parseInt(bufferedReader.readLine());

                                jTextArea.append("Player " + opponent + " has joined the game \n ");
                                jTextArea.append("Game started\n ");
                                jTextArea.append("Your move..\n ");
                                wait=true;


                                break;



                            case "Move":
                                System.out.println(" in move");

                                String mark = bufferedReader.readLine();

                                int y = Integer.parseInt(bufferedReader.readLine());
                                int x = Integer.parseInt(bufferedReader.readLine());
                                System.out.println("x is " + x + " and " + " y is " + y);

                                //button[y][x].setText(mark);


                                if (mark.equals("X")) {

                                    button[y][x].setIcon(imageX);
                                    button[y][x].removeActionListener(send);
                                }
                                else {

                                    button[y][x].setIcon(imageO);
                                    button[y][x].removeActionListener(send);

                                }


                                if(!mark.equals(side)) {

                                    jTextArea.append("Player " + opponent + " played.\n It is your turn.\n");
                                    wait=true;

                                }

                                else {

                                    jTextArea.append("Waiting for the opponent's move.\n");
                                    wait=false;

                                }

                                break;

                            case "Tie":


                                printWriter.println("Terminate");
                                printWriter.flush();
                                frame.dispose();

                                EndGui e = new EndGui(pid,printWriter,bufferedReader,socket);
                                Thread t = new Thread(e);

                                run=false;

                                e.tie();
                                t.start();
                                break;

                            case "End":

                               // int winner = Integer.parseInt(bufferedReader.readLine());
                                String s = bufferedReader.readLine();

                                if (!s.equals(side))
                                {
                                    printWriter.println("Terminate");
                                    printWriter.flush();
                                }
                                //run=false;
                                frame.dispose();

                                EndGui e1 = new EndGui(pid,printWriter,bufferedReader,socket);
                                Thread t1 = new Thread(e1);
                                run=false;


                                if (side.equals(s)){

                                    e1.win();
                                    t1.start();

                                    break;
                                }
                                else{
                                    e1.lost();
                                    t1.start();

                                    break;
                                }

                                //GamesList game = new GamesList(socket,pid,);

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
    }



    private static class EndGui implements ActionListener, Runnable {
        private JPanel panelEnd;
        private JLabel labelEnd,labelEnd1,labelEnd2,labelEnd3;
        private JFrame frameEnd;
        private JButton buttonNew,buttonExit;
        private GridBagConstraints c;
        private GridBagLayout grid ;

        private Socket socket;
        private InputStream is ;
        private OutputStream os;
        private PrintWriter printWriter;
        private InputStreamReader inputStreamReader;
        private BufferedReader bufferedReader;
        private int pid;
        private boolean b ;

        private String[][] data;


        public EndGui(int pid , PrintWriter printWriter, BufferedReader bufferedReader,Socket socket) throws IOException {

            b=true;
            this.socket= socket;
            this.pid = pid;
            this.printWriter = printWriter;
            this.bufferedReader = bufferedReader;

            grid = new GridBagLayout();
            c = new GridBagConstraints();

            panelEnd = new JPanel(grid);

            buttonNew = new JButton("Game List");
            buttonNew.addActionListener(this);
            buttonExit = new JButton("Exit");
            buttonExit.addActionListener(this);

            labelEnd = new JLabel("Game Over");

            c.gridx = 2 ;
            c.gridy = 2;
            c.gridwidth = 3 ;
            c.ipady = 5;
            panelEnd.add(labelEnd,c);

            c.gridx = 2 ;
            c.gridy = 7 ;
            c.gridwidth = 1 ;
            c.ipady = 0 ;
            panelEnd.add(buttonNew,c);

            c.gridx = 4 ;
            c.gridy = 7 ;
            c.gridwidth = 1 ;
            c.ipadx = 25;
            panelEnd.add(buttonExit,c);



            frameEnd = new JFrame("Game Over. - Player " + pid);
            frameEnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameEnd.setSize(250,150);
            frameEnd.add(panelEnd);
            frameEnd.setLocationRelativeTo(null);

        }

        public void win(){

            labelEnd1 = new JLabel("You win.");
            c.gridx = 2 ;
            c.gridy = 4 ;
            c.gridwidth = 3 ;
            c.ipadx = 5;
            c.ipady = 15;

            panelEnd.add(labelEnd1,c);

            frameEnd.setVisible(true);
        }

        public void lost(){

            labelEnd2 = new JLabel("You lost.");
            c.gridx = 2 ;
            c.gridy = 4 ;
            c.gridwidth = 3 ;
            c.ipady = 15;
            c.ipadx = 5;

            panelEnd.add(labelEnd2,c);

            frameEnd.setVisible(true);

        }

        public void tie(){

            labelEnd3 = new JLabel("It is a Tie.");
            c.gridx = 2 ;
            c.gridy = 4 ;
            c.gridwidth = 3 ;
            c.ipady = 15;
            c.ipadx = 5;

            panelEnd.add(labelEnd3,c);

            frameEnd.setVisible(true);



        }

        public void actionPerformed(ActionEvent e ){

            if (e.getSource() == buttonNew){


                printWriter.println("Refresh");
                printWriter.flush();



            }else if (e.getSource() == buttonExit){

            printWriter.println("Shutdown");
            printWriter.flush();

            }


        }


        @Override
        public void run() {

    while(b) {
        try {

            System.out.println("in refresh "  + Thread.currentThread());

            String flag = bufferedReader.readLine();
            switch (flag)   {
            case "Refresh":


                int length = Integer.parseInt(bufferedReader.readLine());
                System.out.println("length is "+ length);

                data = new String[length][3];

                for (int y = 0; y < data.length; y++) {
                    for (int x = 0; x < 3; x++) {

                        if (x==2){
                            data[y][x]= "Join";
                        }
                        else {
                            try {
                                data[y][x] = bufferedReader.readLine();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                            System.out.print(data[y][x]);
                        }
                    }
                    System.out.println();

                }

                frameEnd.dispose();

                GamesList g = new GamesList(socket, pid, data);
                Thread t = new Thread(g);
                t.start();

                b=false;
                break;


                case "Shutdown":
                    socket.close();
                    frameEnd.dispose();
                    b=false;
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

        }
    }






}








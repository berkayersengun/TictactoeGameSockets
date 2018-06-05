import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Login implements Runnable{
    private JPanel panel;
    private JFrame frame;
    private JButton buttonLogin, buttonClear;
    private JLabel labelUsername, labelPass,labelLogin;
    private JTextField textFieldUsername, textFieldPass ;
    private String username,password;

    private Socket socket ;
    private InputStream is ;
    private ObjectInputStream ois;
    private OutputStream os;
    private ObjectOutputStream oos;
    private PrintWriter printWriter;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;


    public Login(Socket socket) throws IOException {

        this.socket = socket ;


        os = socket.getOutputStream();
        printWriter = new PrintWriter(os);

        //oos = new ObjectOutputStream(os);

        is = socket.getInputStream();
        inputStreamReader = new InputStreamReader(is);
        bufferedReader = new BufferedReader(inputStreamReader);


        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel = new JPanel(grid);
        panel.setBackground(Color.GRAY);
        panel.setVisible(true);

        buttonLogin = new JButton("Login");
        buttonLogin.addActionListener(new loginListener());
        buttonClear = new JButton("Clear");
        buttonClear.addActionListener(new clearListener());


        labelLogin = new JLabel("Login");
        labelUsername = new JLabel("Username");
        labelPass = new JLabel("Password");
        textFieldUsername = new JTextField(15);
        textFieldPass = new JTextField(15);

        c.insets = new Insets(3,3,3,3);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 1;
        panel.add(labelLogin,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx =0;
        c.gridy =1;
        panel.add(labelUsername,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2 ;

        panel.add(textFieldUsername,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1 ;
        panel.add(labelPass,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2 ;
        panel.add(textFieldPass,c);


        c.fill = GridBagConstraints.CENTER;
        c.ipadx = 10;
        c.gridy = 3 ;
        c.gridx =1 ;
        c.gridwidth = 1 ;
        panel.add(buttonLogin,c);

        c.fill = GridBagConstraints.CENTER;
        c.ipadx = 10;
        c.gridy = 3 ;
        c.gridx =2  ;
        c.gridwidth = 1 ;
        panel.add(buttonClear,c);

        frame= new JFrame("Login Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(300,175);
        frame.add(panel);
        frame.setVisible(true);

    }
    private class loginListener implements ActionListener  {
        public void actionPerformed(ActionEvent e ){

            username = textFieldUsername.getText();
            password = textFieldPass.getText();

            //send flag
            printWriter.println("Login");

            //send user credentials
            printWriter.println(username);
            printWriter.println(password);


            printWriter.flush();

        }


    }

    private class clearListener implements ActionListener{
        public void actionPerformed(ActionEvent e ){

            textFieldUsername.setText("");
            textFieldPass.setText("");


        }

    }

    public void run(){

        boolean b = true ;

        while (b) {

            try{

            int player = 0;
            String flag = bufferedReader.readLine();
            System.out.println(flag);

                String[][] data  ;
                String dataSize  ;


            switch (flag) {
                case "Login":
                    System.out.println("in login");
                    player = Integer.parseInt(bufferedReader.readLine());
                    System.out.println("received " + player);

                    //read the size first


                    dataSize = bufferedReader.readLine();
                    System.out.println(dataSize);


                    //String[][] data = listenGames(Integer.parseInt(dataSize));

                    data = new String[Integer.parseInt(dataSize)][3];

                    for (int y = 0; y < data.length; y++) {
                        for (int x = 0; x < 3; x++) {

                            if (x == 2) {
                                data[y][x] = "Join";
                            } else {
                                data[y][x] = bufferedReader.readLine();

                                System.out.print(data[y][x]);
                            }
                        }
                        System.out.println();

                    }


                    if (player == 0) {
                        System.out.println("username or password incorrect");

                    } else if (player == (-1) || player == (-2)) {
                        System.out.println("database connection problem");

                    } else  {
                        System.out.println("User loggedin with id number of " + player);
                        try {

                            GamesList g = new GamesList(socket, player, data);
                            Thread t = new Thread(g);
                            t.start();
                            frame.dispose();
                            b = false;
                            break;

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
            }

        }catch (IOException eve ){
            eve.printStackTrace();

            }
        }

}


}

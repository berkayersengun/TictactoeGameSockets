import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Registration implements Runnable {

    private JPanel panel;
    private JFrame frame;
    private JButton buttonSubmit,buttonClear;
    private JTextField fieldName,fieldSurname,fieldUsername,fieldPassword,fieldEmail ;
    private JLabel labelName,labelSurname,labelUsername,labelPassword,labelEmail,labelRegistration;
    private String name,surname,username,password,email;

    private Socket socket ;
    private InputStream is ;
    private ObjectInputStream ois;
    private OutputStream os;
    private ObjectOutputStream oos;
    private PrintWriter printWriter;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;



    public Registration(Socket sock) throws IOException {

        //network

        this.socket = sock ;


        os = socket.getOutputStream();
        printWriter = new PrintWriter(os);

        //oos = new ObjectOutputStream(os);

        is = socket.getInputStream();
        inputStreamReader = new InputStreamReader(is);
        bufferedReader = new BufferedReader(inputStreamReader);

        //ois = new ObjectInputStream(is);



        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel = new JPanel(grid);
        panel.setBackground(Color.GRAY);
        panel.setVisible(true);

        labelRegistration = new JLabel("Registration");
        labelName = new JLabel("Name");
        labelSurname = new JLabel("Surname");
        labelUsername = new JLabel("Username");
        labelPassword = new JLabel("Password");
        labelEmail = new JLabel("Email");

        fieldName = new JTextField(15);
        fieldSurname = new JTextField(15);
        fieldUsername = new JTextField(15);
        fieldPassword = new JTextField(15);
        fieldEmail = new JTextField(15);

        buttonSubmit = new JButton("Submit");
        buttonSubmit.addActionListener(new submitListener());

        buttonClear = new JButton( "Clear");
        buttonClear.addActionListener(new clearListener());

        c.insets = new Insets(3,8,3,8);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1 ;
        c.gridy = 0 ;
        c.ipady =10;
        panel.add(labelRegistration,c);

        c.ipady =0;
        c.ipadx = 20 ;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(labelName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        panel.add(fieldName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(labelSurname,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        panel.add(fieldSurname,c);

        c.ipady =0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(labelUsername,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        panel.add(fieldUsername,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        panel.add(labelPassword,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        panel.add(fieldPassword,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        panel.add(labelEmail,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 5;
        panel.add(fieldEmail,c);

        c.fill = GridBagConstraints.CENTER;
        //c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 15;
        c.gridy = 6 ;
        c.gridx = 1 ;
        c.gridwidth =1;
        panel.add(buttonSubmit,c);

        c.fill = GridBagConstraints.CENTER;
        c.ipadx = 15;
        c.gridy = 6 ;
        c.gridx = 2  ;
        c.gridwidth = 1 ;
        panel.add(buttonClear,c);


        frame.setLocationRelativeTo(null);
        frame = new JFrame("Registration Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400,300);
        frame.add(panel);

    }
    private class submitListener implements ActionListener {
        public void actionPerformed(ActionEvent event ) {

            name = fieldName.getText();
            surname = fieldSurname.getText();
            username = fieldUsername.getText();
            password = fieldPassword.getText();
            email = fieldEmail.getText();

            //send flag
            printWriter.println("Registration");

            printWriter.println(name);
            printWriter.println(surname);
            printWriter.println(username);
            printWriter.println(password);
            printWriter.println(email);
            printWriter.flush();





        }

    }

    private class clearListener implements ActionListener{
        public void actionPerformed(ActionEvent e ){

            fieldName.setText("");
            fieldSurname.setText("");
            fieldUsername.setText("");
            fieldPassword.setText("");
            fieldEmail.setText("");

        }

    }

    public void run(){
        boolean b = true ;

        while (b) {



                try {

                    String flag = bufferedReader.readLine();
                    int check = 0 ;
                    switch (flag) {
                        case "Registration":
                             check = Integer.parseInt(bufferedReader.readLine());
                             break;
                    }

                        if (check == 0) {
                            System.out.println("credentials incorrect");

                        } else if (check == (-1) || check == (-2)) {
                            System.out.println("database connection problem");

                        } else {
                            System.out.println("User added with id number of " + check);
                            Login log =  new Login(socket);
                            Thread t = new Thread(log);
                            t.start();
                            System.out.println(socket);
                            frame.dispose();
                            b=false;

                        }

                } catch (IOException e) {
                    e.printStackTrace();
                }



        }



    }



}

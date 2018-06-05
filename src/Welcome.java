import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;


public class Welcome  {

    private JPanel panel;
    private JFrame frame ;
    private JButton newButton;
    private JButton existingButton;
    private JLabel label;

    private Socket socket;
    private InputStream is ;
    private ObjectInputStream ois;
    private OutputStream os;
    private ObjectOutputStream oos;


    public Welcome() {


        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel = new JPanel(grid);
        panel.setBackground(Color.GRAY);
        panel.setVisible(true);
        label= new JLabel("Tic Tac Toe");

        newButton = new JButton("New User");
        newButton.addActionListener(new newUserListener());

        existingButton = new JButton("Existing User");
        existingButton.addActionListener(new existingUserListener());
        c.insets = new Insets(2,2,2,2);

        c.gridx = 0 ;
        c.ipady = 10 ;
        c.gridy = 0;
        panel.add(label,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.ipady = 6 ;
        panel.add(newButton,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 2;
        panel.add(existingButton,c);

        c.gridy = 4;


        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(200,175);
        frame.add(panel);
        frame.setLocationRelativeTo(null);




    }

    private void setupNetwork() throws IOException, ClassNotFoundException {

        socket = new Socket("localhost", 5555);


//        os = socket.getOutputStream();
//        oos = new ObjectOutputStream(os);
//
//        is = socket.getInputStream();
//        ois = new ObjectInputStream(is);

        System.out.println("connection established");
        System.out.println(socket);


    }

     private class newUserListener implements ActionListener{
        public void actionPerformed(ActionEvent e ){
            frame.dispose();
            try {
                Registration reg =  new Registration(socket);

                JOptionPane.showMessageDialog(frame,"Eggs are not supposed to be green.","Game",JOptionPane.PLAIN_MESSAGE);


               Thread t = new Thread(reg);
                t.start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(socket);
        }

    }

    private class existingUserListener implements ActionListener{
        public void actionPerformed(ActionEvent e ){
            frame.dispose();
            try {
                Login log = new Login(socket);
                Thread t = new Thread(log);
                t.start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    public static void main (String[] arg) throws IOException, ClassNotFoundException {

       new Welcome().setupNetwork();




    }
}

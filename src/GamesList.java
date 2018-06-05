import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class GamesList implements Runnable{
    private JFrame frame ;
    private JPanel panel, panel1,panel2;
    private JLabel label ;
    private JButton buttonCreate,buttonRefresh ;

    private Socket socket;
    private InputStream is ;
    private OutputStream os;
    private PrintWriter printWriter;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private String dataSize;
    private String[][] data;
    private JTable table;
    private JScrollPane scrollPane;
    private String[][] d;
    private int pid,side,opponent;
    private int gid;
    private  boolean b ;



    public GamesList(Socket socket,int pid, String[][] data) throws IOException {

        System.out.println("gamelist cosntructor");
        b=true;
       synchronized (this) {
           this.pid = pid;
           this.data = data;

           //setup network
           this.socket = socket;

           os = socket.getOutputStream();
           printWriter = new PrintWriter(os);

           //oos = new ObjectOutputStream(os);

           is = socket.getInputStream();
           inputStreamReader = new InputStreamReader(is);
           bufferedReader = new BufferedReader(inputStreamReader);
       }

        frame = new JFrame();
        panel = new JPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();
        label = new JLabel("Available games to join - Player" +pid );

        buttonCreate = new JButton("New Game");
        buttonCreate.addActionListener(new createListener());
        buttonRefresh = new JButton("Refresh");
        buttonRefresh.addActionListener(new refreshListener());

        panel1.add(label);
        panel2.add(buttonCreate);
        panel2.add(buttonRefresh);

        GridLayout grid = new GridLayout(1, 0);
        panel.setLayout(grid);

        String[] column = {"Game Number", "Player Number" , "Join"};

         DefaultTableModel dm = new DefaultTableModel();
         dm.setDataVector(data,column);
         table = new JTable(dm);
         scrollPane = new JScrollPane(table);



       // table.getColumn("Join").setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());;
        table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor());

       // table.getColumn()
        panel.add(scrollPane);

        frame.add(BorderLayout.CENTER, panel);
        frame.add(BorderLayout.NORTH,panel1);
        frame.add(BorderLayout.SOUTH,panel2);
        frame.setLocationRelativeTo(null);
        frame.setSize(500,500);
        frame.setVisible(true);

    }
    private class ButtonRenderer extends JButton implements  TableCellRenderer
    {

        public ButtonRenderer() {
            //SET BUTTON PROPERTIES
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj,
                                                       boolean selected, boolean focused, int row, int col) {

            //SET PASSED OBJECT AS BUTTON TEXT
            setText((obj==null) ? "":obj.toString());

            return this;
        }

    }
    private class ButtonEditor extends AbstractCellEditor implements ActionListener, TableCellEditor {
        private JButton btn;
        private String label;
        private int y;


        public ButtonEditor() {

            btn=new JButton();
            btn.setOpaque(true);

            //WHEN BUTTON IS CLICKED
            btn.addActionListener(this) ;

        }
        @Override
        public void actionPerformed(ActionEvent e) {


                //y is selected row when button fired at getTableCellEditorComponent
                gid = Integer.parseInt(data[y][0]);
                opponent = Integer.parseInt(data [y][1]);
                //System.out.println(data[y][0]);
                //player entered a game as second player playing with 'O'
                //new Game( socket,pid,opponent,"O",gid);
               /// b=false;
               // frame.dispose();
                //System.out.println(b);

            printWriter.println("Join");

            printWriter.println(gid);
            printWriter.println(pid);



            printWriter.flush();


            fireEditingStopped();
        }



        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            label=(value==null) ? "":value.toString();
            btn.setText(label);

            //y is selected row when button fired
            y= row;


            return btn;
        }

        @Override
        public Object getCellEditorValue() {
            return new String(label);
        }


    }


    private class createListener implements ActionListener {
        public void actionPerformed(ActionEvent e ){



                //player created a game as first player playing with 'X'

                printWriter.println("New");
                printWriter.println(pid);
            System.out.println(pid);
                printWriter.flush();


        }

    }


    private class refreshListener implements ActionListener {
        public void actionPerformed(ActionEvent e ){


            //send flag
            printWriter.println("Refresh");
            printWriter.flush();

        }

    }

@Override
    public void run() {

        while (b){

            try{
                String flag = bufferedReader.readLine();
                switch (flag) {

                    case "Refresh":
                        System.out.println("in refresh ");
                        //read the size first
                        try {

                            dataSize = bufferedReader.readLine();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        System.out.println(" data size is " + dataSize);

                        //String[][] data = listenGames(Integer.parseInt(dataSize));

                        data = new String[Integer.parseInt(dataSize)][3];

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
                        String[] column = {"Game Number", "Player Number" , "Join"};

                        DefaultTableModel dm = new DefaultTableModel();
                        dm.setDataVector(data,column);

                        System.out.println("refreshed table");
                        table.setModel(dm);
                        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());;
                        table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor());


                        table.repaint();

                        break;

                    case "Join" :

                        Game join = new Game(socket,pid,opponent,"O",gid);

                        Thread t = new Thread(join);
                        t.start();

                        frame.dispose();
                        b= false;

                        break;

                    case "New":

                        gid = Integer.parseInt(bufferedReader.readLine());

                        frame.dispose();

                        b= false ;

                        Game newGame =  new Game(socket,pid,"X",gid);
                        Thread t1 = new Thread(newGame);
                        t1.start();


                       //System.out.println(b);
                        break;

                }

            }catch (IOException eve ){
                eve.printStackTrace();

            }




        }




    }
}

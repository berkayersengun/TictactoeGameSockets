import java.io.Serializable;

public class Move implements Serializable{
    int move ;
    int pID ;
    int gID ;
    String text;




    Move (String text) { this.text = text; }

    Move(int move){
        this.move = move;
    }

    public String getText() {
        return text;
    }


    public int getMove (){
        return this.move;
    }

}




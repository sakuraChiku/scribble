
import java.util.ArrayList;
import scribble.models.*;

public class Main {
    public static void main(String[] args) throws Exception {
        TileBag bag = new TileBag();
        System.out.println(bag.tilesRemaining());
        ArrayList<Tile> rack = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Tile t = bag.drawTile();
            rack.add(t);
        }
        for (Tile tile : rack) {
            System.out.print(tile.getLetter() + " " + tile.getScore() + " , ");
        }
        System.out.println();
        System.out.println(bag.tilesRemaining());
    }
}
// this is a notation made by YYC
// this is a notation made by LJY
// this is a notation made by YYC to test the changes in visual code
// made by LJY
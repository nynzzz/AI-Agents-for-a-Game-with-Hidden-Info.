package sneakthrough.GUI;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sneakthrough.Main;

public class Tile extends Rectangle{

        private Pawn pawn;

        public boolean hasPiece() {
            return pawn != null;
        }

        public Pawn getPiece() {
            return pawn;
        }

        public void setPiece(Pawn pawn) {
            this.pawn = pawn;
        }

        public Tile(boolean light, int x, int y) {
            setWidth(MainScreen.TILE_SIZE);
            setHeight(MainScreen.TILE_SIZE);

            relocate(x * MainScreen.TILE_SIZE, y * MainScreen.TILE_SIZE);

            if (light) {
                setFill(Color.valueOf("#f2e1c3"));
            } else {
                setFill(Color.valueOf("#c3a082"));
            }
        }
    }


package sneakthrough.GUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.HumanPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

//TODO
// fill in empty spaces next to the board with e.g. history of movements or anything really useful for the user
// fix movement forward and diagonally, change turn automated

public class MainScreen extends Application {

    Player whitePlayer ;
    Player blackPlayer ;
    Circle pawn;
    Board mainBoard = new Board() ;
    Rectangle[][] chessMatrix = new Rectangle[8][8] ;
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    final double screenWidth = primaryScreenBounds.getWidth();
    final double screenHeight = primaryScreenBounds.getHeight();
    int boardSize = mainBoard.getSize() ;
    public static final int TILE_SIZE = 100;
    public static final int measure = 8 ;
    private Tile[][] chessBoard = new Tile[measure][measure] ;

    private Group tileGroup = new Group();

    private Group pawnGroup = new Group();

    private Group gameGroup = new Group();

    private Pane createChessboard()
    {
        Pane root = new Pane();

        root.setLayoutX(screenWidth/2 - 350);
        root.setLayoutY(screenHeight/2 - 350);

        root.setPrefSize(measure*TILE_SIZE, measure * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pawnGroup) ;

        for(int i = 0 ; i < measure ; i++)
            for(int j = 0 ; j < measure ; j++)
            {
                Tile tile = new Tile((i+j)% 2 == 0, i ,j) ;
                chessBoard[i][j] = tile ;

                tileGroup.getChildren().addAll(tile) ;

                Pawn pawn = null ;

                if(j <= 1) pawn = makePawn(PawnColor.BLACK, i , j);

                if (j >= 6) pawn = makePawn(PawnColor.WHITE, i, j);


                if (pawn != null) {
                    tile.setPiece(pawn);
                    pawnGroup.getChildren().add(pawn);
                }

            }

        return root;

    }


    private int toBoard(double pixel) {
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    private Pawn makePawn(PawnColor color, int x, int y)
    {
        Pawn pawn = new Pawn(color, x, y);

        pawn.setOnMouseReleased(e -> {

            int newX = toBoard(pawn.getLayoutX());
            int newY = toBoard(pawn.getLayoutY());

            MoveResult result;

            if (newX < 0 || newY < 0 || newX >= measure || newY >= measure) {
                result = new MoveResult(MoveType.NONE);
            } else {
                result = tryMove(pawn, newX, newY);
            }

            int x0 = toBoard(pawn.getOldX());
            int y0 = toBoard(pawn.getOldY());

            switch (result.getType()) {
                case NONE:
                    pawn.abortMove();
                    break;
                case NORMAL:
                    pawn.move(newX, newY);
                    chessBoard[x0][y0].setPiece(null);
                    chessBoard[newX][newY].setPiece(pawn);
                    break;
                case KILL:
                    pawn.move(newX, newY);
                    chessBoard[x0][y0].setPiece(null);
                    chessBoard[newX][newY].setPiece(pawn);

                    Pawn otherPawn = result.getPawn();
                    chessBoard[toBoard(otherPawn.getOldX())][toBoard(otherPawn.getOldY())].setPiece(null);
                    pawn.getChildren().remove(otherPawn);
                    break;
            }
        });

        return pawn;
    }

    private MoveResult tryMove(Pawn piece, int newX, int newY) {

        if (chessBoard[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NONE);
        }

        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getColor().moveDirection) {
            return new MoveResult(MoveType.NORMAL);
        } else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getColor().moveDirection * 2) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (chessBoard[x1][y1].hasPiece() && chessBoard[x1][y1].getPiece().getColor() != piece.getColor()) {
                return new MoveResult(MoveType.KILL, chessBoard[x1][y1].getPiece());
            }
        }

        return new MoveResult(MoveType.NONE);
    }

    @Override
    public void start(Stage stage) throws Exception{

        stage.setTitle("Sneakthrough game - Group 6");
        Group mainMenuGroup = new Group();

        Scene mainMenu = new Scene(mainMenuGroup, screenWidth, screenHeight, true);
        mainMenu.setFill(Color.LIGHTBLUE);
        mainMenu.setRoot(mainMenuGroup);

        //labels for main menu
        Label gameName = new Label("Sneakthrough");
        //gameName.setPrefSize(300, 100);
        gameName.setTextAlignment(TextAlignment.CENTER);

        Label groupNumber = new Label("Made by Group 6");
       // groupNumber.setPrefSize(300, 80); // Changed from gameName.setPrefSize
        groupNumber.setTextAlignment(TextAlignment.CENTER);

        // Define the Font here
        Font font = Font.font("Helvetica Neue", FontWeight.NORMAL, 30);
        gameName.setFont(font);
        groupNumber.setFont(font);

        gameName.setTextFill(Color.BLACK);
        groupNumber.setTextFill(Color.BLACK);

        gameName.setLayoutX(screenWidth / 2 -100);
        gameName.setLayoutY(screenHeight/6);

        groupNumber.setLayoutX(screenWidth / 2 - 100);
        groupNumber.setLayoutY(screenHeight/6 + 100);

        Label whitePlayerLabel = new Label("White player");
        whitePlayerLabel.setLayoutX(screenWidth / 2 - 250);
        whitePlayerLabel.setLayoutY(screenHeight/2 - 30);
        whitePlayerLabel.setTextAlignment(TextAlignment.CENTER);
        whitePlayerLabel.setFont(Font.font("Helvetica Neue", FontWeight.NORMAL, 15));

        Label blackPlayerLabel = new Label("Black player");
        blackPlayerLabel.setLayoutX(screenWidth/2 + 50);
        blackPlayerLabel.setLayoutY(screenHeight/2 - 30);
        blackPlayerLabel.setTextAlignment(TextAlignment.CENTER);
        blackPlayerLabel.setFont(Font.font("Helvetica Neue", FontWeight.NORMAL, 15));


        ObservableList<String> playerOptions =
                FXCollections.observableArrayList("Human", "Random Bot");

        ComboBox whitePlayerCB = new ComboBox(playerOptions);
        whitePlayerCB.setPrefWidth(200);
        whitePlayerCB.setLayoutX(screenWidth/2 - 250);
        whitePlayerCB.setLayoutY(screenHeight/2);

        ComboBox blackPlayerCB = new ComboBox(playerOptions);
        blackPlayerCB.setPrefWidth(200);
        blackPlayerCB.setLayoutX(screenWidth/2 + 50);
        blackPlayerCB.setLayoutY(screenHeight/2);

        Button startGameButton = new Button("Start game");
        startGameButton.setPrefWidth(200);
        startGameButton.setLayoutX(screenWidth/2 - 100);
        startGameButton.setLayoutY(screenHeight/2 + 100);

        Scene gameScene = new Scene(gameGroup,screenWidth,screenHeight);
        gameGroup.getChildren().addAll(createChessboard()) ;

        startGameButton.setOnAction(e ->
        {
            if (whitePlayerCB.getValue() == "Human") whitePlayer = new HumanPlayer("white") ;
            else whitePlayer = new RandomPlayer("white");

            if (blackPlayerCB.getValue() == "Human") blackPlayer = new HumanPlayer("black") ;
            else blackPlayer = new RandomPlayer("black");

            stage.setScene(gameScene);
        });

        mainMenuGroup.getChildren().addAll(gameName,groupNumber,blackPlayerCB,whitePlayerCB,whitePlayerLabel,blackPlayerLabel, startGameButton) ;

        stage.setScene(gameScene);
        stage.setFullScreen(true);
        stage.show();
    }

}
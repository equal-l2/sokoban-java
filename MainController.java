import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.io.IOException;

public class MainController {
  @FXML Button resetBtn;
  @FXML Button prevBtn;
  @FXML Button nextBtn;
  @FXML GridPane field;

  ImageView[] resetImgs = new ImageView[3];
  ImageView[] prevImgs = new ImageView[3];
  ImageView[] nextImgs = new ImageView[3];
  Image[] playerImgs = new Image[4];
  Image[] tiles = new Image[6];

  int[][] map;
  int playerX;
  int playerY;
  Direction playerDir;


  // stage objects are described as bit flags.
  // 0b0000 (0): empty
  // 0b0001 (1): destination
  // 0b0010 (2): player
  // 0b0011 (3): player on dest.
  // 0b0100 (4): crate
  // 0b0101 (5): crate on dest.
  // 0b0110 (6): (not in use)
  // 0b0111 (7): (not in use)
  // 0b1000 (8): wall
  private final int EMPTY  = 0b0000;
  private final int DEST   = 0b0001;
  private final int PLAYER = 0b0010;
  private final int CRATE  = 0b0100;
  private final int WALL   = 0b1000;

  enum Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT
  }

  @FXML
  void initialize() {
    resetImgs[0] = new ImageView("data/button/reset_disabled.png");
    resetImgs[1] = new ImageView("data/button/reset_down.png");
    resetImgs[2] = new ImageView("data/button/reset_up.png");
    prevImgs[0] = new ImageView("data/button/prev_disabled.png");
    prevImgs[1] = new ImageView("data/button/prev_down.png");
    prevImgs[2] = new ImageView("data/button/prev_up.png");
    nextImgs[0] = new ImageView("data/button/next_disabled.png");
    nextImgs[1] = new ImageView("data/button/next_down.png");
    nextImgs[2] = new ImageView("data/button/next_up.png");
    playerImgs[0] = new Image("data/player/player1.png");
    playerImgs[1] = new Image("data/player/player2.png");
    playerImgs[2] = new Image("data/player/player3.png");
    playerImgs[3] = new Image("data/player/player4.png");
    tiles[0] = new Image("data/tile/floor.png");
    tiles[1] = new Image("data/tile/dest.png");
    tiles[2] = new Image("data/tile/crate.png");
    tiles[3] = new Image("data/tile/wall.png");
    tiles[4] = new Image("data/tile/crate_on_dest.png");
    tiles[5] = new Image("data/tile/error.png");
    configureBtn(resetBtn, resetImgs);
    configureBtn(prevBtn, prevImgs);
    configureBtn(nextBtn, nextImgs);
    Platform.runLater(() -> {
      resetBtn.getScene().setOnKeyPressed(this::handleKey);
    });
    try {
      loadMap(1);
    } catch (IOException e) {
      e.printStackTrace();
      Platform.exit();
    }
  }

  private void loadMap(int n) throws IOException {
    Path p = Paths.get("data/map/"+n+".map");
    int[][] new_map = Files.lines(p).map( (s) ->
      s.chars().map(Character::getNumericValue).toArray()
    ).toArray(int[][]::new);
    map = new_map;

player_search:
    for (int x = 0; x < map[0].length; x++) {
      for (int y = 0; y < map.length; y++) {
        if ((map[y][x] & PLAYER) != 0) {
          playerX = x;
          playerY = y;
          break player_search;
        }
      }
    }

    playerDir = Direction.UP;

    draw();
  }

  private void draw() {
    field.getChildren().removeAll();
    field.setMinSize(0, 0);
    for (int x = 0; x < map[0].length; x++) {
      for (int y = 0; y < map.length; y++) {
        int val = map[y][x];
        Image img = tiles[5];
        if ((val & PLAYER) != 0) {
          switch (playerDir) {
            case UP:
              img = playerImgs[0];
              break;
            case DOWN:
              img = playerImgs[1];
              break;
            case RIGHT:
              img = playerImgs[2];
              break;
            case LEFT:
              img = playerImgs[3];
              break;
          }
        } else {
          switch (map[y][x]) {
            case EMPTY:
              img = tiles[0];
              break;
            case DEST:
              img = tiles[1];
              break;
            case CRATE:
              img = tiles[2];
              break;
            case WALL:
              img = tiles[3];
              break;
            case (CRATE | DEST):
              img = tiles[4];
              break;
            default:
              img = tiles[5];
          }
        }
        field.add(new ImageView(img), x, y);
      }
    }
  }

  private void handleKey(KeyEvent k) {
    switch (k.getCode()) {
      case UP :
        tryMove(Direction.UP);
        break;
      case DOWN:
        tryMove(Direction.DOWN);
        break;
      case RIGHT :
        tryMove(Direction.RIGHT);
        break;
      case LEFT :
        tryMove(Direction.LEFT);
        break;
    }
  }

  private void tryMove(Direction d) {
  }

  private void configureBtn(Button b, ImageView[] imgs) {
    b.setGraphic(imgs[2]);
    b.setOnMousePressed((m) -> {
      if(m.getButton() == MouseButton.PRIMARY) {
        b.setGraphic(imgs[1]);
      }
    });
    b.setOnMouseReleased( (MouseEvent m) -> {
      if(m.getButton() == MouseButton.PRIMARY) {
        b.setGraphic(imgs[2]);
      }
    });
  }
}
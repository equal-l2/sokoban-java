import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.application.Application;

public class App extends Application {
  public static void main(String[] args) {
    launch(args);
  }
  @Override
  public void start(Stage stg) throws IOException {
    Scene scn = new Scene(FXMLLoader.load(getClass().getResource("data/sokoban.fxml")));
    stg.setScene(scn);
    stg.show();
  }
}

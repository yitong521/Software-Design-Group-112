import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloWorld extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个 Text 对象
        Text helloText = new Text("Hello, JavaFX!");

        // 创建一个 StackPane，并将 Text 放置在其中
        StackPane root = new StackPane();
        root.getChildren().add(helloText);

        // 创建一个 Scene，并将 StackPane 放置在其中
        Scene scene = new Scene(root, 300, 200);

        // 将 Scene 设置到 Stage 上，并设置 Stage 的标题
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Hello World");

        // 显示 Stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
static boolean answer;
	public static boolean Confirm(String Title, String Message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(Title);
		window.setMinWidth(350);
		window.setMinHeight(140);
		Label label = new Label();
		label.setText(Message);

		Button yesButton = new Button("yes");
		Button noButton = new Button("no");
		
		yesButton.setOnAction(e->{
		answer = true;
		window.close();
		});
		
		noButton.setOnAction(e->{
		answer = false;
		window.close();
		});
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, yesButton, noButton);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		return answer;
		
	
	}
}
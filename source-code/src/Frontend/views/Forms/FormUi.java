package Frontend.views.Forms;

import javafx.scene.Parent;
import javafx.stage.Stage;

public interface FormUi {

    Parent getLayout(Stage stage);

    boolean validate();

    void submit();
}
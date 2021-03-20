package quinzical.design;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class QuinzicalTooltip extends Tooltip{

    /**
     * Our tooltip that is used over buttons/icons to explain their function.
     * @param text
     */
    public QuinzicalTooltip(String text) {
        //Set text of tooltip and make the time to display 0.1seconds after the user has hovered over the object.
        setText(text);
        setShowDelay(Duration.seconds(0.1));
    }
}

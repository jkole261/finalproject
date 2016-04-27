/**/
/** AutoComboBox.java
 * 
 * @author Jason Kole
 * 
 * The AutoComboBox takes in a ComboBox and will filter out results as the user inputs
 * test into the box. if no results with inputed text then the box will be empty.
 **/
/**/
package edu.ramapo.jkole.cad;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AutoComboBox implements EventHandler<KeyEvent> {

    private ComboBox<Nature> comboBox;
    private StringBuilder sb;
    private ObservableList<Nature> data;
    private boolean moveCaretToPos = false;
    private int cPos;

    public AutoComboBox(final ComboBox<Nature> comboBox) {
        this.comboBox = comboBox;
        setSb(new StringBuilder());
        data = comboBox.getItems();

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                comboBox.hide();
            }
        });
        this.comboBox.setOnKeyReleased(AutoComboBox.this);
    }

	@Override
    public void handle(KeyEvent event) {

        if(event.getCode() == KeyCode.UP) {
            cPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            cPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            cPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getCode() == KeyCode.DELETE) {
            moveCaretToPos = true;
            cPos = comboBox.getEditor().getCaretPosition();
        }
 
        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.getCode() == KeyCode.TAB) {
            return;
        }
        
        ObservableList<Nature> list = FXCollections.observableArrayList();
        for (int i=0; i<data.size(); i++) {
            if(data.get(i).toString().toLowerCase().startsWith(
                AutoComboBox.this.comboBox
                .getEditor().getText().toLowerCase())) {
                list.add(data.get(i));
            }
        }
        String t = comboBox.getEditor().getText();

        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            cPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            comboBox.show();
        }
    }

    private void moveCaret(int textLength) {
        if(cPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(cPos);
        }
        moveCaretToPos = false;
    }

	public StringBuilder getSb() {
		return sb;
	}

	public void setSb(StringBuilder sb) {
		this.sb = sb;
	}

}
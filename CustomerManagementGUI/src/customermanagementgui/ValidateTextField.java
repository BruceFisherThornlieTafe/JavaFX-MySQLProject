package customermanagementgui;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Validations for FXML Text Fields - Full Seamless While Typing Text Field
 * Validation
 *
 * © Copyright - Deimos Coding Projects -> Programmer Bruce Fisher
 *
 * @author Email: deimoscodingprojects@gmail.com
 * 
 * Website: <a href="https://deimoscodingprojects.com/">Website</a>
 *
 * @author © Deimos Coding Projects -> Bruce Fisher
 *
 * Date 8-5-2022
 */
public class ValidateTextField {

    // Initialise Text Field used variables
    private int currentCursorCaretPostion = 0;
    private int oldCursorCaretPostion = 0;
    private String oldTextFieldContents = "";

    // Char codes used
    static final int BACK_SPACE = 8;
    static final int CTRL_V = 22;
    static final int DEL = 127;
    static final int ENTER = 13;
    static final int DECIMAL_POINT = 46;
    static final int AT_SYMBOL = 64; // @
    static final int SPACE = 32;

    /**
     * Validate TextField for Alphabetic only and limit to max input length.
     * Uses String manipulation to achieve the required effect.
     *
     * @author © Deimos Coding Projects
     *
     * @param textField - the Text Field to apply everything to
     * @param event - event to get current user typed char input
     * @param textFieldMaxInput - max allowed text field input
     * @return ErrorMessage - returned object containing Boolean error, String
     * errorMsg
     */
    public ErrorMessage validateLetter(TextField textField, KeyEvent event, int textFieldMaxInput) {
        // Initialise return Objects variables
        boolean error = false;
        String errorMsg = "";

        // Get user KeyTyped input as char
        char currentKeyTyped = event.getCharacter().charAt(0);

        // Conditions -> Check for Valid and In-Valid Key Press
        final Boolean PASTE_KEY = currentKeyTyped == CTRL_V; // If Paste Ctrl V pressed remove paste and show error message
        final Boolean INVALID_KEY
                = (!Character.isAlphabetic(currentKeyTyped) && currentKeyTyped != SPACE) // If NOT a letter pressed or SPACE remove key pressed and show error message
                && (currentKeyTyped != BACK_SPACE) // Allow and Pass thru if Back Space pressed
                && (currentKeyTyped != DEL) // Allow and Pass thru if Del pressed
                && (currentKeyTyped != ENTER) // Allow and Pass thru if Enter pressed
                && (!Character.isISOControl(currentKeyTyped)); // Allow and Pass thru if Ctrl + Key pressed

        // get current cursor position to place cursor back at same point 
        // after pasting back to Text Field manipulated String 
        currentCursorCaretPostion = textField.getCaretPosition();

        // Catch general exception
        try {
            // Check for invalid keys and remove
            if (PASTE_KEY) {
                error = true;
                errorMsg = "ERROR: Paste NOT allowed";

                textField.setText(oldTextFieldContents); // reset back to old contents
                textField.positionCaret(getOldCursorCaretPostion()); // place cursor at old postion where clicked with mouse
            } else if (INVALID_KEY) {
                String replaceText = textField.getText().replace(Character.toString(currentKeyTyped), ""); // remove key pressed

                error = true;
                errorMsg = "ERROR: Input must be a Althabetic";

                // paste back to Text Field manipulated String
                textField.setText(replaceText);

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Remove character at any point that makes input exceed textFieldMaxInput
            if (textField.getText().length() > textFieldMaxInput) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Max Number Input " + textFieldMaxInput;

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Variables used for reversing Paste
            oldTextFieldContents = textField.getText(); // store old Text Field contents for reversing Paste
            setOldCursorCaretPostion(textField.getCaretPosition()); // store current cursor position to place cursor back at same point

            return new ErrorMessage(error, errorMsg); // return object ErrorMessage
        } catch (Exception ex) {
            return new ErrorMessage(true, "ERROR: Exception Caught " + ex);
        }
    }

    /**
     * Validate TextField for Digits only and limit to max input length. Uses
     * String manipulation to achieve the required effect.
     *
     * @author © Deimos Coding Projects
     *
     * @param textField - the Text Field to apply everything to
     * @param event - event to get current user typed char input
     * @param textFieldMaxInput - max allowed text field input
     * @return ErrorMessage - returned object containing Boolean error, String
     * errorMsg
     */
    public ErrorMessage validateDigit(TextField textField, KeyEvent event, int textFieldMaxInput) {
        // Initialise return Objects variables
        boolean error = false;
        String errorMsg = "";

        // Get user KeyTyped input as char
        char currentKeyTyped = event.getCharacter().charAt(0);

        // Conditions -> Check for Valid and In-Valid Key Press
        final Boolean PASTE_KEY = currentKeyTyped == CTRL_V; // If Paste Ctrl V pressed remove paste and show error message
        final Boolean INVALID_KEY
                = (!Character.isDigit(currentKeyTyped)) // If NOT a number pressed remove key pressed and show error message
                && (currentKeyTyped != BACK_SPACE) // Allow and Pass thru if Back Space pressed
                && (currentKeyTyped != DEL) // Allow and Pass thru if Del pressed
                && (currentKeyTyped != ENTER) // Allow and Pass thru if Enter pressed
                && (!Character.isISOControl(currentKeyTyped)); // Allow and Pass thru if Ctrl + Key pressed

        // get current cursor position to place cursor back at same point 
        // after pasting back to Text Field manipulated String 
        currentCursorCaretPostion = textField.getCaretPosition();

        // Catch general exception
        try {
            if (PASTE_KEY) {
                error = true;
                errorMsg = "ERROR: Paste NOT allowed";

                textField.setText(oldTextFieldContents); // reset back to old contents
                textField.positionCaret(getOldCursorCaretPostion()); // place cursor at old postion where clicked with mouse
            } else if (INVALID_KEY) {
                String replaceText = textField.getText().replace(Character.toString(currentKeyTyped), ""); // remove key pressed

                error = true;
                errorMsg = "ERROR: Input must be a Number";

                // paste back to Text Field manipulated String
                textField.setText(replaceText);

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Remove character at any point that makes input exceed textFieldMaxInput
            if (textField.getText().length() > textFieldMaxInput) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Max Number Input " + textFieldMaxInput;

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Variables used for reversing Paste
            oldTextFieldContents = textField.getText(); // store old Text Field contents for reversing Paste
            setOldCursorCaretPostion(textField.getCaretPosition()); // store current cursor position to place cursor back at same point

            return new ErrorMessage(error, errorMsg); // return object ErrorMessage
        } catch (Exception ex) {
            return new ErrorMessage(true, "ERROR: Exception Caught " + ex);
        }
    }

    /**
     * Validate TextField for Digits and one decimal place only and limit to max
     * input length. Uses String manipulation to achieve the required effect.
     *
     * @author © Deimos Coding Projects
     *
     * @param textField - the Text Field to apply everything to
     * @param event - event to get current user typed char input
     * @param textFieldMaxInput - max allowed text field input
     * @return ErrorMessage - returned object containing Boolean error, String
     * errorMsg
     */
    public ErrorMessage validateDouble(TextField textField, KeyEvent event, int textFieldMaxInput) {
        // Initialise return Objects variables
        boolean error = false;
        String errorMsg = "";

        // Get user KeyTyped input as char
        char currentKeyTyped = event.getCharacter().charAt(0);

        // Conditions -> Check for Valid and In-Valid Key Press
        final Boolean PASTE_KEY = currentKeyTyped == CTRL_V; // If Paste Ctrl V pressed remove paste and show error message
        final Boolean INVALID_KEY
                = (!Character.isDigit(currentKeyTyped)) // If NOT a number pressed remove key pressed and show error message
                && (currentKeyTyped != BACK_SPACE) // Allow and Pass thru if Back Space pressed
                && (currentKeyTyped != DEL) // Allow and Pass thru if Del pressed
                && (currentKeyTyped != ENTER) // Allow and Pass thru if Enter pressed
                && (currentKeyTyped != DECIMAL_POINT) // Allow and Pass thru if Decimal Point pressed
                && (!Character.isISOControl(currentKeyTyped)); // Allow and Pass thru if Ctrl + Key pressed

        // get current cursor position to place cursor back at same point 
        // after pasting back to Text Field manipulated String 
        currentCursorCaretPostion = textField.getCaretPosition();

        // Catch general exception
        try {
            // If DECIMAL_POINT is pressed twice then remove DECIMAL_POINT at cursor carot postion entered
            if ((currentKeyTyped == DECIMAL_POINT) && (countStringMatches(textField.getText(), ".") > 1)) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Invalid Decimal Number";

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Check for invalid keys and remove
            if (PASTE_KEY) {
                error = true;
                errorMsg = "ERROR: Paste NOT allowed";

                textField.setText(oldTextFieldContents); // reset back to old contents
                textField.positionCaret(getOldCursorCaretPostion()); // place cursor at old postion where clicked with mouse
            } else if (INVALID_KEY) {
                String replaceText = textField.getText().replace(Character.toString(currentKeyTyped), ""); // remove key pressed

                error = true;
                errorMsg = "ERROR: Invalid Decimal Number";

                // paste back to Text Field manipulated String
                textField.setText(replaceText);

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Remove character at any point that makes input exceed textFieldMaxInput
            if (textField.getText().length() > textFieldMaxInput) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Max Number Input " + textFieldMaxInput;

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Variables used for reversing Paste
            oldTextFieldContents = textField.getText(); // store old Text Field contents for reversing Paste
            setOldCursorCaretPostion(textField.getCaretPosition()); // store current cursor position to place cursor back at same point

            return new ErrorMessage(error, errorMsg); // return object ErrorMessage
        } catch (Exception ex) {
            return new ErrorMessage(true, "ERROR: Exception Caught " + ex);
        }
    }

    /**
     * Validate TextField Email and limit to max input length. Uses String
     * manipulation to achieve the required effect.
     *
     * @author © Deimos Coding Projects
     *
     * @param textField - the Text Field to apply everything to
     * @param event - event to get current user typed char input
     * @param textFieldMaxInput - max allowed text field input
     * @return ErrorMessage - returned object containing Boolean error, String
     * errorMsg
     */
    public ErrorMessage validateEmail(TextField textField, KeyEvent event, int textFieldMaxInput) {
        // Initialise return Objects variables
        boolean error = false;
        String errorMsg = "";

        // Get user KeyTyped input as char
        char currentKeyTyped = event.getCharacter().charAt(0);

        // Conditions -> Check for Valid and In-Valid Key Press
        final Boolean PASTE_KEY = currentKeyTyped == CTRL_V; // If Paste Ctrl V pressed remove paste and show error message
        final Boolean INVALID_KEY
                = (!Character.isDigit(currentKeyTyped)) // If NOT a number pressed remove key pressed and show error message
                && (!Character.isAlphabetic(currentKeyTyped)) // If NOT a letter pressed show error message
                && (currentKeyTyped != BACK_SPACE) // Allow and Pass thru if Back Space pressed
                && (currentKeyTyped != DEL) // Allow and Pass thru if Del pressed
                && (currentKeyTyped != ENTER) // Allow and Pass thru if Enter pressed
                && (currentKeyTyped != DECIMAL_POINT) // Allow and Pass thru if Decimal Point pressed
                && (currentKeyTyped != AT_SYMBOL) // Allow and Pass thru if @ pressed
                && (!Character.isISOControl(currentKeyTyped)); // Allow and Pass thru if Ctrl + Key pressed

        // get current cursor position to place cursor back at same point 
        // after pasting back to Text Field manipulated String 
        currentCursorCaretPostion = textField.getCaretPosition();

        // Catch general exception
        try {

            if (currentCursorCaretPostion != 1) {
                if ((currentKeyTyped == DECIMAL_POINT) && (textField.getText().charAt(currentCursorCaretPostion - 2) == AT_SYMBOL)) {

                    System.out.println(textField.getText().charAt(currentCursorCaretPostion - 1));
                    String textInput = textField.getText();

                    error = true;
                    errorMsg = "ERROR: Invalid Email Address";

                    // paste back to Text Field manipulated String
                    textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                    // place cursor back at current position less one as removed charatcer
                    textField.positionCaret(currentCursorCaretPostion - 1);
                }

                if ((currentKeyTyped == AT_SYMBOL) && (textField.getText().charAt(currentCursorCaretPostion - 2) == DECIMAL_POINT)) {

                    System.out.println(textField.getText().charAt(currentCursorCaretPostion - 1));
                    String textInput = textField.getText();

                    error = true;
                    errorMsg = "ERROR: Invalid Email Address";

                    // paste back to Text Field manipulated String
                    textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                    // place cursor back at current position less one as removed charatcer
                    textField.positionCaret(currentCursorCaretPostion - 1);
                }
            }

            if (textField.getText().length() > currentCursorCaretPostion) {
                if ((currentKeyTyped == DECIMAL_POINT) && (textField.getText().charAt(currentCursorCaretPostion) == AT_SYMBOL)) {

                    System.out.println(textField.getText().charAt(currentCursorCaretPostion - 1));
                    String textInput = textField.getText();

                    error = true;
                    errorMsg = "ERROR: Invalid Email Address";

                    // paste back to Text Field manipulated String
                    textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                    // place cursor back at current position less one as removed charatcer
                    textField.positionCaret(currentCursorCaretPostion - 1);
                }
            }

            if ((currentKeyTyped == DECIMAL_POINT) && (currentCursorCaretPostion == 1)) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Invalid Email Address";

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            if ((currentKeyTyped == AT_SYMBOL) && (currentCursorCaretPostion == 1)) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Invalid Email Address";

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // If AT_SYMBOL is pressed twice then remove AT_SYMBOL at cursor carot postion entered and don't allow two ..
            if (((currentKeyTyped == AT_SYMBOL) && (countStringMatches(textField.getText(), "@") > 1)) || (countStringMatches(textField.getText(), "..") > 0)) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Invalid Email Address";

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Check for invalid keys and remove
            if (PASTE_KEY) {
                error = true;
                errorMsg = "ERROR: Paste NOT allowed";

                textField.setText(oldTextFieldContents); // reset back to old contents
                textField.positionCaret(getOldCursorCaretPostion()); // place cursor at old postion where clicked with mouse
            }
            if (INVALID_KEY) {
                String replaceText = textField.getText().replace(Character.toString(currentKeyTyped), ""); // remove key pressed

                error = true;
                errorMsg = "ERROR: Invalid Decimal Number";

                // paste back to Text Field manipulated String
                textField.setText(replaceText);

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Remove character at any point that makes input exceed textFieldMaxInput
            if (textField.getText().length() > textFieldMaxInput) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Max Number Input " + textFieldMaxInput;

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Variables used for reversing Paste
            oldTextFieldContents = textField.getText(); // store old Text Field contents for reversing Paste
            setOldCursorCaretPostion(textField.getCaretPosition()); // store current cursor position to place cursor back at same point

            return new ErrorMessage(error, errorMsg); // return object ErrorMessage
        } catch (Exception ex) {
            return new ErrorMessage(true, "ERROR: Exception Caught " + ex);
        }
    }

    /**
     * Validate TextField for Digits only and limit to max input length. Uses
     * String manipulation to achieve the required effect.
     *
     * © Deimos Coding Projects
     *
     * @param textField - the Text Field to apply everything to
     * @param event - event to get current user typed char input
     * @param textFieldMaxInput - max allowed text field input
     * @return ErrorMessage - returned object containing Boolean error, String
     * errorMsg
     */
    public ErrorMessage validateMobile(TextField textField, KeyEvent event, int textFieldMaxInput) {
        // Initialise return Objects variables
        boolean error = false;
        String errorMsg = "";

        // Get user KeyTyped input as char
        char currentKeyTyped = event.getCharacter().charAt(0);

        // Conditions -> Check for Valid and In-Valid Key Press
        final Boolean PASTE_KEY = currentKeyTyped == CTRL_V; // If Paste Ctrl V pressed remove paste and show error message
        final Boolean INVALID_KEY
                = (!Character.isDigit(currentKeyTyped) && currentKeyTyped != SPACE) // If NOT a number pressed or SPACE remove key pressed and show error message
                && (currentKeyTyped != BACK_SPACE) // Allow and Pass thru if Back Space pressed
                && (currentKeyTyped != DEL) // Allow and Pass thru if Del pressed
                && (currentKeyTyped != ENTER) // Allow and Pass thru if Enter pressed
                && (!Character.isISOControl(currentKeyTyped)); // Allow and Pass thru if Ctrl + Key pressed

        // get current cursor position to place cursor back at same point 
        // after pasting back to Text Field manipulated String 
        currentCursorCaretPostion = textField.getCaretPosition();

        // Catch general exception
        try {
            if (PASTE_KEY) {
                error = true;
                errorMsg = "ERROR: Paste NOT allowed";

                textField.setText(oldTextFieldContents); // reset back to old contents
                textField.positionCaret(getOldCursorCaretPostion()); // place cursor at old postion where clicked with mouse
            } else if (INVALID_KEY) {
                String replaceText = textField.getText().replace(Character.toString(currentKeyTyped), ""); // remove key pressed

                error = true;
                errorMsg = "ERROR: Input must be a Number";

                // paste back to Text Field manipulated String
                textField.setText(replaceText);

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Remove character at any point that makes input exceed textFieldMaxInput
            if (textField.getText().length() > textFieldMaxInput) {
                String textInput = textField.getText();

                error = true;
                errorMsg = "ERROR: Max Number Input " + textFieldMaxInput;

                // paste back to Text Field manipulated String
                textField.setText(deleteCharInString(textInput, currentCursorCaretPostion - 1));

                // place cursor back at current position less one as removed charatcer
                textField.positionCaret(currentCursorCaretPostion - 1);
            }

            // Variables used for reversing Paste
            oldTextFieldContents = textField.getText(); // store old Text Field contents for reversing Paste
            setOldCursorCaretPostion(textField.getCaretPosition()); // store current cursor position to place cursor back at same point

            return new ErrorMessage(error, errorMsg); // return object ErrorMessage
        } catch (Exception ex) {
            return new ErrorMessage(true, "ERROR: Exception Caught " + ex);
        }
    }

    /**
     * Counts how many times searchStr is found in text
     *
     * Returns how many times the searchStr was found
     *
     * @param text
     * @param searchStr
     * @return found
     */
    public static int countStringMatches(String text, String searchStr) {
        int index = 0; // start search from beginning of String text
        int found = 0; // initialise found - if nothing found returns zero

        // while searchStr is found in text keep looking for next indexOf
        while ((index = text.indexOf(searchStr, index)) != -1) {
            found++;
            index += searchStr.length(); // skip over searchStr length to search new index location
        }

        return found;
    }

    /**
     * Removes Character from given Position from given String
     *
     * @param textInput - String to remove Char from
     * @param charPositon - Position to remove Char from String
     * @return
     */
    static private String deleteCharInString(String textInput, int charPositon) {
        StringBuilder str = new StringBuilder(textInput); // Constructor used to set Text Field string to delete char from
        StringBuilder limitTextInput = str.deleteCharAt(charPositon); // Perform delete at current cursor position less one as typed charatcer to remove pushed cursor along 1

        return limitTextInput.toString();
    }

    /**
     * @return the oldCursorCaretPostion
     */
    public int getOldCursorCaretPostion() {
        return oldCursorCaretPostion;
    }

    /**
     * @param oldCursorCaretPostion the oldCursorCaretPostion to set
     */
    public void setOldCursorCaretPostion(int oldCursorCaretPostion) {
        this.oldCursorCaretPostion = oldCursorCaretPostion;
    }
}

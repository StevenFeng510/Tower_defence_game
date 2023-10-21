package WizardTD;

// Import PApplet from the processing package

import processing.core.PApplet;

public class SidebarButton extends PApplet {
    int buttonX, buttonY;
    int mouseX, mouseY;
    int buttonWidth = 32, buttonHeight = 32;
    String buttonText;
    String buttonDescriptionText;
    boolean isHovered = false;
    boolean isPressed = false;


    SidebarButton(int buttonX, int buttonY, int mouseX, int mouseY, String buttonText, String buttonDescriptionText) {
        this.buttonX = buttonX;
        this.buttonY = buttonY;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.buttonText = buttonText;
        this.buttonDescriptionText = buttonDescriptionText;
    }

    public void setButtonDescriptionText(String buttonDescriptionText) {
        this.buttonDescriptionText = buttonDescriptionText;
    }

    public void draw(PApplet app) {
        // 128 128 128
        // setting of the button
        app.stroke(0);
        app.strokeWeight(2);
        if (isHovered) {
            app.fill(128, 128, 128); //ffff08 255 255 8
        } else if (isPressed) {
            app.fill(255, 255, 8);
        } else {
            app.noFill();
        }
        app.rect(buttonX, buttonY, 32, 32);

        // the text of Button
        app.fill(0);
        app.textSize(20);
        app.text(buttonText, buttonX + 3, buttonY + 24);

        // Description of Button
        if (!buttonDescriptionText.isEmpty()) {
            app.fill(0);
            app.textSize(13);
            app.text(buttonDescriptionText, buttonX + 40, buttonY + 12);
        }
    }

    public void isHoveredListener(int mouseX, int mouseY) {
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
            isHovered = true;
        } else {
            isHovered = false;
        }
    }

    public void isPressedListener() {
        isPressed = !isPressed;
    }

    public void isClickedListener(int mouseX, int mouseY) {
        if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
            isPressed = !isPressed;
        }
    }

    public void showManaCost(PApplet app, int manaCost) {
        app.stroke(0);
        app.strokeWeight(2);
        app.fill(255, 255, 255);
        app.rect(buttonX - 80, buttonY - 8, 63, 20);
        app.fill(0);
        app.text("Cost: " + manaCost, buttonX - 79, buttonY + 8);
    }

}

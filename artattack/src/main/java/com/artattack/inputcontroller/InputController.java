package com.artattack.inputcontroller;

public class InputController {
    private PlayerStrategy currentState;
    /* private MainFrame mainFrame; */
    

    public void setStrategy(PlayerStrategy strategy){
        this.currentState = strategy;
        updateFocus();

    }

    private void updateFocus(){
       switch (currentState.getType()) {
        case 0:
            /* this.mainFrame.setMapFocus(); */
            break;
        case 1:
              /* this.mainFrame.setMovesFocus(); */   
        default:
            break;
       }
    }
}

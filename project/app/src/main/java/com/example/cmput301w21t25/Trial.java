package com.example.cmput301w21t25;

public class Trial {

    /****************************************
                    ATTRIBUTES
     ****************************************/
    private boolean hidden = false;
    private User user;

    /****************************************
                CONSTRUCTORS
     ****************************************/
    public Trial(User user) {
        this.user = user;
    }

    /****************************************
                    METHODS
     ****************************************/

    public User getUser() {
        return user;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}

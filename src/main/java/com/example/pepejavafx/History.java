package com.example.pepejavafx;

public class History {
    private String[] history;

    public History() {
        history = new String[5];
    }

    public boolean check(String url) {
        for (int i = 0; i < 5; i++) {
            if (history[i] == url) {
                return true;
            }
        }
        return false;
    }

    // function to move url to index
    public void move(String url, int index) {
        for (int i = 0; i < 5; i++) {
            if (history[i] == url) {
                for (int j = i; j > index; j--) {
                    history[j] = history[j - 1];
                }
                history[index] = url;
            }
        }
    }

    public void save(String url) {
        // If url is already in history, just move it to the front
        if (check(url)) {
            move(url, 0);
            return;
        }
        // Otherwise, move everything back and add url to the front = 0
        for (int i = 4; i > 0; i--) {
            history[i] = history[i - 1];
        }
        history[0] = url;

        System.out.println("History:");
        for (int i = 0; i < 5; i++) {
            System.out.println(history[i]);
        }
    }

    public String[] getHistory() {
        return history;
    }

}

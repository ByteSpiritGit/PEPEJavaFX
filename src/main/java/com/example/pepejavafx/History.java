package com.example.pepejavafx;

import java.util.Objects;

public class History {
    private String[] history;

    public History() {
        history = new String[5];
    }

    public boolean check(String url) {
        for (String s : history) {
            if (Objects.equals(s, url)) {
                return true;
            }
        }
        return false;
    }

    // function to move url to index
    public void move(String url, int index) {
        for (int i = history.length-1; i < 5; i++) {
            if (Objects.equals(history[i], url)) {
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
        for (int i = history.length-1; i > 0; i--) {
            history[i] = history[i - 1];
        }
        history[0] = url;

        System.out.println("History:");
        for (String s : history) {
            System.out.println(s);
        }
    }

    public String[] getHistory() {
        return history;
    }

}

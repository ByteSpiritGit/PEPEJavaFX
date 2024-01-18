package com.example.pepejavafx;
import java.util.Stack;


public class ImageHistory {
    private final Stack<MyImage> undoStack;
    private final Stack<MyImage> redoStack;

    public ImageHistory() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void saveImage(MyImage image) {
        undoStack.push(image);
        redoStack.clear(); // Clear redo stack when a new image is saved
        // print len of undo stack
//        System.out.println("Undo stack length: " + undoStack.size());
//        System.out.println("Redo stack length: " + redoStack.size());
    }

    public MyImage undo() {
        if (canUndo()) {
            MyImage currentImage = undoStack.pop();
            redoStack.push(currentImage.clone());
            return undoStack.isEmpty() ? null : undoStack.peek();
        }
        return null;
    }

    public MyImage redo() {
        if (canRedo()) {
            MyImage redoImage = redoStack.pop();
            undoStack.push(redoImage);
            return redoImage;
        }
        return null;
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}

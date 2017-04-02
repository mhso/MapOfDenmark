package dk.itu.n.danmarkskort.extras.brewj;

import javax.swing.*;
import java.util.*;

public class BrewJ {
    Model model = new Model();
    ObjectWindow window;
    int x = 100, y = 100;

    public BrewJ(Observable ... observables) {
        SwingUtilities.invokeLater(() -> {
            window = new ObjectWindow(model);
            for (Observable observable : observables) observable.addObserver(window);
        });
    }

    public BrewJ add(Object ... objects) {
        for (Object object : objects) {
            model.add(object, x, y);
            x += 150;
            if (x > (window == null ? 500 : window.window.getWidth())) {
                x = 100;
                y += 200;
            }
        }
        return this;
    }

    public void refresh() {
        model.setDirty();
    }

    public static void main(String[] args) {
		BrewJ bj = new BrewJ();
		SwingUtilities.invokeLater(() -> bj.window.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE));
	    bj.add(bj);
    }
}

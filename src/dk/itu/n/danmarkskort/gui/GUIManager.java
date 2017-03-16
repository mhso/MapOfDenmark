<<<<<<< HEAD
package dk.itu.n.danmarkskort.gui;

import javax.swing.*;
import java.awt.*;

public class GUIManager extends JPanel{

    Style style;

    public GUIManager() {

        // manages the current stylesheet
        style = new Style();

        setLayout(new BorderLayout());

        // margin for the whole frame
        setBorder(BorderFactory.createEmptyBorder(style.margin(), style.margin(), style.margin(), style.margin()));

        add(new TopPanel(style), BorderLayout.NORTH);

        add(new BottomPanel(style), BorderLayout.SOUTH);

        setOpaque(false);
    }
}
=======
package dk.itu.n.danmarkskort.gui;

import javax.swing.*;
import java.awt.*;

public class GUIManager extends JPanel{

    Style style;

    public GUIManager() {

        // manages the current stylesheet
        style = new Style();

        setLayout(new BorderLayout());

        // margin for the whole frame
        setBorder(BorderFactory.createEmptyBorder(style.margin(), style.margin(), style.margin(), style.margin()));

        add(new TopPanel(style), BorderLayout.NORTH);

        add(new BottomPanel(style), BorderLayout.SOUTH);

        setOpaque(false);
    }
}
>>>>>>> branch 'develop' of https://github.itu.dk/fand/BFST17_DK_Kort

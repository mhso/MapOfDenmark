package dk.itu.n.danmarkskort.gui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;

import java.awt.*;

public class CustomScrollBarUI extends BasicScrollBarUI {

    private Style style;
    private final Dimension emptyDimension = new Dimension();
    private Color background;

    public CustomScrollBarUI() {
        this(null);
    }

    public CustomScrollBarUI(Color background) {
        this.style = Main.style;
        if(background == null) {
            if(style == null) style = new Style();
            this.background = style.menuContentBG();
        }
        else this.background = background;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if(thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;

        if(isDragging) g.setColor(style.scrollBarThumbActive());
        else g.setColor(style.scrollBarThumb());

        int w = thumbBounds.width;
        int h = thumbBounds.height;
        int x = thumbBounds.x;
        int y = thumbBounds.y;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.fillRoundRect(x + 4, y + 4, w - 8, h - 8,12,12);
        g2d.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(background);

        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

        if(trackHighlight == DECREASE_HIGHLIGHT) paintDecreaseHighlight(g);
        else if(trackHighlight == INCREASE_HIGHLIGHT) paintIncreaseHighlight(g);
    }

    @Override
    protected void paintDecreaseHighlight(Graphics g) {}

    @Override
    protected void paintIncreaseHighlight(Graphics g) {}

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new JButton() {
            @Override
            public Dimension getPreferredSize() { return emptyDimension; }
        };
    }

    @Override protected JButton createIncreaseButton(int orientation) {
        return new JButton() {
            @Override public Dimension getPreferredSize() {
                return emptyDimension;
            }
        };
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }
}

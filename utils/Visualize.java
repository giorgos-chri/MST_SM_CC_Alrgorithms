/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.BlackAnt;
import model.RedAnt;

/**
 *
 * @author Giorgos Christakis
 * @email giorgos_chri@hotmail.com
 */
public class Visualize extends JPanel{
    ArrayList<RedAnt> redAntsCopy = new ArrayList();
    ArrayList<BlackAnt> blackAntsCopy = new ArrayList();
    int[][] mstCopy = null;
    int[][] matchingsCopy = null;
    private String title = "";
    private int windowSize = 600;
    
    public Visualize(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts, int[][] mst, int[][] matchings, String title){
        this.redAntsCopy = redAnts;
        this.blackAntsCopy = blackAnts;
        this.mstCopy = new int[2 * redAnts.size() - 1][4];
        this.mstCopy = mst;
        this.matchingsCopy = new int[redAnts.size()][2];
        this.matchingsCopy = matchings;
        this.title = title;
    }
    
    
    private void doDrawing(Graphics g) {
        int i;
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.red);
        Dimension size = this.getSize();
        Insets insets = this.getInsets();
        int w = size.width - insets.left - insets.right;
        int h = size.height - insets.top - insets.bottom;
        int x = 0;
        int y = 0;
        int x1 = 0;
        int y1 = 0;
        g2d.setColor(Color.red);
        for(i = 0; i < this.redAntsCopy.size(); ++i){
            x = (int)(this.redAntsCopy.get(i).getX() * (double)this.windowSize % (double)w);
            y = (int)(this.redAntsCopy.get(i).getY() * (double)this.windowSize % (double)h);
            g2d.drawOval(x, y, 3, 3);
            g2d.drawString(Integer.toString(this.redAntsCopy.get(i).getID()), x, y-3);
        }
        g2d.setColor(Color.black);
        for(i = 0; i < this.blackAntsCopy.size(); ++i){
            x = (int)(this.blackAntsCopy.get(i).getX() * (double)this.windowSize % (double)w);
            y = (int)(this.blackAntsCopy.get(i).getY() * (double)this.windowSize % (double)h);
            g2d.drawOval(x, y, 3, 3);
            g2d.drawString(Integer.toString(this.redAntsCopy.get(i).getID()), x, y-3);
        }
        if (this.mstCopy != null) {
            g2d.setColor(Color.blue);
            int mstSize = 2 * this.redAntsCopy.size() - 1;
            for (int i2 = 0; i2 < mstSize; ++i2) {
                if (this.mstCopy[i2][1] == 0) {
                    x = (int)(this.redAntsCopy.get(this.mstCopy[i2][0]).getX() * (double)this.windowSize % (double)w);
                    y = (int)(this.redAntsCopy.get(this.mstCopy[i2][0]).getY() * (double)this.windowSize % (double)w);
                } else {
                    x = (int)(this.blackAntsCopy.get(this.mstCopy[i2][0]).getX() * (double)this.windowSize % (double)w);
                    y = (int)(this.blackAntsCopy.get(this.mstCopy[i2][0]).getY() * (double)this.windowSize % (double)w);
                }
                if (this.mstCopy[i2][3] == 0) {
                    x1 = (int)(this.redAntsCopy.get(this.mstCopy[i2][2]).getX() * (double)this.windowSize % (double)w);
                    y1 = (int)(this.redAntsCopy.get(this.mstCopy[i2][2]).getY() * (double)this.windowSize % (double)w);
                } else {
                    x1 = (int)(this.blackAntsCopy.get(this.mstCopy[i2][2]).getX() * (double)this.windowSize % (double)w);
                    y1 = (int)(this.blackAntsCopy.get(this.mstCopy[i2][2]).getY() * (double)this.windowSize % (double)w);
                }
                g2d.drawLine(x, y, x1, y1);
            }
        }
        if (this.matchingsCopy != null) {
            g2d.setColor(Color.green);
            for (i = 0; i < this.redAntsCopy.size(); ++i) {
                x = (int)(this.redAntsCopy.get(this.matchingsCopy[i][0]).getX() * (double)this.windowSize % (double)w);
                y = (int)(this.redAntsCopy.get(this.matchingsCopy[i][0]).getY() * (double)this.windowSize % (double)w);
                x1 = (int)(this.blackAntsCopy.get(this.matchingsCopy[i][1]).getX() * (double)this.windowSize % (double)w);
                y1 = (int)(this.blackAntsCopy.get(this.matchingsCopy[i][1]).getY() * (double)this.windowSize % (double)w);
                g2d.drawLine(x, y, x1, y1);
            }
        }
    }    
    
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.doDrawing(g);
    }
    
    public void drawInitialPoints() {
        JFrame frame = new JFrame(this.title);
        frame.setDefaultCloseOperation(3);
        frame.add(new Visualize(this.redAntsCopy, this.blackAntsCopy, this.mstCopy, this.matchingsCopy, this.title));
        frame.setSize(this.windowSize + 50, this.windowSize + 50);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
    }
}

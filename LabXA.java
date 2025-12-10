import java.awt.Color;
import java.awt.Graphics;


public class LabXA extends GBSGame {

    int[][] tris = {
        // Front
        {100, 100, -100,  -100, 100, -100,  -100, -100, -100}, 
        {-100, -100, -100, 100, -100, -100, 100, 100, -100}, 

        // Right
        {100, 100, 100, 100, 100, -100, 100, -100, -100}, 
        {100, -100, -100, 100, -100, 100, 100, 100, 100},

        // Back
        {-100, 100, 100, 100, 100, 100, 100, -100, 100}, 
        {100, -100, 100, -100, -100, 100, -100, 100, 100}, 

        // Left
        {-100, 100, -100, -100, 100, 100, -100, -100, 100}, 
        {-100, -100, 100, -100, -100, -100, -100, 100, -100}, 

        // Top
        {100, 100, 100, -100, 100, 100, -100, 100, -100}, 
        {-100, 100, -100, 100, 100, -100, 100, 100, 100}, 

        // Bottom
        {100, -100, -100, -100, -100, -100, -100, -100, 100}, 
        {-100, -100, 100, 100, -100, 100, 100, -100, -100}, 
    };

    int[][] trisProj = new int[tris.length][tris[0].length];

    public LabXA() {
        
    }

    @Override
    public void update(double dt) {
        
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,800,600);

        
    }

    public static void main(String[] args) {
        LabXA lab = new LabXA();
        lab.setResolution(800, 600);
        lab.setFrameRate(60);
        lab.createWindow();
    }
}

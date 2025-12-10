import java.awt.Graphics;

public class LabXA extends GBSGame {

    int[][] tris = {
        {100, 100, 100},
        {100, -100, 100},
        {-100, -100, 100},
        {-100, 100, 100},

        {100, 100, -100},
        {100, -100, -100},
        {-100, -100, -100},
        {-100, 100, -100},
    };

    public LabXA() {

    }

    @Override
    public void update(double dt) {
        
    }

    @Override
    public void draw(Graphics g) {
        // drawTri(g, 100, 200, 300, 400, 100, 400);
    }

    public static void main(String[] args) {
        LabXA lab = new LabXA();
        lab.setResolution(800, 600);
        lab.setFrameRate(60);
        lab.createWindow();
    }
}

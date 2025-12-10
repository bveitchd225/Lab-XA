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
        theta = 0;
    }

    @Override
    public void update(double dt) {
        theta += dt;
        // Reset trisProj
        for (int tri = 0; tri < tris.length; tri++) {
            for (int p = 0; p < tris[tri].length; p++) {
                trisProj[tri][p] = tris[tri][p];
            }
        }


        // Object to World

        // rotate Y
        for (int tri = 0; tri < tris.length; tri++) {
            for (int p = 0; p < tris[tri].length; p+=3) {
                int x = trisProj[tri][p];
                int y = trisProj[tri][p+1];
                int z = trisProj[tri][p+2];

                trisProj[tri][p] = (int) (x*Math.cos(theta) - z*Math.sin(theta));
                trisProj[tri][p+2] = (int) (x*Math.sin(theta) + z*Math.cos(theta));
            }
        }

        for (int tri = 0; tri < tris.length; tri++) {
            for (int p = 0; p < tris[tri].length; p+=3) {
                trisProj[tri][p+2] = trisProj[tri][p+2] +500; //z
            }
        }

        // World to Perspective
        for (int tri = 0; tri < tris.length; tri++) {
            for (int p = 0; p < tris[tri].length; p+=3) {
                trisProj[tri][p] = 600 * trisProj[tri][p] / trisProj[tri][p+2] ; //x
                trisProj[tri][p+1] = 600 * trisProj[tri][p+1] / trisProj[tri][p+2] ; //y
            }
        }

        // Perspective to Screen
        for (int tri = 0; tri < tris.length; tri++) {
            for (int p = 0; p < tris[tri].length; p+=3) {
                trisProj[tri][p] = trisProj[tri][p] += 400; //x
                trisProj[tri][p+1] = trisProj[tri][p+1] += 300; //y
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,800,600);
        g.setColor(Color.RED);
        for (int i = 0; i < tris.length; i++) {
            drawTri(g, trisProj[i][0], trisProj[i][1], trisProj[i][3], trisProj[i][4], trisProj[i][6], trisProj[i][7]);
        }
    }

    public static void main(String[] args) {
        LabXA lab = new LabXA();
        lab.setResolution(800, 600);
        lab.setFrameRate(60);
        lab.createWindow();
    }
}

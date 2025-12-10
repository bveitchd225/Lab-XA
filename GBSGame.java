import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GBSGame extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private Thread gameThread;
    private JFrame f;

    private int FPS;
    private double maxFrameTime;
    private static int WIDTH;
    private static int HEIGHT;

    private BufferedImage buffer1;
    private BufferedImage buffer2;
    private int[] buffer1Data;
    private int[] buffer2Data;
    private int currentBuffer;
    private Graphics screen1;
    private Graphics screen2;

    private RollingAverage updateTime = new RollingAverage(100);
    private RollingAverage drawTime = new RollingAverage(100);

    private static ArrayList<String> keys = new ArrayList<>();
    private static ArrayList<String> keyEvents = new ArrayList<>();

    private static int lastReportedMouseX = -1;
    private static int lastReportedMouseY = -1;
    
    public void setResolution(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        buffer1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        buffer2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        screen1 = buffer1.getGraphics();
        screen2 = buffer2.getGraphics();
        buffer1Data = ((DataBufferInt) buffer1.getRaster().getDataBuffer()).getData();
        buffer2Data = ((DataBufferInt) buffer2.getRaster().getDataBuffer()).getData();
        currentBuffer = 1;
    }

    public void setPixelActiveBuffer(int x, int y, int c) {
        if (currentBuffer == 2) {
            buffer2Data[buffer2.getWidth()*y + x] = c;
        }
        else {
            buffer1Data[buffer1.getWidth()*y + x] = c;
        }
    }

    public void setFrameRate(int f) {
        FPS = f;
        maxFrameTime = 1000000000.0 / f;
    }

    public double getUpdateTime() {
        return updateTime.currentAverage();
    }

    public double getDrawTime() {
        return drawTime.currentAverage();
    }

    public static int getScreenWidth() {
        return WIDTH;
    }

    public static int getScreenHeight() {
        return HEIGHT;
    }

    public int getFPS() {
        return (int) (1000 / (getUpdateTime() + getDrawTime()));
    }

    public void drawStats(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(10, 10, 90, 52);
        g.setColor(Color.GREEN);
        g.drawString("FPS: " + getFPS(), 14, 24);
        g.drawString("update: " + (int) (getUpdateTime()*100)/100.0, 14, 40);
        g.drawString("draw: " + (int) (getDrawTime()*100)/100.0, 14, 56);

    }

    public void createWindow() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
        f = new JFrame();
        f.add(this);
        f.setTitle("Game");
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        f.setFocusable(true);
        startGameThread();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        // This will call the "run" method of the object passed into the thread.
        gameThread.start();
    }

    @Override
    public void run() {

        // double lastFrame = System.nanoTime();
        double start = System.nanoTime();
        double lastStart = start;

        

        while (gameThread != null) {
            

            if (System.nanoTime() - start > maxFrameTime) {
                lastStart = start;
    
                start = System.nanoTime();
    
                update((start - lastStart)/1000000000);
    
                double afterUpdate = System.nanoTime();
                updateTime.addValue((afterUpdate - start) / 1000000);
    
                if (currentBuffer == 1) {
                    draw(screen1);
                    currentBuffer = 2;
                }
                else {
                    draw(screen2);
                    currentBuffer = 1;
                }
    
                drawTime.addValue((System.nanoTime() - afterUpdate) / 1000000);
    
                repaint();
            }
            
        }
    }

    // Meant to override
    public void update(double dt) {

    }

    // Meant to override
    public void draw(Graphics g) {

    }

    // Meant to override
    public void draw(BufferedImage b) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // g.setColor(Color.WHITE);
        // g.fillRect(0,0,WIDTH, HEIGHT);
        if (currentBuffer == 2) {
            g.drawImage(buffer1,0,0,null);
        }
        else {
            g.drawImage(buffer2,0,0,null);
        }
    }

    public static boolean keyDown(String key) {
        if (key.equals("space")) {
            key = " ";
        }
        return keys.contains(key);
    }

    public Image getImage(String filePath) {
        return javax.swing.ImageIcon.class.getResource(filePath) != null
            ? new javax.swing.ImageIcon(getClass().getResource(filePath)).getImage()
            : new javax.swing.ImageIcon(filePath).getImage();
    }

    public static BufferedImage getBufferedImage(String filePath) {
        Image image = new javax.swing.ImageIcon(filePath).getImage();
        BufferedImage newBuffered = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics g = newBuffered.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newBuffered;
    }

    public void drawTri(Graphics g, double x1, double y1, double x2, double y2, double x3, double y3) {
        g.drawPolygon(new int[]{(int) x1, (int) x2, (int) x3}, new int[]{(int) y1, (int) y2, (int) y3}, 3);
    }




    public static boolean keyPressed(String key) {
        if (key.equals("space")) {
            key = " ";
        }
        boolean keyPressedBool = keyEvents.contains(key);
        while (keyEvents.contains(key)) {
            keyEvents.remove(key);
        }
        return keyPressedBool;
    }

    public static int getMouseX() {
        return lastReportedMouseX;
    }

    public static int getMouseY() {
        return lastReportedMouseY;
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!keys.contains(e.getKeyChar() + "")) {
            keyEvents.add(e.getKeyChar() + "");
        }
        keys.add(e.getKeyChar() + "");
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String key = e.getKeyChar() + "";
        while (keys.contains(key)) {
            keys.remove(key);
        }
        while (keyEvents.contains(key)) {
            keyEvents.remove(key);
        }
    }

    public void onMouseClick(MouseEvent e) {
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        onMouseClick(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastReportedMouseX = e.getX();
        lastReportedMouseY = e.getY();
    }
}





class RollingAverage {
    private double[] values;
    private double lastReported;
    private int rollingIndex;
    private boolean filled;

    public RollingAverage(int size) {
        values = new double[size];
        rollingIndex = 0;
        lastReported = -1;
        filled = false;
    }

    public void addValue(double newValue) {
        values[rollingIndex] = newValue;
        rollingIndex++;
        if (rollingIndex > values.length-1) {
            filled = true;
            rollingIndex = 0;
            double sum = 0;
            for (double n: values) {
                sum += n;
            }
            lastReported = sum / values.length;
        }
    }

    public double currentAverage() {
        double sum = 0;
        if (!filled) {
            for (int i = 0; i < rollingIndex; i++) {
                sum += values[i];
            }
            return sum/rollingIndex;
        }
        
        return lastReported;
    }
}

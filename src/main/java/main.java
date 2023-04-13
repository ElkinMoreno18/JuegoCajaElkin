import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class main extends JFrame implements KeyListener {
    private Box box;
    private JPanel panel;
    private Thread moveThread;
    private Thread animateThread;

    public main() {
        box = new Box(250, 250, 50, 50, 5);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                box.draw(g);
            }
        };
        moveThread = new Thread(() -> {
            while (true) {
                if (box.isMoving()) {
                    box.move(panel.getWidth(),panel.getHeight());
                    panel.repaint();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        animateThread = new Thread(() -> {
            while (true) {
                if (box.isAtEdge(panel)) {
                    int randomSizeChange = (int) (Math.random() * 10 + 1);
                    box.changeSize(randomSizeChange);
                    box.changeColor(new Color((int)(Math.random() * 0x1000000)));
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        setSize(500,500);
        panel.setFocusable(true);
        panel.addKeyListener(this);

        add(panel);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        moveThread.start();
        animateThread.start();
    }

    public static void main(String[] args) {
        new main();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                box.setDirection(Box.Direction.UP);
                box.setMoving(true);
                break;
            case KeyEvent.VK_DOWN:
                box.setDirection(Box.Direction.DOWN);
                box.setMoving(true);
                break;
            case KeyEvent.VK_LEFT:
                box.setDirection(Box.Direction.LEFT);
                box.setMoving(true);
                break;
            case KeyEvent.VK_RIGHT:
                box.setDirection(Box.Direction.RIGHT);
                box.setMoving(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                box.setMoving(false);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}

class Box {
    enum Direction {UP, DOWN, LEFT, RIGHT}

    private int x, y, width, height, speed;
    private Direction direction;
    private boolean moving;
    private Color color;

    public Box(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.direction = Direction.RIGHT;
        this.moving = false;
        this.color = Color.BLUE;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public void move(int panelWidth, int panelHeight) {
        switch (direction) {
            case UP:
                if (y > 0) {
                    y -= speed;
                } else {
                    y += speed;
                }
                break;
            case DOWN:
                if (y + height < panelHeight) {
                    y += speed;
                } else {
                    y -= speed;
                }
                break;
            case LEFT:
                if (x > 0) {
                    x -= speed;
                } else {
                    x += speed;
                }
                break;
            case RIGHT:
                if (x + width < panelWidth) {
                    x += speed;
                } else {
                    x -= speed;
                }
                break;
        }
    }


    public void changeSize(int sizeChange) {
        Random random = new Random();
        boolean increase = random.nextBoolean();
        if (increase) {
            width += sizeChange;
            height += sizeChange;
        } else {
            width -= sizeChange;
            height -= sizeChange;
        }
    }

    public void changeColor(Color color) {
        this.color = color;
    }

    public boolean isAtEdge(JPanel panel) {
        return x <= 0 || y <= 0 || x + width >= panel.getWidth() || y + height >= panel.getHeight();
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
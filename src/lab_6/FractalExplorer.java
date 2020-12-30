package lab_6;

import lab_6.fractals.BurningShip;
import lab_6.fractals.Mandelbrot;
import lab_6.fractals.Tricorn;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class FractalExplorer extends JFrame {

    private int width, height;
    private FractalGenerator generator;

    private Rectangle2D.Double viewport;
    private JImageDisplay fractalImage;

    public DrawCancelToken drawCancelToken;
    private JButton saveBtn;

    public FractalExplorer(int w, int h, FractalGenerator generator) {
        super("Fractal Explorer");
        this.width = w;
        this.height = h;
        this.generator = generator;

        this.viewport = generator.getInitialRange();
    }

    public JImageDisplay getFractalImage() {
        return fractalImage;
    }

    public void resetViewport() {
        this.setViewport(this.generator.getInitialRange());
    }

    public Rectangle2D.Double getViewport() {
        return viewport;
    }

    public void setViewport(Rectangle2D.Double viewport) {

        this.viewport = viewport;
        drawFractal();
    }

    static final String MANDELBROT = "Mandelbrot";
    static final String BURNING_SHIP = "Burning Ship";
    static final String TRICORN = "Tricorn";

    public void createAndShowGui() {
        JFrame frame = this;
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);

        JImageDisplay jim = new JImageDisplay(width, height);
        this.fractalImage = jim;

        JPanel panelNorth = new JPanel();
        {
            panelNorth.add(new JLabel("Select fractal: "));

            FractalGenerator mandelbrot = new Mandelbrot();
            FractalGenerator tricorn = new Tricorn();
            FractalGenerator burningShip = new BurningShip();

            JComboBox cBox = new JComboBox(new String[]{MANDELBROT, TRICORN, BURNING_SHIP});
            cBox.addItemListener((ev) -> {
                if (MANDELBROT.equals(ev.getItem())) {
                    setGenerator(mandelbrot);
                }
                if (TRICORN.equals(ev.getItem())) {
                    setGenerator(tricorn);
                }
                if (BURNING_SHIP.equals(ev.getItem())) {
                    setGenerator(burningShip);
                }
            });

            panelNorth.add(cBox);
        }

        JPanel panelSouth = new JPanel();
        {
            JButton saveBtn = this.saveBtn = new JButton("Save");
            saveBtn.addActionListener((_e) -> {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
                fc.setAcceptAllFileFilterUsed(false);
                int result = fc.showDialog(this, "Save");
                if (result != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File file = fc.getSelectedFile();

                try {
                    ImageIO.write(this.fractalImage.getImage(), "png", file);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e, "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton resetBtn = new JButton("Reset");
            resetBtn.addActionListener((_e) -> { this.resetViewport(); });

            panelSouth.add(saveBtn);
            panelSouth.add(resetBtn);
        }

        frame.add(jim,  BorderLayout.CENTER);
        frame.add(panelNorth,  BorderLayout.NORTH);
        frame.add(panelSouth, BorderLayout.SOUTH);

        // Mouse listeners
        jim.addMouseWheelListener(ev -> {
            Rectangle2D.Double vp = this.getViewport();
            double mouseNormedX = (double) ev.getX() / (double) jim.getWidth();
            double mouseNormedY = (double) ev.getY() / (double) jim.getHeight();
            double dCenterX = ((mouseNormedX - 0.5) * 2);
            double dCenterY = ((mouseNormedY - 0.5) * 2);

            if (ev.getWheelRotation() == -1 /* zoom + */) {
                final double ZOOM_SCALE = 0.5;
                double newCenterX = vp.getCenterX() + dCenterX * vp.getWidth() / 2.0;
                double newCenterY = vp.getCenterY() + dCenterY * vp.getHeight() / 2.0;
                double vpWidth = vp.getWidth() * ZOOM_SCALE;
                double vpHeight = vp.getHeight() * ZOOM_SCALE;

                Rectangle2D.Double newVp = new Rectangle2D.Double(
                        newCenterX - vpWidth / 2.0,
                        newCenterY - vpHeight / 2.0,
                        vpWidth,
                        vpHeight
                );
                this.setViewport(newVp);
            } else {
                final double ZOOM_SCALE = 1.5;
                double newCenterX = vp.getCenterX() - dCenterX * vp.getWidth() / 2.0;
                double newCenterY = vp.getCenterY() - dCenterY * vp.getHeight() / 2.0;
                double vpWidth = vp.getWidth() * ZOOM_SCALE;
                double vpHeight = vp.getHeight() * ZOOM_SCALE;

                Rectangle2D.Double newVp = new Rectangle2D.Double(
                        newCenterX - vpWidth / 2.0,
                        newCenterY - vpHeight / 2.0,
                        vpWidth,
                        vpHeight
                );
                this.setViewport(newVp);
            }
        });

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // first draw
        drawFractal();
    }

    public FractalGenerator getGenerator() {
        return generator;
    }

    public void onDrawLine(int all) {
        if (all == this.fractalImage.getImage().getHeight()) {
            this.saveBtn.setEnabled(true);
        }
    }

    private void setGenerator(FractalGenerator generator) {
        this.generator = generator;
        this.viewport = this.generator.getInitialRange();
        this.drawFractal();
    }

    private void drawFractal() {
        BufferedImage im = this.fractalImage.getImage();
        int imHeight = im.getHeight();

        if (this.drawCancelToken != null) { this.drawCancelToken.cancel(); }
        this.drawCancelToken = new DrawCancelToken(this);
        this.saveBtn.setEnabled(false);

        for (int imY = 0; imY < imHeight; imY++) {
            SwingWorker worker = new FractalRenderWorker(this, imY, this.drawCancelToken);
            worker.execute();
        }
    }
}

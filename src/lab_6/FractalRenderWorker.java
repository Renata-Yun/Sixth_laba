package lab_6;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class FractalRenderWorker extends SwingWorker<Object, Object> {

    private int y;
    private FractalExplorer explorer;
    private int[] lineBuffer;
    private DrawCancelToken cancelToken;

    public FractalRenderWorker(FractalExplorer explorer, int y) {
        this(explorer, y, null);
    }

    public FractalRenderWorker(FractalExplorer explorer, int y, DrawCancelToken cancelToken) {
        this.explorer = explorer;
        this.y = y;
        this.cancelToken = cancelToken;
    }

    private boolean isCancelledByToken() {
        return cancelToken != null && cancelToken.isCancelled();
    }

    @Override
    protected Object doInBackground() throws Exception {
        if (isCancelledByToken()) { return null; }
        BufferedImage im = this.explorer.getFractalImage().getImage();
        lineBuffer = new int[im.getWidth()];
        int imWidth = im.getWidth();
        int imHeight = im.getWidth();

        Rectangle2D.Double vp = this.explorer.getViewport();

        FractalGenerator generator = this.explorer.getGenerator();

        int imY = this.y;

        for (int imX = 0; imX < imWidth; imX++) {
            // Если не отменено, то продолжаем рисовать
            if (isCancelledByToken()) { break; }
            double x = FractalGenerator.getCoord(vp.getX(), vp.getMaxX(), imWidth, imX);
            double y = FractalGenerator.getCoord(vp.getY(), vp.getMaxY(), imHeight, imY);

            int iters = generator.numIterations(x, y);
            if (iters == -1) {
                this.lineBuffer[imX] = 0x00000000;
            } else {
                float hue = 0.7f + (float) iters / 200f;
                int color = Color.HSBtoRGB(hue, 1f, 1f);
                this.lineBuffer[imX] = color;
            }
        }
        return null;
    }

    @Override
    protected void done() {
        if (isCancelledByToken()) { return; }
        if (this.cancelToken != null) {
            this.cancelToken.notifyDrawLine();
        }
        BufferedImage im = this.explorer.getFractalImage().getImage();
        for (int x = 0; x < im.getWidth(); x++) {
            im.setRGB(x, this.y, this.lineBuffer[x]);
        }
        this.explorer.getFractalImage().repaint(0, 0, this.y, im.getHeight(), 1);
    }
}

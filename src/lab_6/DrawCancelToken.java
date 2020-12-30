package lab_6;

import java.util.concurrent.atomic.AtomicBoolean;

public class DrawCancelToken {
    private AtomicBoolean isCancelled = new AtomicBoolean(false);
    private int linesDraw = 0;
    private FractalExplorer explorer;

    public DrawCancelToken(FractalExplorer explorer) {
        this.explorer = explorer;
    }

    public boolean isCancelled() {
        return this.isCancelled.get();
    }

    public void cancel() {
        this.isCancelled.set(true);
        this.explorer = null;
    }

    public void notifyDrawLine() {
        if (this.explorer == null) { return; }
        this.linesDraw += 1;
        this.explorer.onDrawLine(this.linesDraw);
    }
}

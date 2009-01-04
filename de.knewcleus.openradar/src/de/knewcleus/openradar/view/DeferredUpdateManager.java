package de.knewcleus.openradar.view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

/**
 * A deferred viewer repaint manager collects dirty regions and schedules them for
 * repaint as soon as the system is idle.
 * 
 * @author Ralf Gerlich
 *
 */
public class DeferredUpdateManager implements IUpdateManager, Runnable {
	protected final IViewerAdapter viewerAdapter;
	protected ICanvas canvas;
	
	protected Rectangle2D dirtyRegion = new Rectangle2D.Double();
	protected final Set<IView> invalidViews = new HashSet<IView>();
	protected boolean fullRepaint = false;
	protected boolean repaintScheduled = false;
	
	public DeferredUpdateManager(IViewerAdapter viewerAdapter) {
		this.viewerAdapter = viewerAdapter;
	}
	
	@Override
	public void setCanvas(ICanvas canvas) {
		this.canvas = canvas;
	}
	
	@Override
	public ICanvas getCanvas() {
		return canvas;
	}
	
	@Override
	public synchronized void invalidateView(IView view) {
		invalidViews.add(view);
		scheduleUpdate();
	}

	@Override
	public synchronized void addDirtyView(IView view) {
		if (view instanceof IBoundedView) {
			final IBoundedView boundedView=(IBoundedView)view;
			Rectangle2D.union(boundedView.getDisplayExtents(), dirtyRegion, dirtyRegion);
		} else {
			fullRepaint = true;
		}
		scheduleUpdate();
	}

	protected void scheduleUpdate() {
		if (repaintScheduled)
			return;
		SwingUtilities.invokeLater(this);
		repaintScheduled=true;
	}
	
	protected synchronized void performValidation() {
		viewerAdapter.revalidate();
		for (IView view: invalidViews) {
			view.revalidate();
		}
		invalidViews.clear();
	}
	
	protected synchronized void repairDamage() {
		if (canvas!=null) {
			if (fullRepaint) {
				dirtyRegion = viewerAdapter.getViewerExtents();
			}
			final Graphics2D g2d = canvas.getGraphics(dirtyRegion);
			if (g2d!=null) {
				final Rectangle clipRectangle = dirtyRegion.getBounds();
				g2d.clearRect(clipRectangle.x, clipRectangle.y, clipRectangle.width, clipRectangle.height);
				final ViewPaintVisitor viewPaintVisitor = new ViewPaintVisitor(g2d);
				viewerAdapter.getRootView().accept(viewPaintVisitor);
				canvas.flushGraphics();
			}
		}
		dirtyRegion = new Rectangle2D.Double();
		fullRepaint = false;
		repaintScheduled = false;
	}
	
	@Override
	public void run() {
		performValidation();
		repairDamage();
	}
}
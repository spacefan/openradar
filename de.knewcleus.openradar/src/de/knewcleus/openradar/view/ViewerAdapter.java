package de.knewcleus.openradar.view;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import de.knewcleus.openradar.notify.Notifier;

public class ViewerAdapter extends Notifier implements IViewerAdapter {
	protected Rectangle2D viewerExtents = new Rectangle2D.Double();
	protected IUpdateManager updateManager = new DeferredUpdateManager(this); 
	protected final LayeredView rootView = new LayeredView(this);
	protected double logicalScale = 1.0;
	protected Point2D logicalOrigin = new Point2D.Double();
	protected Point2D deviceOrigin = new Point2D.Double();
	protected AffineTransform deviceToLogicalTransform = null;
	protected AffineTransform logicalToDeviceTransform = null;

	public ViewerAdapter() {
		super();
	}

	@Override
	public Rectangle2D getViewerExtents() {
		return viewerExtents;
	}

	@Override
	public void setViewerExtents(Rectangle2D extents) {
		viewerExtents = extents;
		notify(new CoordinateSystemNotification(this));
	}
	
	@Override
	public IUpdateManager getUpdateManager() {
		return updateManager;
	}
	
	@Override
	public LayeredView getRootView() {
		return rootView;
	}
	
	@Override
	public Point2D getDeviceOrigin() {
		return deviceOrigin;
	}
	
	@Override
	public void setDeviceOrigin(double originX, double originY) {
		deviceOrigin = new Point2D.Double(originX, originY);
		invalidateTransforms();
	}
	
	@Override
	public void setDeviceOrigin(Point2D origin) {
		deviceOrigin = origin;
		invalidateTransforms();
	}

	@Override
	public double getLogicalScale() {
		return logicalScale;
	}

	@Override
	public void setLogicalScale(double scale) {
		this.logicalScale = scale;
		invalidateTransforms();
	}

	@Override
	public void setLogicalOrigin(double offsetX, double offsetY) {
		logicalOrigin = new Point2D.Double(offsetX, offsetY);
		invalidateTransforms();
	}
	
	@Override
	public void setLogicalOrigin(Point2D origin) {
		logicalOrigin = origin;
		invalidateTransforms();
	}
	
	@Override
	public Point2D getLogicalOrigin() {
		return logicalOrigin;
	}

	@Override
	public AffineTransform getDeviceToLogicalTransform() {
		if (deviceToLogicalTransform==null) {
			updateTransforms();
		}
		return deviceToLogicalTransform;
	}

	@Override
	public AffineTransform getLogicalToDeviceTransform() {
		if (logicalToDeviceTransform==null) {
			updateTransforms();
		}
		return logicalToDeviceTransform;
	}

	protected void invalidateTransforms() {
		if (deviceToLogicalTransform==null || logicalToDeviceTransform==null) {
			/* No need to invalidate and update if they are still invalidated */
			return;
		}
		notify(new CoordinateSystemNotification(this));
	}

	@Override
	public void revalidate() {
		deviceToLogicalTransform = null;
		logicalToDeviceTransform = null;
	}
	
	protected void updateTransforms() {
		deviceToLogicalTransform = new AffineTransform(
				logicalScale, 0,
				0, -logicalScale,
				logicalOrigin.getX() - logicalScale * deviceOrigin.getX(),
				logicalOrigin.getY() + logicalScale * deviceOrigin.getY());
		logicalToDeviceTransform = new AffineTransform(
				1.0/logicalScale, 0,
				0, -1.0/logicalScale,
				deviceOrigin.getX() - logicalOrigin.getX()/logicalScale,
				deviceOrigin.getY() + logicalOrigin.getY()/logicalScale);
	}
}
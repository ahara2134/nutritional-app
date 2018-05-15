package ca.infostages.infonut;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

import ca.infostages.infonut.ui.camera.GraphicOverlay;

public class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private BarcodeGraphicTracker.NewDetectionListener newDetectionListener;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    public BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay, BarcodeGraphicTracker.NewDetectionListener listener) {
        mGraphicOverlay = barcodeGraphicOverlay;
        newDetectionListener = listener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        BarcodeGraphicTracker tracker = new BarcodeGraphicTracker(mGraphicOverlay, graphic);
        if (newDetectionListener != null) tracker.setListener(newDetectionListener);
        return tracker;
    }
}


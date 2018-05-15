/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.infostages.infonut;

import android.support.annotation.UiThread;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import ca.infostages.infonut.ui.camera.GraphicOverlay;


public class BarcodeGraphicTracker extends Tracker<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mOverlay;
    private BarcodeGraphic mGraphic;
    private NewDetectionListener mListener;

    private BarcodeUpdateListener mBarcodeUpdateListener;

    public interface BarcodeUpdateListener {
        @UiThread
        void onBarcodeDetected(Barcode barcode);
    }

    BarcodeGraphicTracker(GraphicOverlay<BarcodeGraphic> mOverlay, BarcodeGraphic mGraphic/*,
                          Context context*/) {
        this.mOverlay = mOverlay;
        this.mGraphic = mGraphic;
    }

    @Override
    public void onNewItem(int id, Barcode item) {
        /*mGraphic.setId(id);
        mBarcodeUpdateListener.onBarcodeDetected(item);*/

        mGraphic.setId(id);
        if (mListener != null) mListener.onNewDetection(item);

    }

    public void setListener(NewDetectionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode item) {
        mOverlay.add(mGraphic);
        mGraphic.updateItem(item);
    }

    @Override
    public void onMissing(Detector.Detections<Barcode> detectionResults) {
        mOverlay.remove(mGraphic);
    }

    @Override
    public void onDone() {
        mOverlay.remove(mGraphic);
    }

    public interface NewDetectionListener {
        void onNewDetection(Barcode barcode);
    }
}

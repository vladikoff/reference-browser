/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.reference.browser.settings

import android.content.pm.PackageManager
import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.mozilla.reference.browser.ext.requireComponents

import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.LinearLayout;
import android.widget.TextView;



class ScanFragment : Fragment(), ZXingScannerView.ResultHandler {
    lateinit var scannerView: ZXingScannerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val cameraPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 1)
        }

        val linearLa = FrameLayout(requireActivity())
        scannerView = ZXingScannerView(requireActivity())
        scannerView.layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        linearLa.addView(scannerView)

        //val layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        val valueTV = TextView(requireActivity())
        valueTV.text = "Open https://pairsona-qa.dev.lcip.org/pair in Firefox Desktop for your pairing code"
        valueTV.layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        valueTV.setGravity(Gravity.CENTER)
        valueTV.setTextColor(Color.WHITE)
        valueTV.setTextSize(20f)
        linearLa.addView(valueTV)



        return linearLa;
    }

    override fun onResume() {
        super.onResume()
        scannerView?.setResultHandler(this)
        scannerView?.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView?.stopCamera()
    }

    override fun handleResult(rawResult: Result) {
        activity?.finish()
        requireComponents.services.accounts.pair(rawResult.text)
    }
}
package com.example.tourrent.liveStream

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tourrent.R

class CreateLiveStream : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        context?.let { validateMobileLiveIntent(it) }

        return inflater.inflate(R.layout.fragment_create_live_stream, container, false)
    }

    private fun canResolveMobileLiveIntent(context: Context): Boolean {
        val intent = Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM")
            .setPackage("com.google.android.youtube")
        val pm: PackageManager = context.packageManager
        val resolveInfo: List<*> =
            pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo.isNotEmpty()
    }


    private fun validateMobileLiveIntent(context: Context) {
        if (canResolveMobileLiveIntent(context)) {
            startMobileLive(context)
        } else {
            Toast.makeText(activity, "Please Install or Update the\nlatest Youtube App", Toast.LENGTH_LONG).show()
        }
    }

    private fun createMobileLiveIntent(context: Context, description: String): Intent {
        val intent = Intent("com.google.android.youtube.intent.action.CREATE_LIVE_STREAM")
            .setPackage("com.google.android.youtube")
        val referrer: Uri = Uri.Builder()
            .scheme("android-app")
            .appendPath(context.packageName)
            .build()
        intent.putExtra(Intent.EXTRA_REFERRER, referrer)
        if (!TextUtils.isEmpty(description)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, description)
        }
        return intent
    }


    private fun startMobileLive(context: Context) {
        val mobileLiveIntent = createMobileLiveIntent(context, "Streaming via ...")
        startActivity(mobileLiveIntent)
    }
}
package com.apck.huzgtv;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.media.tv.TvInputInfo;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.media.tv.TvContract;
import android.net.Uri;

public class HuzgTvInputSetupActivity extends Activity {
    private static final String CHANNEL_1_NUMBER = "1";
    private static final String CHANNEL_1_NAME = "Tom and Jerry";
    private static final int CHANNEL_1_ORIG_NETWORK_ID = 0;
    private static final int CHANNEL_1_TRANSPORT_STREAM_ID = 0;
    public static final int CHANNEL_1_SERVICE_ID = 1;
    private String mInputId;

    private void registerChannels() {
        // Check if we already registered channels.
        Uri uri = TvContract.buildChannelsUriForInput(mInputId);
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                return;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ContentValues values = new ContentValues();
        values.put(TvContract.Channels.COLUMN_INPUT_ID, mInputId);

        // Register channel 1-1.
        values.put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, CHANNEL_1_NUMBER);
        values.put(TvContract.Channels.COLUMN_DISPLAY_NAME, CHANNEL_1_NAME);
        values.put(TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID, CHANNEL_1_ORIG_NETWORK_ID);
        values.put(TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID, CHANNEL_1_TRANSPORT_STREAM_ID);
        values.put(TvContract.Channels.COLUMN_SERVICE_ID, CHANNEL_1_SERVICE_ID);
        getContentResolver().insert(TvContract.Channels.CONTENT_URI, values);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputId = getIntent().getStringExtra(TvInputInfo.EXTRA_INPUT_ID);

        AlertDialog alertDialog = new AlertDialog.Builder(HuzgTvInputSetupActivity.this).create();
        alertDialog.setTitle("Setup HuzgTV Input");
        alertDialog.setMessage("Do you want to register detected channels?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        registerChannels();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.show();
    }
}

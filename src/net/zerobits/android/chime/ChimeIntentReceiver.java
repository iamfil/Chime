/**
 * Chime
 * 
 * Copyright (C) 2011 ZeroBits LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.zerobits.android.chime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class ChimeIntentReceiver extends BroadcastReceiver
{
	private static final String TAG = "ChimeIntentReceiver";

	/**
	 * Debugging intents that can be received.
	 */
	public static final String ACTION_DEBUG_BOOT = "net.zerobits.android.chime.ACTION_DEBUG_BOOT";
	public static final String ACTION_DEBUG_SHUTDOWN = "net.zerobits.android.chime.ACTION_DEBUG_SHUTDOWN";

	/**
	 * Possible actions that can be inside the intent.
	 */
	private static final int ACTION_INVALID = 0;
	private static final int ACTION_BOOT = ACTION_INVALID + 1;
	private static final int ACTION_SHUTDOWN = ACTION_BOOT + 1;

	private static int compareAction(final Intent intent)
	{
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(ChimeIntentReceiver.ACTION_DEBUG_BOOT))
		{
			return ChimeIntentReceiver.ACTION_BOOT;
		}
		else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN) || intent.getAction().equals(ChimeIntentReceiver.ACTION_DEBUG_SHUTDOWN))
		{
			return ChimeIntentReceiver.ACTION_SHUTDOWN;
		}

		return ChimeIntentReceiver.ACTION_INVALID;
	}

	private static void playAudio(final Context context, final String uri)
	{
		try
		{
			final MediaPlayer mp = new MediaPlayer();
			mp.setDataSource(context, Uri.parse(uri));
			mp.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
			mp.prepare();
			mp.setLooping(false);
			mp.start();

			mp.setOnCompletionListener(new OnCompletionListener()
			{
				@Override
				public void onCompletion(final MediaPlayer mp)
				{
					mp.release();
				}
			});
		}
		catch (final Exception e)
		{
			Log.e(ChimeIntentReceiver.TAG, "Failed playing audio.");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(final Context context, final Intent intent)
	{
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		switch (compareAction(intent))
		{
			case ChimeIntentReceiver.ACTION_BOOT:
			{
				if (prefs.getBoolean(PreferenceKeys.BOOT_ENABLED, false))
				{
					ChimeIntentReceiver.playAudio(context, prefs.getString(PreferenceKeys.BOOT_SOUND, context.getString(R.string.default_notification)));
				}

				break;
			}
			case ChimeIntentReceiver.ACTION_SHUTDOWN:
			{
				if (prefs.getBoolean(PreferenceKeys.SHUTDOWN_ENABLED, false))
				{
					ChimeIntentReceiver.playAudio(context, prefs.getString(PreferenceKeys.SHUTDOWN_SOUND, context.getString(R.string.default_notification)));
				}

				break;
			}
			default:
			{
				Log.e(ChimeIntentReceiver.TAG, "Unknown action!");

				break;
			}
		}
	}
}

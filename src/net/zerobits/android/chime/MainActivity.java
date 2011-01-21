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

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class MainActivity extends PreferenceActivity
{
	private static final int DIALOG_LICENSE = 1;

	/*
	 * (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		PreferenceManager.setDefaultValues(this, R.xml.settings, false);

		this.addPreferencesFromResource(R.xml.settings);

		this.findPreference(PreferenceKeys.LICENSE).setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(final Preference preference)
			{
				MainActivity.this.showDialog(MainActivity.DIALOG_LICENSE);

				return true;
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(final int id)
	{
		Dialog dialog = null;

		switch (id)
		{
			case MainActivity.DIALOG_LICENSE:
			{
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("License");
				builder.setView(this.getLayoutInflater().inflate(R.layout.license_dialog, null));
				builder.setNeutralButton("Ok", null);

				dialog = builder.create();
			}
		}

		return dialog;
	}
}

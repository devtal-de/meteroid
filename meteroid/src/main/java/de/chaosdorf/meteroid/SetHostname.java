/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2013-2016 Chaosdorf e.V.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/

package de.chaosdorf.meteroid;

import android.app.Activity;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import de.chaosdorf.meteroid.util.Utility;

public class SetHostname extends Activity
{
	private Activity activity = null;
	private SharedPreferences prefs;
	private EditText editText;
	private Button saveButton;

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_set_hostname);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final String hostname = prefs.getString("hostname", null);

		editText = (EditText) findViewById(R.id.hostname);
		if (editText != null)
		{
			if (hostname != null)
			{
				editText.setText(hostname);
			}
			final Editable editTextHostname = editText.getText();
			if (editTextHostname != null)
			{
				Selection.setSelection(editTextHostname, editTextHostname.length());
			}
		}

		saveButton = (Button) findViewById(R.id.button_save);
		saveButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				saveHostname();
			}
		});

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			ActionBar actionBar = getActionBar();
			if(actionBar != null)
			{
				saveButton.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_save:
				saveHostname();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	public void saveHostname()
	{
		if (editText == null)
		{
			Utility.displayToastMessage(activity, getResources().getString(R.string.set_hostname_empty));
			return;
		}
		final Editable editTextHostname = editText.getText();
		if (editTextHostname == null)
		{
			Utility.displayToastMessage(activity, getResources().getString(R.string.set_hostname_empty));
			return;
		}
		String newHostname = editTextHostname.toString();
		if (newHostname.equals("http://"))
		{
			Utility.displayToastMessage(activity, getResources().getString(R.string.set_hostname_empty));
			return;
		}
		if (!newHostname.endsWith("/"))
		{
			newHostname += "/";
		}
		if (!(URLUtil.isHttpUrl(newHostname) || URLUtil.isHttpsUrl(newHostname)))
		{
			Utility.displayToastMessage(activity, getResources().getString(R.string.set_hostname_invalid));
			return;
		}
		prefs.edit().putString("hostname", newHostname).apply();
		Utility.startActivity(activity, PickUsername.class);
	}
}

package com.example.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnStart1 = (Button) findViewById(R.id.btn_start1);
		Button btnStart2 = (Button) findViewById(R.id.btn_start2);

		btnStart1.setOnClickListener(this);
		btnStart2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_start1:
			// start a blocking long running task
			doLongTaskOnMain(50001);
			break;

		case R.id.btn_start2:
			// start a long task that uses an AsyncTask
			// will NOT block the UI thread
			LongTask task = new LongTask();
			task.execute(50000);
			break;
		}

	}

	// a long task.
	// run on the UI thread
	// will block the UI thread and hang the application
	private void doLongTaskOnMain(int number) {

		// before the calculation
		TextView textOutput = (TextView) findViewById(R.id.textOutput);
		textOutput.setText("calculating sum of " + number);

		long sum = 0;

		// the calculation
		for (int i = 0; i < number; i++) {
			// do something...
			sum += i;

			textOutput.setText("progress " + 100f * i / number);
		}

		// after the calculation
		textOutput.setText("sum: " + sum);
	}

	// -- LongTask class:

	class LongTask extends AsyncTask<Integer, Float, Long> {

		// this will run first.
		// it will run on the UI thread
		// it's safe to change the UI
		@Override
		protected void onPreExecute() {

			TextView output = (TextView) findViewById(R.id.textOutput);
			output.setText("start");

		}

		// this will run on a BACKGROUND thread
		// cannot touch the UI here
		@Override
		protected Long doInBackground(Integer... params) {

			int number = params[0];
			long sum = 0;

			for (int i = 0; i < number; i++) {
				// do something
				sum += i;

				// we can set the progress
				// this will call the onProgressUpdate on the UI thread.
				publishProgress(100f * i / number);
			}

			return sum;
		}

		// this will be called when publishProgress() is called
		// this will run on the UI thread
		// it's safe to change the UI
		@Override
		protected void onProgressUpdate(Float... values) {

			Float progress = values[0];

			TextView output = (TextView) findViewById(R.id.textOutput);
			output.setText("progress : " + Math.round(progress) + "%");

		}

		// this will be called after doInBackground() is finished
		// and will receive the result
		// this will run on the UI thread
		// it's safe to change the UI
		@Override
		protected void onPostExecute(Long result) {
			TextView output = (TextView) findViewById(R.id.textOutput);
			output.setText("sum: " + result);
		}
	}
}

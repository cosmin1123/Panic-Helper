package ninja.PanicHelper.facebook;
/*Class required for Facebook Connectivity*/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import ninja.PanicHelper.R;

public class MemorizingActivity extends Activity
		implements OnClickListener,OnCancelListener {

	int decisionId;
	String app;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		Intent i = getIntent();
		app = i.getStringExtra(MemorizingTrustManager.DECISION_INTENT_APP);
		decisionId = i.getIntExtra(MemorizingTrustManager.DECISION_INTENT_ID, MTMDecision.DECISION_INVALID);
		String cert = i.getStringExtra(MemorizingTrustManager.DECISION_INTENT_CERT);

		new AlertDialog.Builder(this).setTitle(R.string.mtm_accept_cert)
			.setMessage(cert)
			.setPositiveButton(R.string.mtm_decision_always, this)
			.setNeutralButton(R.string.mtm_decision_once, this)
			.setNegativeButton(R.string.mtm_decision_abort, this)
			.setOnCancelListener(this)
			.create().show();
	}

	void sendDecision(int decision) {
		Intent i = new Intent(MemorizingTrustManager.DECISION_INTENT + "/" + app);
		i.putExtra(MemorizingTrustManager.DECISION_INTENT_ID, decisionId);
		i.putExtra(MemorizingTrustManager.DECISION_INTENT_CHOICE, decision);
		sendBroadcast(i);
		finish();
	}

	// react on AlertDialog button press
	public void onClick(DialogInterface dialog, int btnId) {
		int decision;
		dialog.dismiss();
		switch (btnId) {
		case DialogInterface.BUTTON_POSITIVE:
			decision = MTMDecision.DECISION_ALWAYS;
			break;
		case DialogInterface.BUTTON_NEUTRAL:
			decision = MTMDecision.DECISION_ONCE;
			break;
		default:
			decision = MTMDecision.DECISION_ABORT;
		}
		sendDecision(decision);
	}

	public void onCancel(DialogInterface dialog) {
		sendDecision(MTMDecision.DECISION_ABORT);
	}
}

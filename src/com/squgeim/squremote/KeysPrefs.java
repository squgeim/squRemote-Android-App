package com.squgeim.squremote;

//import android.app.AlertDialog;
//import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Spinner;
//import android.widget.Toast;

public class KeysPrefs extends DialogPreference {

	Resources res;
	Spinner frs, sec;
	String[] frs_vals;
	String[] sec_vals;
	String[] frs_names;
	String[] sec_names;
	Integer frs_pos, sec_pos;
	public KeysPrefs(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.pref);
		//setPersistent(false);
		res = context.getResources();
		frs_vals = res.getStringArray(R.array.mod);
		sec_vals = res.getStringArray(R.array.keyvals);
		frs_names = res.getStringArray(R.array.mod_names);
		sec_names = res.getStringArray(R.array.keys);
	}

/*	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		builder.setTitle("Select a keyboard sequence");
		super.onPrepareDialogBuilder(builder);
	}
*/	
	private int indexOf(String[] strs, String str) {
		for(int i=0;i<strs.length;i++) {
			if(strs[i].equals(str)) return i;
		}
		return -1;
	}
	
/*	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if(restorePersistedValue) {
			String current = this.getPersistedString("def");
			String[] vals = current.split("\\+");
			if(vals.length==1) {
				frs_pos = indexOf(frs_vals,"none");
				if(frs_pos==-1) frs_pos=0;
				sec_pos = indexOf(sec_vals,vals[0]);
				if(sec_pos==-1) sec_pos=0;
			}
			else {
				frs_pos = indexOf(frs_vals,vals[0]);
				if(frs_pos==-1) frs_pos=0;
				sec_pos = indexOf(sec_vals,vals[1]);
				if(sec_pos==-1) sec_pos=0;
			}
		}
		else {
			frs_pos=0;
			sec_pos=0;
			persistString((String) defaultValue);
		}
		super.onSetInitialValue(restorePersistedValue, defaultValue);
	}
*/	
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getString(index);
	}
	
	protected void onBindDialogView(View v) {
		frs = (Spinner) v.findViewById(R.id.pref_frs);
		sec = (Spinner) v.findViewById(R.id.pref_sec);
		String current = this.getPersistedString("def");
		if(current!="def") {
			String[] vals = current.split("\\+");
			if(vals.length==1) {
				frs_pos = indexOf(frs_vals,"none");
				if(frs_pos==-1) frs_pos=0;
				sec_pos = indexOf(sec_vals,vals[0]);
				if(sec_pos==-1) sec_pos=0;
			}
			else {
				frs_pos = indexOf(frs_vals,vals[0]);
				if(frs_pos==-1) frs_pos=0;
				sec_pos = indexOf(sec_vals,vals[1]);
				if(sec_pos==-1) sec_pos=0; 
			}
		}
		else {
			frs_pos=0;
			sec_pos=0;
		}
		frs.setSelection(frs_pos);
		sec.setSelection(sec_pos);
		//Toast.makeText(getContext(), frs_pos.toString()+" "+sec_pos.toString(), Toast.LENGTH_SHORT).show();
		super.onBindDialogView(v);
	}
	
	@Override
	protected void onDialogClosed(boolean result) {
		if(result) {
			int new_frs_pos = frs.getSelectedItemPosition();
			int new_sec_pos = sec.getSelectedItemPosition();
			String frs_val = frs_vals[new_frs_pos];
			String sec_val = sec_vals[new_sec_pos];
			//Toast.makeText(getContext(), frs_val+" "+sec_val, Toast.LENGTH_SHORT).show();
			String val="";
			if(!sec_val.equals("def")) {
				if(!frs_val.equals("none")) {
					val+=frs_val+"+";
				}
				val+=sec_val;
			}
			else val="def";
			//Toast.makeText(getContext(), val, Toast.LENGTH_SHORT).show();
			persistString(val);
			//Toast.makeText(getContext(), (ispersistant)?"true":"false", Toast.LENGTH_SHORT).show();
		}
		super.onDialogClosed(result);
	}
	
}

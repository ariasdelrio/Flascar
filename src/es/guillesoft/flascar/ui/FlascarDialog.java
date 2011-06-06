package es.guillesoft.flascar.ui;

import es.guillesoft.flascar.activity.FlascarActivity;
import android.app.Dialog;

public interface FlascarDialog {
	
	public Dialog build( FlascarActivity activity );
	
}

package it.unimib.buildyourholiday.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import it.unimib.buildyourholiday.R;

public class LogoutBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    Button confirm;
    Button cancel;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.botton_sheet_logout_layout,container,false);
        confirm = v.findViewById(R.id.confirm_logout_button);
        cancel = v.findViewById(R.id.negate_logout_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.oonButtonClicked("Yes");
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }
    public interface BottomSheetListener{
        void oonButtonClicked(String text);
    }

    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement BottomSheetListener");
        }
    }
}

package it.unimib.buildyourholiday.util;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import it.unimib.buildyourholiday.R;

public class LogoutBottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_logout_layout, container, false);
        Button yesButton = v.findViewById(R.id.confirm_logout_button);
        Button noButton = v.findViewById(R.id.negate_logout_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Yes");
                dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("No");
                dismiss();
            }
        });
        return v;
    }

    public interface BottomSheetListener{
        void onButtonClicked(String action);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mListener = (BottomSheetListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException((context.toString() + ""));
        }

    }
}

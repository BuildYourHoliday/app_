package it.unimib.buildyourholiday.util;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import it.unimib.buildyourholiday.R;

public class ProfilePicBottomSheetDialog extends BottomSheetDialogFragment {

    private LogoutBottomSheetDialog.BottomSheetListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_profile_pic_layout, container, false);
        Button addPhotoButton = v.findViewById(R.id.add_image_profile_button);
        Button removePhotoButton = v.findViewById(R.id.remove_image_profile_button);


        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Remove");
                dismiss();
            }
        });
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Add");
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
            mListener = (LogoutBottomSheetDialog.BottomSheetListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException((context.toString() + ""));
        }
    }

}

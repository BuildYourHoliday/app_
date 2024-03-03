package it.unimib.buildyourholiday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProfilePicBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    Button setImage;
    Button removeImage;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.botton_sheet_profile_pic_layout,container,false);
        setImage = v.findViewById(R.id.add_image_profile_button);
        removeImage = v.findViewById(R.id.remove_image_profile_button);
        setImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.oonButtonClicked("Add");
                dismiss();
            }
        });
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.oonButtonClicked("Remove");
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

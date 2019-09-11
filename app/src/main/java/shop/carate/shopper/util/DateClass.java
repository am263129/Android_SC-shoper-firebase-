//package shop.carate.shopper.util;
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//
//import androidx.annotation.NonNull;
//import com.google.firebase.database.annotations.Nullable;
//
//import java.util.Calendar;
//
//import shop.carate.shopper.R;
//
//public class DateClass extends DialogFragment {
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        return new DatePickerDialog(getActivity(),
//                R.style.DialogTheme,
//                (DatePickerDialog.OnDateSetListener) getActivity(),
//                year, month, day);
//    }
//}
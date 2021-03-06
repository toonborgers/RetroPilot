package be.cegeka.retrobox.presentation.newretro.retrodetail;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import be.cegeka.retrobox.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

import static be.cegeka.retrobox.infrastructure.BeanProvider.retroCreationContext;

public class NewRetroDetailFragment extends Fragment implements CalendarDatePickerDialog.OnDateSetListener, RadialTimePickerDialog.OnTimeSetListener {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm");

    @InjectView(R.id.new_retro_name)
    EditText etName;

    @InjectView(R.id.new_retro_date)
    TextView tvDate;

    @InjectView(R.id.new_retro_time)
    TextView tvTime;

    @InjectView(R.id.new_retro_place)
    EditText etPlace;

    @InjectView(R.id.new_retro_done)
    Button btDone;

    private FragmentManager supportFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        this.supportFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        this.supportFragmentManager = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_retro, container, false);
        ButterKnife.inject(this, view);
        setupDateAndTimeFields();
        setupDoneButton();
        setUpNameAndPlaceFields();
        return view;
    }

    private void setupDateAndTimeFields() {
        LocalDateTime now = LocalDateTime.now();
        tvDate.setText(DATE_FORMAT.print(now));
        tvTime.setText(TIME_FORMAT.print(now));

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
    }

    private void setupDoneButton() {
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPlace.hasFocus()) {
                    etPlace.clearFocus();
                }
                if (etName.hasFocus()) {
                    etName.clearFocus();
                }
                if (!retroCreationContext().currentRetroIsValid()) {
                    Toast.makeText(getActivity(), R.string.new_retro_name_error, Toast.LENGTH_LONG).show();
                } else {
                    if (retroCreationContext().storeCurrentRetro()) {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                        retroCreationContext().doneCreatingNewRetro();
                    }
                }
            }
        });
    }

    private void setUpNameAndPlaceFields() {
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    retroCreationContext().retroNameSet(etName.getText().toString().trim());
                }
            }
        });
        etPlace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    retroCreationContext().retroPlaceSet(etPlace.getText().toString().trim());
                }
            }
        });
    }

    private void showDatePicker() {
        LocalDateTime now = LocalDateTime.now();
        CalendarDatePickerDialog dialog = CalendarDatePickerDialog.newInstance(this, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
        dialog.setYearRange(now.getYear(), now.getYear() + 100);
        dialog.show(supportFragmentManager, "date_picker_fragment");
    }

    private void showTimePicker() {
        LocalDateTime now = LocalDateTime.now();
        RadialTimePickerDialog dialog = RadialTimePickerDialog.newInstance(this, now.getHourOfDay(), now.getMinuteOfHour(), true);
        dialog.show(supportFragmentManager, "time_picker_fragment");
    }


    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        LocalDate parsedDate = new LocalDate(year, monthOfYear + 1, dayOfMonth);
        if (parsedDate.isBefore(new LocalDate())) {
            Toast.makeText(getActivity(), R.string.new_retro_date_error, Toast.LENGTH_LONG).show();
        } else {
            tvDate.setText(DATE_FORMAT.print(parsedDate));
            retroCreationContext().retroDateSet(parsedDate);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        LocalTime parsedTime = new LocalTime(hourOfDay, minute);
        tvTime.setText(TIME_FORMAT.print(parsedTime));
        retroCreationContext().retroTimeSet(parsedTime);
    }
}

package com.zybooks.pizzaparty;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final int SLICES_PER_PIZZA = 8;

    private EditText mNumAttendEditText;
    private TextView mNumPizzasTextView;
    private Spinner mHowHungrySpinner;

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate was called");

        // Assign the widgets to fields
        mNumAttendEditText = findViewById(R.id.attendEditText);
        mNumPizzasTextView = findViewById(R.id.answerTextView);
        mHowHungrySpinner = findViewById(R.id.hungrySpinner);

        // TextWatcher for attendEditText ie. mNumAttendEditText
        mNumAttendEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            int i = 0;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNumPizzasTextView.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hunger_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mHowHungrySpinner.setAdapter(adapter);
        mHowHungrySpinner.setSelection(0,false);
        mHowHungrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
                mNumPizzasTextView.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void calculateClick(View view) {
        // Get how many are attending the party
        int numAttend;
        try {
            String numAttendStr = mNumAttendEditText.getText().toString();
            numAttend = Integer.parseInt(numAttendStr);
        }
        catch (NumberFormatException ex) {
            numAttend = 0;
        }

        // Get hunger level selection
        String checkedItem = mHowHungrySpinner.getSelectedItem().toString();
        PizzaCalculator.HungerLevel hungerLevel = PizzaCalculator.HungerLevel.RAVENOUS;
        if (checkedItem.equals("Light")) {
            hungerLevel = PizzaCalculator.HungerLevel.LIGHT;
        }
        else if (checkedItem.equals("Medium")) {
            hungerLevel = PizzaCalculator.HungerLevel.MEDIUM;
        }

        // Get the number of pizzas needed
        PizzaCalculator calc = new PizzaCalculator(numAttend, hungerLevel);
        int totalPizzas = calc.getTotalPizzas();

        // Place totalPizzas into the string resource and display
        String totalText = getString(R.string.total_pizzas, totalPizzas);
        mNumPizzasTextView.setText(totalText);
    }
}

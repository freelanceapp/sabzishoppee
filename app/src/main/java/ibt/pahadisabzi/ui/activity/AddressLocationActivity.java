package ibt.pahadisabzi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import ibt.pahadisabzi.R;
import ibt.pahadisabzi.adapter.MyPlacesAdapter;
import ibt.pahadisabzi.ui.MyGooglePlaces;

public class AddressLocationActivity extends AppCompatActivity {
    AutoCompleteTextView places;
    MyPlacesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_location);

        places=(AutoCompleteTextView)findViewById(R.id.places);
        adapter=new MyPlacesAdapter(AddressLocationActivity.this);
        places.setAdapter(adapter);
        // text changed listener to get results precisely according to our search
        places.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            //calling getfilter to filter the results
                adapter.getFilter().filter(s);
            //notify the adapters after results changed
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
            // handling click of autotextcompleteview items
        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyGooglePlaces googlePlaces=(MyGooglePlaces)parent.getItemAtPosition(position);
                places.setText(googlePlaces.getName());
            }
        });

    }
}
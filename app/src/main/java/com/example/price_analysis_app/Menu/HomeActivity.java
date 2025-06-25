package com.example.price_analysis_app.Menu;

import com.example.price_analysis_app.Account.Account;
import com.example.price_analysis_app.Account.BookmarkActivity;
import com.example.price_analysis_app.Account.SessionManager;
import com.example.price_analysis_app.Items.Icallable;
import com.example.price_analysis_app.Items.Item;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.price_analysis_app.Items.ItemAdapter;
import com.example.price_analysis_app.Items.ItemDisplay;
import com.example.price_analysis_app.Links.Link;

import com.example.price_analysis_app.R;
import com.example.price_analysis_app.TempMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeActivity extends DrawerActivity implements Icallable {
    private EditText searchBar;
//comments close button, add more sites, more products,change comment to have an id, name should not be hard coded for collections

    public HomeActivity() throws MalformedURLException {
    }

    private Button button;
    private FirebaseFirestore db;
    private static List<Item> itemList = new ArrayList<>();
    private static List<Item> filteredItems = new ArrayList<>();
    private RecyclerView rvItems;
    private ItemAdapter itemAdapter;
    private Spinner optionsSp;
    private Button menuTestButton;
    Account account = SessionManager.getCurrentAccount();
    private Button compareFab;
    private String selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get database
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        EdgeToEdge.enable(this);

        //link activity
        setContentView(R.layout.activity_home);
        //list of house appliances
        List<String> collectionNames = Arrays.asList(new String("combine frigorifice"),
                new String("aspiratoare"),
                new String("cuptoare cu microunde"),
                new String("espressoare automate"),
                new String("frigidere"),
                new String("masini de spalat rufe")
        );
        optionsSp = findViewById(R.id.spinnerOptions);
        //create adapter

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_item, R.id.spinner_item_text, collectionNames);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        optionsSp.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        //test button for menu
        menuTestButton = findViewById(R.id.buttonMenu);


        menuTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDrawer();
            }
        });

//        Button bookmarkButton = findViewById(R.id.BOOKMARKS);
//        bookmarkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BookmarkedActivity();
//            }
//
//        });


//search bar
        searchBar = findViewById(R.id.searchBar);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                filteredItems.clear();
                //search at every letter typed
                itemAdapter.notifyDataSetChanged();
                if (query.isEmpty()) {
                    filteredItems.addAll(itemList);

                } else {

                    query = query.toLowerCase();
                    for (Item item : itemList) {
                        if (item.getName() != null) {
                            if (item.getName().toLowerCase().contains(query)) {
                                filteredItems.add(item);
                            }
                        }

                    }
                }
//                for(Item item : filteredItems){
//                    System.out.println(item.getName());
//                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //link the list to the adapter and display them
        filteredItems.addAll(itemList);
        rvItems = findViewById(R.id.rvItems);
        itemAdapter = new ItemAdapter(this, filteredItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(itemAdapter);

        //open activty item_display after clicking one item
        optionsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filteredItems.clear();
                String selectedCollection = parent.getItemAtPosition(position).toString();
                retrieveItems(selectedCollection);
                selectedOption = selectedCollection;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList.stream().map(Item::getName).distinct().collect(Collectors.toList()));

        rvItems.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Intent intent = new Intent(HomeActivity.this, ItemDisplay.class);
                int position = rv.getChildAdapterPosition(rv.findChildViewUnder(e.getX(), e.getY()));
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                intent.putExtra("selectedObject", filteredItems.get(position));
                intent.putExtra("selectedOption", selectedOption);

                //System.out.println("before being sent "+filteredItems.get(position).getLinkList().get(0).toString());
                startActivity(intent);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        compareFab = findViewById(R.id.compareFab);
        compareFab.setOnClickListener(v -> {
            Intent i = new Intent(this, CompareActivity.class);
            startActivity(i);
        });
        updateCompareButtonVisibility();

    }
    public void updateCompareButtonVisibility() {
        compareFab.setVisibility(
                ComparisonManager.getInstance().getSelectedItems().size() == 2
                        ? View.VISIBLE : View.GONE
        );
    }
    //item retrieving functionality from the database --maybe change
    private void retrieveItems(String collection) {
        new Thread(() -> {
            db.collection(collection).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    itemList.clear();
                    System.out.println("searched");
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        List<String> g = (ArrayList<String>) documentSnapshot.get("linkList");

                        String name = (String) documentSnapshot.get("name");
                        String imgUrl = (String) documentSnapshot.get("imgUrl");
                        String productCode = (String) documentSnapshot.get("productCode");
                        String technicalChar = (String) documentSnapshot.get("technicalChar");
                        String documentId = (String) documentSnapshot.getId();
                        List<Link> links = new ArrayList<>();
                        for (String h : g) {
                            Link temp = fromString(h);
                            links.add(temp);
                        }
                        Item item = new Item(name, productCode, links, imgUrl, technicalChar, documentId);
                        itemList.add(item);
                        // Log.d("Firestore",g.toString());
                        //itemList.add(item);
                    }
                    filteredItems.clear();
                    filteredItems.addAll(itemList);
                    itemAdapter.notifyDataSetChanged();

                } else {
                    Log.w("FireStore", "Error getting items", task.getException());
                }

            });
        }).start();
    }

    //transforming item from string
    public Link fromString(String linkString) {
        // → match the URL
        Pattern siteLinkPattern = Pattern.compile("siteLink='(https?://[^']+)'");
        // → match the name
        Pattern namePattern     = Pattern.compile("name='([^']*)'");
        // → match price, allowing digits, dots, commas, optional whitespace, then Lei or RON
        Pattern pricePattern    = Pattern.compile("price='([0-9.,]+)\\s*(?:Lei|RON)'");

        Matcher siteLinkMatcher = siteLinkPattern.matcher(linkString);
        Matcher nameMatcher     = namePattern.matcher(linkString);
        Matcher priceMatcher    = pricePattern.matcher(linkString);

        URL    siteLink = null;
        String name     = null;
        double price    = 0.0;

        // extract URL
        if (siteLinkMatcher.find()) {
            try {
                siteLink = new URL(siteLinkMatcher.group(1));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        // extract name
        if (nameMatcher.find()) {
            name = nameMatcher.group(1);
        }

        // extract price
        if (priceMatcher.find()) {
            // group(1) is something like "1.299,99"
            String raw = priceMatcher.group(1)
                    .replace(".", "")    // remove thousands separator
                    .replace(",", ".");  // convert decimal comma to dot
            try {
                price = Double.parseDouble(raw);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return new Link(siteLink, name, price);
    }



    //debugging info
    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, ItemDisplay.class);
        intent.putExtra("selectedObject", filteredItems.get(position));
        intent.putExtra("selectedOption", selectedOption);
        startActivity(intent);
    }

    public void testDrawer() {
        Intent intent = new Intent(this, TempMenu.class);
        startActivity(intent);
    }



    @Override
    public void onLinkClicked(Link position) {

    }

    public void BookmarkedActivity() {
        Intent intent = new Intent(this, BookmarkActivity.class);
        startActivity(intent);
    }

}
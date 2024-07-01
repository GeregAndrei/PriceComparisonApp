package com.example.price_analysis_app.uiStuff;
import com.example.price_analysis_app.Items.Icallable;
import com.example.price_analysis_app.Items.Item;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.price_analysis_app.Items.ItemAdapter;
import com.example.price_analysis_app.Items.ItemDisplay;
import com.example.price_analysis_app.Items.Link;

import com.example.price_analysis_app.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity implements Icallable {
    private EditText searchBar;





    public HomeActivity() throws MalformedURLException {
    }
    private Button button;
    private FirebaseFirestore db;
    private static List<Item> itemList=new ArrayList<>();
    private static List<Item> filteredItems=new ArrayList<>();
    private RecyclerView rvItems;
    private ItemAdapter itemAdapter;
    private Spinner optionsSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=FirebaseFirestore.getInstance();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        List<String> collectionNames = Arrays.asList(new String("combine_frigorifice"),new String("masini_spalat_rufe"),new String("cuptoare_incorporabile"));
        optionsSp=findViewById(R.id.spinnerOptions);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, collectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSp.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int i=0;

        searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                filteredItems.clear();
                itemAdapter.notifyDataSetChanged();
                if(query.isEmpty()){
                    filteredItems.addAll(itemList);

                }else{

                    query=query.toLowerCase();
                    for(Item item : itemList){
                        if(item.getName() != null){
                            if (item.getName().toLowerCase().contains(query)){
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
        filteredItems.addAll(itemList);
        rvItems=findViewById(R.id.rvItems);

        itemAdapter=new ItemAdapter(this,filteredItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(itemAdapter);
        optionsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filteredItems.clear();
                String selectedCollection = parent.getItemAtPosition(position).toString();
                retrieveItems(selectedCollection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList.stream().map(Item::getName).distinct().collect(Collectors.toList()));
    }
    private void retrieveItems(String collection){
        db.collection(collection).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                itemList.clear();
                System.out.println("searched");
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                   List<String> g=(ArrayList<String>)documentSnapshot.get("linkList");

                   String name = (String)documentSnapshot.get("name");
                   String imgUrl = (String)documentSnapshot.get("imgUrl");
                   String productCode=(String)documentSnapshot.get("productCode");
                   String technicalChar=(String)documentSnapshot.get("technicalChar");
                   List<Link>links=new ArrayList<>();
                   for(String h : g){
                       Link temp=fromString(h);
                       links.add(temp);
                   }
                   Item item = new Item(name,productCode,links,imgUrl,technicalChar);
                   itemList.add(item);
                   // Log.d("Firestore",g.toString());
                    //itemList.add(item);
                }
                itemAdapter.notifyDataSetChanged();

            }else{
                Log.w("FireStore","Error getting items",task.getException());
            }

        });

    }
    public Link fromString(String linkString){
        //"Link{siteLink='https://www.google.com/', name='among usV3', price='50.0 Lei'}"

        Pattern siteLinkPattern=Pattern.compile("siteLink='(https?://[^']+)'");
        Pattern namePattern = Pattern.compile("name='([^']*)'");
        Pattern pricePattern = Pattern.compile("price='([0-9.,]+) Lei'");
        Pattern pricePattern2 = Pattern.compile("price='([0-9.,]+) RON'");
        Matcher siteLinkMatcher = siteLinkPattern.matcher(linkString);
        Matcher nameMatcher = namePattern.matcher(linkString);
        Matcher priceMatcher = pricePattern.matcher(linkString);
        Matcher priceMatcher2 =pricePattern2.matcher(linkString);
        URL siteLink=null;
        String name = null;
        double price = 0.0;
        if (siteLinkMatcher.find()) {
            try {
                siteLink = new URL(siteLinkMatcher.group(1));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        // Extract name
        if (nameMatcher.find()) {
            name = nameMatcher.group(1);
        }

        // Extract price
        if (priceMatcher.find()) {

            String priceString = priceMatcher.group(1).replace(".","").replace(",",".");

            try{
                price = Double.parseDouble(priceString);
             //   System.out.println("PRICE IS "+price);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        if (priceMatcher2.find()) {

            String priceString = priceMatcher2.group(1).replace(".","").replace(",",".");

            try{
                price = Double.parseDouble(priceString);
                //   System.out.println("PRICE IS "+price);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

        Link result = new Link(siteLink,name,price);

        return result;
    }



    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, ItemDisplay.class);
        intent.putExtra("selectedObject",filteredItems.get(position));
        System.out.println("before being sent "+filteredItems.get(position).getLinkList().get(0).toString());
        startActivity(intent);
    }

    @Override
    public void onLinkClicked(Link position) {

    }


}
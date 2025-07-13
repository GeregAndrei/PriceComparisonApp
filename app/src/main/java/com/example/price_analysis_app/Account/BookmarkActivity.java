package com.example.price_analysis_app.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.price_analysis_app.Items.Item;
import com.example.price_analysis_app.Items.ItemDisplay;
import com.example.price_analysis_app.Links.Link;
import com.example.price_analysis_app.R;
import com.example.price_analysis_app.Menu.DrawerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookmarkActivity extends DrawerActivity implements Icallable2 {
    Account account;
    List<Item> bookmarkedItems = new ArrayList<>();
    List<TemporaryClass> temporaryClassList=new ArrayList<>();
    RecyclerView rvBookmarks;
    BookmarkAdapter itemAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bookmark);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        account = SessionManager.getCurrentAccount();
        rvBookmarks = findViewById(R.id.rvBookmarks);
        rvBookmarks.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();


        itemAdapter = new BookmarkAdapter(this, bookmarkedItems);
        rvBookmarks.setAdapter(itemAdapter);

        if (account == null) {
            Log.e("BookmarkActivity", "Account is null");
            Toast.makeText(this, "Not logged In", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = mAuth.getUid();

        DocumentReference accountRef = db.collection("conturi").document(currentUserId);
        accountRef.addSnapshotListener((snapshot, e) -> {
            if (e != null || snapshot == null || !snapshot.exists()) {

                Log.e("BookmarkActivity", "Error getting account document", e);
                return;
            }

            List<String> bookmarkedIds = (List<String>) snapshot.get("bookmarkedItems");
            if (bookmarkedIds == null) {
                bookmarkedIds = new ArrayList<>();
            }

            loadBookmarkedItems(bookmarkedIds);
        });


    }

    private void loadBookmarkedItems(List<String> bookmarkedIds) {

        bookmarkedItems.clear();
        temporaryClassList.clear();
        if (bookmarkedIds.isEmpty()) {
            itemAdapter.notifyDataSetChanged();
            return;
        }
        List<String> collectionNames = Arrays.asList(new String("combine frigorifice"),
                new String("aspiratoare"),
                new String("cuptoare cu microunde"),
                new String("espressoare automate"),
                new String("frigidere"),
                new String("masini de spalat rufe")
        );

        for (int i =0; i <collectionNames.size();i++) {
            db.collection(collectionNames.get(i).toString())
                    .whereIn(FieldPath.documentId(), bookmarkedIds)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot documentSnapshot : querySnapshot) {
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

                            bookmarkedItems.add(item);
                            temporaryClassList.add(new TemporaryClass(item, documentSnapshot.getReference()));

                        }
                        itemAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("BookmarkActivity", "Error getting documents: ", e);
                    });


        }
    }
    public Link fromString(String linkString) {
        // match the URL
        Pattern siteLinkPattern = Pattern.compile("siteLink='(https?://[^']+)'");
        // match the name
        Pattern namePattern     = Pattern.compile("name='([^']*)'");
        // match price, allowing digits, dots, commas, optional whitespace, then Lei or RON
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

    @Override
    public void onItemClicked2(int position) {
        Intent intent = new Intent(this, ItemDisplay.class);
        Item selectedItem = bookmarkedItems.get(position);
        intent.putExtra("selectedObject", selectedItem);
        DocumentReference doc = temporaryClassList.get(position).getDocRef();
        String collectionName = doc.getParent().getId();

        intent.putExtra("selectedOption", collectionName);

        startActivity(intent);
    }

    @Override
    public void onLinkClicked2(Link position) {

    }
}
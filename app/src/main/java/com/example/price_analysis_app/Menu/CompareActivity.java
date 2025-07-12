package com.example.price_analysis_app.Menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.price_analysis_app.Items.Item;
import com.example.price_analysis_app.Links.Link;
import com.example.price_analysis_app.OpenAI.ChatGPTClient;
import com.example.price_analysis_app.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.price_analysis_app.OpenAI.ChatGPTService;

public class CompareActivity extends AppCompatActivity {
    private ImageView imageLeft, imageRight;
    private TextView comparisonText;
    private Button buyLeftBtn, buyRightBtn;
    private Button explainAllBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        imageLeft = findViewById(R.id.imageLeft);
        imageRight = findViewById(R.id.imageRight);
        buyLeftBtn = findViewById(R.id.buyLeftBtn);
        buyRightBtn = findViewById(R.id.buyRightBtn);
        comparisonText = findViewById(R.id.comparisonText);
        explainAllBtn   = findViewById(R.id.btnExplainAll);

        List<Item> items = ComparisonManager.getInstance().getSelectedItems();
        if (items.size() != 2) {
            finish();
            return;
        }
        Item left = items.get(0);
        Item right = items.get(1);

        // Load images
        Glide.with(this).load(left.getImgUrl()).into(imageLeft);
        Glide.with(this).load(right.getImgUrl()).into(imageRight);

        // Determine lowest-price link for each item
        Link lowestLeft = Collections.min(
                left.getLinkList(), Comparator.comparingDouble(Link::getPrice)
        );
        Link lowestRight = Collections.min(
                right.getLinkList(), Comparator.comparingDouble(Link::getPrice)
        );

        // Configure Buy buttons
        buyLeftBtn.setText(
                String.format("Buy at %s for %.2f", lowestLeft.getSiteName(), lowestLeft.getPrice())
        );
        buyLeftBtn.setOnClickListener(v -> {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(lowestLeft.getSiteLink().toString())
            ));
        });

        buyRightBtn.setText(
                String.format("Buy at %s for %.2f", lowestRight.getSiteName(), lowestRight.getPrice())
        );
        buyRightBtn.setOnClickListener(v -> {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(lowestRight.getSiteLink().toString())
            ));
        });

        // Build ChatGPT prompt using lowest prices
        String prompt = String.format(
                "Analyze the technical specifications and lowest available prices of two products, and provide a detailed review with pros and cons for each, followed by a comparative summary and recommendation: " +
                        "Product A: %s " +
                        "Specifications: %s " +
                        "Lowest Price: %.2f on %s " +
                        "Product B: %s " +
                        "Specifications: %s " +
                        "Lowest Price: %.2f on %s ",
                left.getName(), left.getTechnicalChar(), lowestLeft.getPrice(), lowestLeft.getSiteName(),
                right.getName(), right.getTechnicalChar(), lowestRight.getPrice(), lowestRight.getSiteName()
        );


        // Call ChatGPT
        ChatGPTClient.getInstance()
                .compare(prompt, responseText -> comparisonText.setText(responseText));

        explainAllBtn.setOnClickListener(v -> {
            // build the detailed prompt by re-using the existing comparison text

            String detailedPrompt = new StringBuilder()
                    .append("Here are the pros and cons for both products:\n\n")
                    .append(comparisonText.getText().toString())
                    .append("\n\nPlease explain each pro and con in detail in this format  " +
                            "product name\n"+
                            " \"Pros:\\n\" +\n" +
                            "  \"1. <Short title of pro>: <Your explanation>\\n\\n\" +\n" +
                            "  \"2. <Short title of pro>: <Your explanation>\\n\\n\" +\n" +
                            "  \"Cons:\\n\" +\n" +
                            "  \"1. <Short title of con>: <Your explanation>\\n\\n\" +\n" +
                            "  \"2. <Short title of con>: <Your explanation>\\n\\n\" +" +
                            "do the same for the second product " +
                            ", then give the same overall conclusion.")
                    .toString();

            // send it to GPT and update the TextView when it comes back
            ChatGPTClient.getInstance()
                    .compare(detailedPrompt, detailedResponse ->
                            //post callback to the main thread
                            comparisonText.setText(detailedResponse)
                    );
            comparisonText.setText("please wait");
        });

    }

}
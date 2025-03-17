package com.example.price_analysis_app.comments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.price_analysis_app.Account.Account;
import com.example.price_analysis_app.Account.SessionManager;
import com.example.price_analysis_app.Items.Item;
import com.example.price_analysis_app.R;

public class CommentsActivity extends AppCompatActivity {
    private String productCode;
    private Item item;

private TextView name;
private RatingBar ratingBar;
private EditText commentBody;
private Button createComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Account currentAccount = SessionManager.getCurrentAccount();
        String accountName = currentAccount.getUsername();

        String accountId = currentAccount.getId();


        ratingBar=findViewById(R.id.commentRating);
        commentBody =findViewById(R.id.commentBody);
        createComment=findViewById(R.id.buttonCreateComment);
        createComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = getIntent().getParcelableExtra("itemforcomment");
                String productCode = (item != null) ? item.getProductCode() : "";
                Comment comment = new Comment(accountName,accountId, productCode, commentBody.getText().toString(), ratingBar.getRating());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newComment", comment);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
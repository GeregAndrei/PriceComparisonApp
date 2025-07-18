package com.example.price_analysis_app.Items;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.price_analysis_app.Account.Account;
import com.example.price_analysis_app.Account.SessionManager;
import com.example.price_analysis_app.Links.Link;
import com.example.price_analysis_app.Links.LinkAdapter;
import com.example.price_analysis_app.OpenAI.ChatMessage;
import com.example.price_analysis_app.OpenAI.ChatRequest;
import com.example.price_analysis_app.OpenAI.ChatResponse;
import com.example.price_analysis_app.OpenAI.ChatGPTService;
import com.example.price_analysis_app.comments.Comment;
import com.example.price_analysis_app.comments.CommentAdapter;
import com.example.price_analysis_app.R;
import com.example.price_analysis_app.comments.CommentsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jsoup.Jsoup;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Converter.Factory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ItemDisplay extends AppCompatActivity implements Icallable {
    private RecyclerView linkList;
    private Account currentAccount=SessionManager.getCurrentAccount();
    private TextView name;
    private TextView specs;
    private ImageView img;
    private Button closeCommentButton;
    private boolean isCommentsInflated = false;
    private CommentAdapter commentAdapter;
    private View commentsView;
    private Context context;
    private ActivityResultLauncher<Intent> commentActivityLauncher;
    private FirebaseFirestore db;
    String selectedCollection;
   private static List<Comment> commentList = new ArrayList<>();
    private View analysisView;
   List<String> bookmarks = new ArrayList<>();
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ChatGPTService chatGPTService = retrofit.create(ChatGPTService.class);

    public interface ChatGPTCallback {
        void onResponse(String response);
        void onError(Throwable t);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_display);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectedCollection = getIntent().getStringExtra("selectedOption");
        // Retrieve the selected item passed from the previous activity
        Item selectedObject = (Item) getIntent().getParcelableExtra("selectedObject");

        Log.d("UUUUUUUUUUUUUUU", selectedObject.getDocId());
        Log.d("UUUUUUUU",selectedObject.getLinkList().toString());
        name = findViewById(R.id.titleItemTv);
        img = findViewById(R.id.imageView2);
        specs = findViewById(R.id.buttonTechnical);
        specs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailView(selectedObject);
            }
        });


        commentAdapter = new CommentAdapter(this, commentList);
        Account currentAccount = SessionManager.getCurrentAccount();
        Button bookmarkItem = findViewById(R.id.bookmarkButton);
        bookmarkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBookmark(selectedObject,mAuth);
            }
        });



        linkList=findViewById(R.id.linkListRv);
        linkList.setLayoutManager(new LinearLayoutManager(this));
        LinkAdapter linkAdapter=new LinkAdapter(this,selectedObject.getLinkList());
        linkList.setAdapter(linkAdapter);
        name.setText(selectedObject.name);
        //url to img library
        Glide.with(this).load(selectedObject.getImgUrl()).into(img);
        if(selectedObject.name ==null){
            Log.d("LOL",selectedObject.toString());
            }

       Button showCommentsButton = findViewById(R.id.showCommentsButton);

        showCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCommentsInflated) {
                    fetchCommentsFromFirestore(selectedCollection, selectedObject.getDocId());
                } else {
                    boolean nowVisible = commentsView.getVisibility() != View.VISIBLE;
                    commentsView.setVisibility(nowVisible ? View.VISIBLE : View.GONE);
                    closeCommentButton.setVisibility(nowVisible ? View.VISIBLE : View.GONE);
                }
            }
        });

         closeCommentButton = findViewById(R.id.buttonCloseComment);
        closeCommentButton.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       commentsView.setVisibility(View.GONE);

                                                       closeCommentButton.setVisibility(View.GONE);
                                                   }
                                               });

        Button addcomment =findViewById(R.id.buttonAddComment);
        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addComment(selectedObject);
            }
        });


        commentActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Comment newComment = data.getParcelableExtra("newComment");


                            selectedObject.addComment(newComment);
                            commentAdapter.notifyDataSetChanged();
                            pushCommentToFirebase(selectedObject.getDocId(), newComment);
                        }
                    }
                }
        );



        Button analyzeButton = findViewById(R.id.analyzeButton);

        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnalysis(selectedObject);
            }
        });


    }
private void pushCommentToFirebase(String documentId, Comment comment) {
    DocumentReference docRef = db.collection(selectedCollection).document(documentId);
    Map<String, Object> commentMap = new HashMap<>();
    commentMap.put("accountName", comment.getAccountName());
    commentMap.put("accountId", currentAccount.getId());
    commentMap.put("accountId", comment.getAccountId());
    commentMap.put("description", comment.getDescription());
    commentMap.put("bar", comment.getBar());

    docRef.update("comments", FieldValue.arrayUnion(commentMap))
            .addOnSuccessListener(aVoid -> Log.d("Firestore", "Comment added successfully"))
            .addOnFailureListener(e -> Log.e("Firestore", "Error adding comment", e));
}



    private void addComment(Item selectedObject){
        Intent intent =new Intent(ItemDisplay.this, CommentsActivity.class);
        intent.putExtra("itemforcomment",selectedObject);
        commentActivityLauncher.launch(intent);
    }

    private void fetchCommentsFromFirestore(String collectionName, String docId) {
        db.collection(collectionName)
                .document(docId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Log.d("aaa", "No such document");
                        Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<Map<String, Object>> commentsField =
                            (List<Map<String, Object>>) documentSnapshot.get("comments");
                    if (commentsField == null || commentsField.isEmpty()) {
                        Toast.makeText(this, "No comments available for this item", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // build Comment list
                    commentList.clear();
                    for (Map<String, Object> commentMap : commentsField) {
                        String accountName = (String) commentMap.get("accountName");
                        String description = (String) commentMap.get("description");
                        Number barNumber   = (Number) commentMap.get("bar");
                        float bar = barNumber != null ? barNumber.floatValue() : 0f;
                        commentList.add(new Comment(accountName, currentAccount.getId(), docId, description, bar));
                    }

                    // only now inflate the stub
                    if (!isCommentsInflated) {
                        ViewStub stub = findViewById(R.id.commentsViewStub);
                        commentsView = stub.inflate();
                        RecyclerView commentsRv = commentsView.findViewById(R.id.commentsRv);

                        commentsRv.setLayoutManager(new LinearLayoutManager(this));
                        commentsRv.setAdapter(commentAdapter);
                        isCommentsInflated = true;
                        closeCommentButton.setVisibility(View.VISIBLE);
                    }

                    // ensure it's visible
                    commentsView.setVisibility(View.VISIBLE);
                    commentAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {
                    Log.e("aaa", "Error fetching comments", e);
                    Toast.makeText(this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                });
    }

    public void toggleBookmark (Item selectedObject, FirebaseAuth mAuth) {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            Log.e("Firestore", "User not authenticated.");
            return;
        }
        String uid = firebaseUser.getUid();
        DocumentReference accountDocRef = db.collection("conturi").document(uid);
        accountDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> bookmarks = (List<String>) documentSnapshot.get("bookmarkedItems");

                        if (bookmarks != null && bookmarks.contains(selectedObject.getDocId())) {
                                accountDocRef.update("bookmarkedItems", FieldValue.arrayRemove(selectedObject.getDocId()))
                                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Item removed from bookmarks"))
                                    .addOnFailureListener(e -> Log.e("Firestore", "Error removing item from bookmarks", e));
                        } else {
                            accountDocRef.update("bookmarkedItems", FieldValue.arrayUnion(selectedObject.getDocId()))
                                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Item added to bookmarks"))
                                    .addOnFailureListener(e -> Log.e("Firestore", "Error adding item to bookmarks", e));
                        }
                    } else {
                        Log.e("Firestore", "Account document does not exist.");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching account document", e));
    }

    private void openDetailView(Item selectedObject) {
        Intent intent = new Intent(this, Technical.class);
        intent.putExtra("selectedObject",selectedObject);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(int position) {


    }

    @Override
    public void onLinkClicked(Link item) {
            String url = item.getSiteLink().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);

    }

    private void showAnalysis(Item selectedObject) {

        if(analysisView == null){
        ViewStub stub = findViewById(R.id.analysisStub);
       analysisView = stub.inflate();

       Log.d("UUUUUUUUUUUUUUU", selectedObject.getName().toString());
        Button closeButton = analysisView.findViewById(R.id.closeAnalysis);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnalysis();
            }
        });
    } else {
        analysisView.setVisibility(View.VISIBLE);
    }
        final ProgressBar loadingIndicator = analysisView.findViewById(R.id.loadingIndicator);
        final TextView analysisTextView = analysisView.findViewById(R.id.analysisTextView);
        loadingIndicator.setVisibility(View.VISIBLE);

        String technicalHtml = selectedObject.getTechnicalChar();
        String plainTechnicalDetails = Jsoup.parse(technicalHtml).text();


        String prompt = "Analyze the following product technical specifications and provide a detailed review with pros and cons. " +
                "List reasons why the product is good and potential issues it might have:\n\n" + plainTechnicalDetails;


        getChatGPTResponse(new ChatGPTCallback() {
            @Override
            public void onResponse(String response) {
                loadingIndicator.setVisibility(View.GONE);
               analysisTextView.setText(response);
            }

            @Override
            public void onError(Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Log.e("HUUUUH",t.toString());
                analysisTextView.setText("Error fetching analysis.");

            }
        }, prompt);
    }

    private void getChatGPTResponse(final ChatGPTCallback callback, String prompt) {
        ChatMessage systemMessage = new ChatMessage("system", "You are a product analyst.");
        ChatMessage userMessage = new ChatMessage("user", prompt);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatRequest chatRequest = new ChatRequest("gpt-4o", messages);

        chatGPTService.getChatResponse(chatRequest).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    String analysis = response.body().getChoices().get(0).getMessage().getContent();
                    callback.onResponse(analysis);
                    Log.d("Error",response.toString());
                } else {
                    callback.onError(new Exception("Error in API response"));
                    Log.d("Error",response.toString());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                callback.onError(t);
                Log.d("Error",t.toString());
            }
        });
    }


    private void closeAnalysis() {
        if (analysisView != null) {
            analysisView.setVisibility(View.GONE);
        }
    }
}
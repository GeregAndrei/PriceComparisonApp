package com.example.price_analysis_app.Account;

import com.example.price_analysis_app.Items.Item;
import com.google.firebase.firestore.DocumentReference;

public class TemporaryClass {
    private Item item;
    private DocumentReference docRef;
    public TemporaryClass(Item item,DocumentReference docRef) {
        this.item = item;
        this.docRef = docRef;

    }

    public DocumentReference getDocRef() {
        return docRef;
    }

    public void setDocRef(DocumentReference docRef) {
        this.docRef = docRef;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

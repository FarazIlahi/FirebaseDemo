package aydin.firebasedemo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SecondaryController {

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordtextField;


    @FXML
    private Button signinButton;

    @FXML
    private Button registerButton;

    @FXML
    void registerButtonClicked(ActionEvent event) {
        registerUser();
    }

    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(emailTextField.getText())
                .setEmailVerified(false)
                .setPassword(passwordtextField.getText())
                .setDisabled(false);
        UserRecord userRecord;
        try {
            userRecord = DemoApp.fauth.createUser(request);
            addPassword(userRecord);
            emailTextField.clear();
            passwordtextField.clear();
            System.out.println("Successfully created new user with Firebase Uid: " + userRecord.getUid()
                    + " check Firebase > Authentication > Users tab");
            return true;

        } catch (FirebaseAuthException ex) {
            // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error creating a new user in the firebase");
            return false;
        }


    }

    public void addPassword(UserRecord userRecord) {

        DocumentReference docRef = DemoApp.fstore.collection("Passwords").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Password", passwordtextField.getText());
        data.put("passwordID", userRecord.getUid());


        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public void signIn() throws FirebaseAuthException, IOException {
        String pID = DemoApp.fauth.getUserByEmail(emailTextField.getText()).getUid();
        boolean swicth = false;
        ApiFuture<QuerySnapshot> future =  DemoApp.fstore.collection("Passwords").get();

        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {

                for (QueryDocumentSnapshot document : documents)
                {
                    if(document.getData().get("passwordID").equals(pID)){
                        if(passwordtextField.getText().equals( document.getData().get("Password"))){
                            swicth = true;
                        }
                    }



                }
            }
            else
            {
                System.out.println("No data");
            }

        }
        catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace();
        }
        if(swicth){
            DemoApp.setRoot("primary");
        }
    }

}

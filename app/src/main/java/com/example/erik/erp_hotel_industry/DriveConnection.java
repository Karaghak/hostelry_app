package com.example.erik.erp_hotel_industry;

/**
 * Created by Erik on 27/01/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;




import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.events.DriveEventService;
import com.google.android.gms.drive.query.Filter;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.File;
import java.sql.SQLOutput;

public class DriveConnection extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Google Drive Activity";
    private static  String DATABASE_NAME = "";
    private final static String MAIN_FOLDER_NAME = "Hostelry App Backup";
    private final static String DATABASE_FOLDER_NAME = "Database";
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_RESOLUTION = 1;
    private static final  int REQUEST_CODE_OPENER = 2;
    private DriveId mFileId;
    private static DriveId main_folder_id;
    private static DriveId database_folder_id;
    public DriveFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driveconnection);

        // Load parameters
        Bundle b = getIntent().getExtras();
        if(b != null) DATABASE_NAME = b.getString("db_name");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {

            /**
             * Create the API client and bind it to an instance variable.
             * We use this instance as the callback for connection and connection failures.
             * Since no account name is passed, the user is prompted to choose.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {

            // disconnect Google Android Drive API connection.
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + connectionResult.toString());

        if (!connectionResult.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */

        try {

            connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

        } catch (IntentSender.SendIntentException e) {

            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }



    public void onClickCreateFile(View view){
        checkDriveFolders();
    }

    private void checkDriveFolders(){
        //// WARNING: USE ONLY ON DEBUG MODE
        //deleteAll();
        DriveFolder folder = Drive.DriveApi.getRootFolder(mGoogleApiClient);
        folder.listChildren(mGoogleApiClient).setResultCallback(childrenRetrievedCallback);
    }

    ResultCallback<DriveApi.MetadataBufferResult> childrenRetrievedCallback = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        //showMessage("Problem while retrieving files");
                        return;
                    }
                    MetadataBuffer metadataBuffer = result.getMetadataBuffer();
                    for(Metadata m : metadataBuffer){
                        System.out.println(m.getTitle());
                        if(m.getTitle().equals(MAIN_FOLDER_NAME))
                        {
                            System.out.println("***** MAIN FOLDER DETECTED *****");
                            System.out.println("***** SEARCHING THE DATABASE FOLDER *****");
                            main_folder_id = m.getDriveId();
                            DriveFolder folder = Drive.DriveApi.getFolder(mGoogleApiClient, m.getDriveId());
                            folder.listChildren(mGoogleApiClient).setResultCallback(mainFolderChildren);
                            return;
                        }
                    }
                    System.out.println("***** FOLDERS NOT DETECTED *****");
                    System.out.println("***** CREATING ALL ******");
                    createMainFolder();

                }
            };

    ResultCallback<DriveApi.MetadataBufferResult> mainFolderChildren = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        //showMessage("Problem while retrieving files");
                        return;
                    }
                    MetadataBuffer metadataBuffer = result.getMetadataBuffer();
                    for(Metadata m : metadataBuffer){
                        System.out.println(m.getTitle());
                        if(m.getTitle().equals(DATABASE_FOLDER_NAME))
                        {
                            System.out.println("***** DATABASE FOLDER DETECTED *****");
                            System.out.println("***** SEARCHING THE DATABASE FILE *****");
                            database_folder_id = m.getDriveId();
                            // If the database file exists it will be deleted for creating the new one.
                            // If not, it will be create.
                            DriveFolder db = Drive.DriveApi.getFolder(mGoogleApiClient, database_folder_id);
                            db.listChildren(mGoogleApiClient).setResultCallback(deleteDbFile);
                            Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(driveContentsCallback);
                            return;
                        }
                    }
                    System.out.println("***** DATABASE FOLDER NOT DETECTED *****");
                    System.out.println("***** CREATING ******");
                    createDatabaseFolder(main_folder_id);
                }
            };

    private void createMainFolder(){
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(MAIN_FOLDER_NAME).build();
        Drive.DriveApi.getRootFolder(mGoogleApiClient)
                .createFolder(mGoogleApiClient, changeSet)
                .setResultCallback(mainFolderCreatedCallback);
    }

    private void createDatabaseFolder(DriveId folderID){
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(DATABASE_FOLDER_NAME).build();
        Drive.DriveApi.getFolder(mGoogleApiClient, folderID)
                .createFolder(mGoogleApiClient, changeSet)
                .setResultCallback(databaseFolderCreatedCallback);
    }

    ResultCallback<DriveFolder.DriveFolderResult> mainFolderCreatedCallback = new
            ResultCallback<DriveFolder.DriveFolderResult>() {
                @Override
                public void onResult(DriveFolder.DriveFolderResult result) {
                    if (!result.getStatus().isSuccess()) {
                        //showMessage("Error while trying to create the folder");
                        return;
                    }
                    else{
                        createDatabaseFolder(result.getDriveFolder().getDriveId());
                    }
                }
            };

    ResultCallback<DriveFolder.DriveFolderResult> databaseFolderCreatedCallback = new
            ResultCallback<DriveFolder.DriveFolderResult>() {
                @Override
                public void onResult(DriveFolder.DriveFolderResult result) {
                    if (!result.getStatus().isSuccess()) {
                        //showMessage("Error while trying to create the folder");
                        return;
                    }
                    else{
                        database_folder_id = result.getDriveFolder().getDriveId();
                        DriveFolder db = Drive.DriveApi.getFolder(mGoogleApiClient, database_folder_id);
                        db.listChildren(mGoogleApiClient).setResultCallback(deleteDbFile);
                        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(driveContentsCallback);
                    }
                }
            };

    ResultCallback<DriveApi.MetadataBufferResult> deleteDbFile = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        //showMessage("Problem while retrieving files");
                        return;
                    }
                    MetadataBuffer metadataBuffer = result.getMetadataBuffer();
                    for(Metadata m : metadataBuffer){
                        System.out.println(m.getTitle());
                        if(m.getTitle().equals(DATABASE_NAME))
                        {
                            DriveResource dbFile = m.getDriveId().asDriveFile();
                            System.out.println("///// DATABASE FILE DELETED FOR UPDATE /////");
                            dbFile.delete(mGoogleApiClient);
                        }
                    }
                }
            };



    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {

                    if (result.getStatus().isSuccess()) {
                        String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
                        OutputStream outputStream = result.getDriveContents().getOutputStream();

                        try {
                            FileInputStream fileInputStream = new FileInputStream(getDatabaseFile(DATABASE_NAME));
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                            byte[] buffer = new byte[8 * 1024];
                            int n = 0;
                            while ( (n = bufferedInputStream.read(buffer)) > 0){
                                outputStream.write(buffer, 0, n);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                .setTitle(DATABASE_NAME)
                                .setMimeType(mimeType)
                                .setStarred(true).build();

                        Drive.DriveApi.getFolder(mGoogleApiClient, database_folder_id)
                                .createFile(mGoogleApiClient, metadataChangeSet, result.getDriveContents())
                                .setResultCallback(fileCallback);
                    }
                }
            };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (result.getStatus().isSuccess()) {

                        Toast.makeText(getApplicationContext(), "database file created", Toast.LENGTH_LONG).show();

                        //result.getDriveFile().getDriveId(), Toast.LENGTH_LONG).show();
                    }

                    return;

                }
            };

    /**
     *  Handle Response of selected file
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_OPENER:

                if (resultCode == RESULT_OK) {

                    mFileId = (DriveId) data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);

                    Log.e("file id", mFileId.getResourceId() + "&");

                    String url = "https://drive.google.com/open?id="+ mFileId.getResourceId();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private File getDatabaseFile(String databaseName){
        return getApplicationContext().getDatabasePath(databaseName);
    }


    private void deleteAll(){
        DriveFolder folder = Drive.DriveApi.getRootFolder(mGoogleApiClient);
        folder.listChildren(mGoogleApiClient).setResultCallback(deleteAllCallback);
    }

    ResultCallback<DriveApi.MetadataBufferResult> deleteAllCallback = new
            ResultCallback<DriveApi.MetadataBufferResult>() {
                @Override
                public void onResult(DriveApi.MetadataBufferResult result) {
                    if (!result.getStatus().isSuccess()) {
                        //showMessage("Problem while retrieving files");
                        return;
                    }
                    MetadataBuffer metadataBuffer = result.getMetadataBuffer();
                    for(Metadata m : metadataBuffer){
                        m.getTitle();
                        if(m.getTitle().equals(MAIN_FOLDER_NAME))
                        {
                            System.out.println("********DELETED********");
                            DriveResource folder = m.getDriveId().asDriveFolder();
                            folder.delete(mGoogleApiClient);
                        }
                        System.out.println("********NEXT ONE********");
                    }
                }
            };


}

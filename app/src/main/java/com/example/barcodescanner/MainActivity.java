package com.example.barcodescanner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    Button btn_scan;
    Button btn_manual;
    Button btn_tracks;
    Button btn_addorder;
    Button btn_addcust;
    RecyclerView recyclerViewOrders;
    List<PostModel> postModelList;
    List<PostModel> postModelTrackList;
    PostAdapter adapter;
    private String scannedCode;
    private String manualCode;
    private String inputType;
    public String clickType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v -> scanCode());

        btn_manual = findViewById(R.id.btn_manual);
        btn_manual.setOnClickListener(v -> manualTrackCode());

        btn_tracks = findViewById(R.id.btn_tracks);
        btn_tracks.setOnClickListener(v -> trackList());

        btn_addorder = findViewById(R.id.btn_addorder);
        btn_addorder.setOnClickListener(v -> showCustomDialog());

        btn_addcust = findViewById(R.id.btn_addcust);
        btn_addcust.setOnClickListener(v -> showCustomDialogCust());
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        final EditText name_surname_input = dialog.findViewById(R.id.name_input);
        final EditText phone_number_input = dialog.findViewById(R.id.phone_number_input);
        final EditText price_input = dialog.findViewById(R.id.payment_input);
        final Spinner spinner = dialog.findViewById(R.id.order_type_spinner);
        final CheckBox isWhatsApp_input = dialog.findViewById(R.id.whatsApp_input);
        Button submitButton = dialog.findViewById(R.id.submit_input);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_surname_input.getText().toString();
                String phone = phone_number_input.getText().toString();
                String price = price_input.getText().toString() + " TL";
                String letterName = spinner.getSelectedItem().toString();
                Boolean isWhatsApp = isWhatsApp_input.isChecked();
                Log.i("Name:", name);
                Log.i("Phone:", phone);
                Log.i("Price:", price);
                Log.i("Letter Name", letterName);
                Log.i("WhatsApp:", isWhatsApp.toString());
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("customerName", name);
                    jsonObject.put("letterName", letterName);
                    jsonObject.put("orderPrice", price);
                    jsonObject.put("phoneNumber", phone);
                    jsonObject.put("isWhatsApp", isWhatsApp);
                    String jsonString = jsonObject.toString();
                    new PostData("add_order").execute(jsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showCustomDialogCust() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_customer);

        final EditText cust_name_input = dialog.findViewById(R.id.cust_name_input);
        final EditText cust_email = dialog.findViewById(R.id.cust_email);
        final EditText cust_priv_input = dialog.findViewById(R.id.cust_priv_input);
        final CheckBox cust_active_input = dialog.findViewById(R.id.cust_active_input);
        Button submitButton = dialog.findViewById(R.id.submit_input);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = cust_name_input.getText().toString();
                String email = cust_email.getText().toString();
                String privilage = cust_priv_input.getText().toString();
                Boolean isActive = cust_active_input.isChecked();

                String isActive_str;

                if (isActive) {
                    isActive_str = "Aktif";
                } else {
                    isActive_str = "Pasif";
                }

                Log.i("Name:", name);
                Log.i("Email:", email);
                Log.i("Privilage:", privilage);
                Log.i("Active:", isActive_str);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("customerName", name);
                    jsonObject.put("email", email);
                    jsonObject.put("status", isActive_str);
                    jsonObject.put("privilage", privilage);
                    String jsonString = jsonObject.toString();
                    new PostData("add_customer").execute(jsonString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class PostData extends AsyncTask<String, Void, String> {
        private String postType;

        // Constructor to initialize postType
        public PostData(String postType) {
            this.postType = postType;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                // on below line creating a url to post the data.
                URL url = new URL("https://" + BuildConfig.IP1 + ":5000/" + postType);

                // on below line opening the connection.
                HttpURLConnection client = (HttpURLConnection) url.openConnection();

                // on below line setting method as post.
                client.setRequestMethod("POST");

                // on below line setting content type and accept type.
                client.setRequestProperty("Content-Type", "application/json");
                client.setRequestProperty("Accept", "application/json");

                // on below line setting client.
                client.setDoOutput(true);

                // on below line we are creating an output stream and posting the data.
                try (OutputStream os = client.getOutputStream()) {
                    byte[] input = strings[0].getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // on below line creating and initializing buffer reader.
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(client.getInputStream(), "utf-8"))) {

                    // on below line creating a string builder.
                    StringBuilder response = new StringBuilder();

                    // on below line creating a variable for response line.
                    String responseLine = null;

                    // on below line writing the response
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Data has been posted to the API.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Fail to post the data : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Flaş açmak için ses yükseltme tuşuna basınız.");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            scannedCode = result.getContents();
            inputType = "scan";
            showAlertDialog(scannedCode);
        }
    });

    private void showAlertDialog(String contents) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Takip Kodu:");
        builder.setMessage(contents);
        builder.setPositiveButton("Müşteri Seç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                orderList();
                dialogInterface.dismiss();
                setButtonPositions();
            }
        }).setNegativeButton("Yeniden Çek", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanCode(); // Re-scan when "Yeniden Çek" is clicked
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void orderList() {
        clearRecyclerView();
        CaptureAct captureAct = new CaptureAct();
        ApiService serviceGenerator = captureAct.buildService(ApiService.class);
        Call<List<PostModel>> call = serviceGenerator.getOrders();
        clickType = "notsent";

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                Log.i("Order List", "Bağlantı Kuruldu");
                if (response.isSuccessful() && response.body() != null) {
                    postModelList = response.body();

                    if (recyclerViewOrders != null) {
                        recyclerViewOrders.setAdapter(null);
                        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        adapter = new PostAdapter(postModelList, MainActivity.this);
                        recyclerViewOrders.setAdapter(adapter);
                    } else {
                        Log.e("RecyclerView", "Null object. Check if it's correctly referenced in the layout.");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                t.printStackTrace();
                Log.e("Order List", t.getMessage());
                orderList();
            }
        });
    }

    private void trackList() {
        clearRecyclerView();
        setButtonPositions();
        CaptureAct captureAct = new CaptureAct();
        ApiService serviceGenerator = captureAct.buildService(ApiService.class);
        Call<List<PostModel>> call = serviceGenerator.getOrders();
        clickType = "sent";

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                Log.i("Track List", "Bağlantı Kuruldu");

                if (response.isSuccessful() && response.body() != null) {
                    postModelList = response.body();
                    postModelTrackList = new ArrayList<>();

                    if (postModelList != null) {
                        for (PostModel postModel : postModelList) {
                            String trackId = postModel.getTrackId();
                            if (trackId != null && !trackId.isEmpty()) {
                                postModelTrackList.add(postModel);
                                Log.i("Track ID", postModel.getID());
                            }
                        }
                        if (recyclerViewOrders != null) {
                            recyclerViewOrders.setAdapter(null);
                            recyclerViewOrders.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            adapter = new PostAdapter(postModelTrackList, MainActivity.this);
                            recyclerViewOrders.setAdapter(adapter);
                        } else {
                            Log.e("RecyclerView", "Null object. Check if it's correctly referenced in the layout.");
                        }

                    } else {
                        Log.e("postModelList", "Null object. Check if it's correctly referenced in onResponse.");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                t.printStackTrace();
                Log.e("Track List", t.getMessage());
                trackList();
            }
        });
    }

    private void clearRecyclerView() {
        if (adapter != null) {
            adapter.clear(); // Önce adaptörü temizleyelim
        }
    }

    @Override
    public void onItemOnClick(int position) {
        if (postModelList != null && position >= 0 && position < postModelList.size()) {
            PostModel selectedPostModel;

            // trackList'teki pozisyona göre seçilen PostModel nesnesini al
            if (clickType.equals("notsent")) {
                selectedPostModel = postModelList.get(position);
            } else if (clickType.equals("sent")) {
                selectedPostModel = postModelTrackList.get(position);
            } else {
                return;
            }

            Log.i("Post Model Email: ", selectedPostModel.getEmail());
            if (clickType.equals("notsent")) {
                sendMessageAlertBox(selectedPostModel);
            } else if (clickType.equals("sent")) {
                pttPage(selectedPostModel);
            }
        }
    }


    public void pttPage(PostModel selectedPostModel) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", BuildConfig.TRACK_LINK + selectedPostModel.getTrackId());
        Log.i("Link: ", BuildConfig.TRACK_LINK + selectedPostModel.getTrackId());
        startActivity(intent);
    }

    public void sendMessageAlertBox(PostModel selectedPostModel) {
        CaptureAct captureAct = new CaptureAct();
        if (selectedPostModel != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Müşteri Bilgileri:");
            builder.setMessage("Takip Kodu: " + ((inputType.equals("scan")) ? scannedCode : manualCode) +
                    "\nMüşteri Adı: " + selectedPostModel.getCustomerName() +
                    "\nMüşteri Telefon Numarası: " + selectedPostModel.getPhoneNumber());
            builder.setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ArrayList<String> selectedItems = new ArrayList<>();
                    String[] choices = {"SMS", "Email"};
                    boolean[] checkedItems = {true, true}; // İlk iki seçeneği varsayılan olarak işaretle
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("İletim Türü:")
                            .setMultiChoiceItems(choices, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if (isChecked) {
                                        selectedItems.add(choices[which]); // Add selected item to the list
                                    } else {
                                        selectedItems.remove(choices[which]); // Remove if unselected
                                    }
                                }
                            })
                            .setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // İşaretlenmiş öğeleri kontrol etmek için bir döngü
                                    for (int j = 0; j < choices.length; j++) {
                                        if (checkedItems[j]) {
                                            // İşaretlenen öğeyi alın
                                            String selectedItem = choices[j];

                                            // İşaretlenen öğenin hangi eylemi gerçekleştireceğini kontrol et
                                            if (selectedItem.equals("SMS")) {
                                                sendSMS(((inputType.equals("scan")) ? scannedCode : manualCode), selectedPostModel.getPhoneNumber());
                                            } else if (selectedItem.equals("Email")) {
                                                try {
                                                    String toEmail = selectedPostModel.getEmail();
                                                    if (toEmail.equals("Bu Bir WhatsApp Siparişidir.")) {
                                                        throw new Exception("Email Adresi Bulunamamıştır.");
                                                    }
                                                    String emailSubject = "Siparişiniz Kargoya Verildi!";
                                                    String track_code = (inputType.equals("scan")) ? scannedCode : manualCode;
                                                    String track_link = "https://gonderitakip.ptt.gov.tr/Track/Verify?q=" + track_code;
                                                    String emailMessage = "Değerli müşterimiz, Mektup Evi üzerinden vermiş olduğunuz siparişinizin teslimat durumunu " + track_code +  " takip kodu ile PTT'nin internet sitesi üzerinden veya verilen link üzerinden takip edebilirsiniz. " + track_link;;

                                                    SendMailTask sendMailTask = new SendMailTask(toEmail, emailSubject, emailMessage);
                                                    sendMailTask.execute();
                                                    Toast.makeText(getApplicationContext(), "Email Gönderimi Başarılı Oldu!", Toast.LENGTH_LONG).show();
                                                    Log.i("Send Email","Email Gönderimi Başarılı Oldu.");
                                                }
                                                catch (Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Email Gönderimi Başarısız Oldu! Hata: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    Log.e("Send Email", "Email Gönderimi Başarısız Oldu. Hata: " + e.getMessage());
                                                }
                                            }
                                        }
                                    }

                                    // PostModel'den gerekli verileri alarak post işlemini gerçekleştir
                                    String orderId = selectedPostModel.getID();
                                    String trackId = ((inputType.equals("scan")) ? scannedCode : manualCode);

                                    ApiService serviceGenerator = captureAct.buildService(ApiService.class);
                                    Call<PostModel> call = serviceGenerator.importTrack(orderId, trackId, selectedPostModel);

                                    call.enqueue(new Callback<PostModel>() {
                                        @Override
                                        public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                                            // Post işlemi başarılı olduysa SMS gönder
                                            if (response.isSuccessful()) {
                                                Log.d("POST", "POST işlemi başarılı oldu.");
                                            } else {
                                                Log.e("POST", "Post işlemi başarısız oldu");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<PostModel> call, Throwable t) {
                                            t.printStackTrace();
                                            Log.e("error", t.getMessage());
                                            // Hata durumları veya geri dönüşe göre işlemler
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("İptal Et", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            builder.setNegativeButton("Yeniden Dene", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void sendSMS(String track_code, String phone_number) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                try {
                    // SMS gönderimi için gerekli değişken tanımları
                    // kullanıcı adı
                    String username = BuildConfig.NETGSM_USERNAME;
                    // kullanıcı şifresi
                    String password = BuildConfig.NETGSM_PASSWORD;
                    // mesaj başlığı
                    String header = BuildConfig.NETGSM_HEADER;
                    // gönderilecek mesaj
                    String track_link = BuildConfig.TRACK_LINK + track_code;

                    String msg = "Değerli müşterimiz, Mektup Evi üzerinden vermiş olduğunuz siparişinizin teslimat durumunu " + track_code +  " takip kodu ile PTT'nin internet sitesi üzerinden veya verilen link üzerinden takip edebilirsiniz. " + track_link;

                    byte[] utf8Bytes = msg.getBytes(StandardCharsets.UTF_8);

                    // UTF-8 olarak encode edilmiş metni almak için
                    String encodedMsg = new String(utf8Bytes, StandardCharsets.UTF_8);

                    // Post gönderilecek Netgsm servisi
                    URL u = new URL("https://api.netgsm.com.tr/sms/send/xml");

                    String body = "<?xml version='1.0' encoding='UTF-8'?> "+
                            "<mainbody> "+
                            "<header>  "+
                            "<company dil='TR'>Netgsm</company>   "+
                            "<usercode>" + username + "</usercode> "+
                            "<password>" + password + "</password>  "+
                            "<type>1:n</type>  "+
                            "<msgheader>" + header + "</msgheader>  "+
                            "</header>  "+
                            "<body>  "+
                            "<msg><![CDATA[" + msg + "]]></msg>  "+
                            "<no>" + phone_number + "</no>    "+
                            "</body>  "+
                            "</mainbody>";

                    byte[] utf8Body = body.getBytes(StandardCharsets.UTF_8);

                    String decodedBody = new String(utf8Body, StandardCharsets.UTF_8);

                    URLConnection uc = u.openConnection();
                    HttpURLConnection connection = (HttpURLConnection) uc;
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    OutputStream out = connection.getOutputStream();
                    OutputStreamWriter wout = new OutputStreamWriter(out, "UTF-8");
                    wout.write(body);
                    wout.flush();
                    out.close();
                    InputStream in = connection.getInputStream();
                    int c;
                    final StringBuilder sonucWrapper = new StringBuilder();
                    while ((c = in.read()) != -1){
                        sonucWrapper.append((char)c); // Append characters to the wrapper object
                    }
                    in.close();
                    out.close();
                    connection.disconnect();

                    final String sonuc = sonucWrapper.toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (sonuc.startsWith("00")) {
                                Toast.makeText(getApplicationContext(), "SMS Gönderimi Başarılı Oldu!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "SMS Gönderimi Başarısız Oldu, Durum Kodu: " + sonuc, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    Log.d("Send SMS", sonuc);

                    // alınan örnek cevap
                    // 00 361126686

                }
                catch (IOException e) {
                    System.err.println(e);
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(track_code, phone_number);
    }

    public void manualTrackCode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Takip Kodunu Giriniz:");

        // EditText oluşturarak kullanıcıdan giriş yapmasını isteyin
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Pozitif buton ekleyerek kullanıcının girişi onaylamasını sağlayın
        builder.setPositiveButton("Onayla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manualCode = input.getText().toString();
                inputType = "manual";
                orderList();
                setButtonPositions();

                // Kullanıcının girdisini kullanabilirsiniz (örneğin, userInput değerini kullanın)
                // Burada yapılacak işlemleri gerçekleştirin
            }
        });

        // Dialog'ı iptal etmesi için negatif buton ekleyin
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // AlertDialog'u gösterin
        builder.show();
    }

    public void setButtonPositions() {
        RelativeLayout.LayoutParams paramsTracks = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsTracks.addRule(RelativeLayout.ALIGN_PARENT_END);
        paramsTracks.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        paramsTracks.setMargins(0, 0, 16, 16);
        btn_tracks.setLayoutParams(paramsTracks);

        RelativeLayout.LayoutParams paramsAddOrder = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsAddOrder.addRule(RelativeLayout.ALIGN_PARENT_END);
        paramsAddOrder.addRule(RelativeLayout.ABOVE, R.id.btn_tracks);
        paramsAddOrder.setMargins(0, 0, 16, 16);
        btn_addorder.setLayoutParams(paramsAddOrder);

        RelativeLayout.LayoutParams paramsManual = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsManual.addRule(RelativeLayout.ALIGN_PARENT_END);
        paramsManual.addRule(RelativeLayout.ABOVE, R.id.btn_addorder);
        paramsManual.setMargins(0, 0, 16, 16);
        btn_manual.setLayoutParams(paramsManual);

        RelativeLayout.LayoutParams paramsScan = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        paramsScan.addRule(RelativeLayout.ALIGN_PARENT_END);
        paramsScan.addRule(RelativeLayout.ABOVE, R.id.btn_manual);
        paramsScan.setMargins(0, 0, 16, 16);
        btn_scan.setLayoutParams(paramsScan);

        btn_addcust.setVisibility(View.GONE); // ya da View.INVISIBLE
    }
}


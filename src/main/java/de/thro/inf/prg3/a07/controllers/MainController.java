package de.thro.inf.prg3.a07.controllers;

import de.thro.inf.prg3.a07.api.OpenMensaAPI;
import de.thro.inf.prg3.a07.model.Meal;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {

	private ObservableList observableList = FXCollections.observableArrayList();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
	static String today = sdf.format(new Date());
	private static OpenMensaAPI openMensaAPI;
	private static final Logger logger = LogManager.getLogger(MainController.class);
	HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		//loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


	private OkHttpClient client= new OkHttpClient.Builder()
		.addInterceptor(loggingInterceptor)
		.build();;

	private Retrofit retrofit;





	public MainController(){
		//HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		retrofit = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl("https://openmensa.org/api/v2/")
			.client(client)
			.build();
		openMensaAPI = retrofit.create(OpenMensaAPI.class);

	}

	// use annotation to tie to component in XML
	@FXML
	private Button btnRefresh;

	@FXML
	private CheckBox chkVegetarian;

	@FXML
	private Button btnClose;

	@FXML
	private ListView<String> mealsList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		observableList.addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(Change<? extends String> c) {
				mealsList.setItems(observableList);
			}
		});
		// set the event handler (callback)
		btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// create a new (observable) list and tie it to the view
				//observableList.add("Hans");
				//mealsList.setItems(observableList);

				observableList.removeAll();

				Call<List<Meal>> getMeals = openMensaAPI.getMeals("229","2019-11-13");
				getMeals.enqueue(new Callback<List<Meal>>() {
					@Override
					public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
						List<Meal> meals = response.body();
						boolean boolVegetarian = chkVegetarian.isSelected();

						assert meals != null;
						for(Meal m : meals){
							if(boolVegetarian == true && m.isVegetarian()==true)
							observableList.add(m.toString());
							if(boolVegetarian == false){
								observableList.add(m.toString());
							}
						}
					}

					@Override
					public void onFailure(Call<List<Meal>> call, Throwable t) {
						try {
							throw t;
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}

					}
				});

			}
		});

		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});


	}
}



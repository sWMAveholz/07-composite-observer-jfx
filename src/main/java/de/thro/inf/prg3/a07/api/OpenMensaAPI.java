package de.thro.inf.prg3.a07.api;

import de.thro.inf.prg3.a07.model.Meal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

/**
 * Created by Peter Kurfer on 11/19/17.
 */

public interface OpenMensaAPI {
	// TODO add method to get meals of a day
	// example request: GET /canteens/229/days/2017-11-22/meals

	@GET("canteens/{canteenid}/days/{timeOfDate}/meals")
	Call<List<Meal>> getMeals(@Path("canteenid") String canteenid, @Path("timeOfDate") String timeOfDate);
}

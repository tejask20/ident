import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Company;

import models.Ident;
import org.hamcrest.CoreMatchers;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WS;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class RestControllerTest {

	private static final String BASE_URL = "http://localhost:3333";
	private static final String ADD_COMP_URL = "/api/v1/addCompany";
	private static final String ADD_IDENT_URL = "/api/v1/startIdentification";
	private static final String GET_IDENT_URL = "/api/v1/identifications";

	@Test
	public void postIdentification() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				JsonNode company = Json.parse("{\"id\": 1, \"name\": \"Test Bank\", \"slaTime\": 60, \"slaPercentage\": 0.9, \"currentSlaPercentage\": 0.95}");
				assertEquals(WS.url("http://localhost:3333/api/v1/addCompany").post(company).get(10000).getStatus(), OK);
				
				JsonNode identification = Json.parse("{\"id\": 1, \"name\": \"Peter Huber\", \"startTime\": 1435667215, \"waitTime\": 45, \"companyId\": 1}");
				assertEquals(WS.url("http://localhost:3333/api/v1/startIdentification").post(identification).get(10000).getStatus(), OK);
			}
		});

	}

	@Test
	public void getIdentifications()
	{
		running(testServer(3333, fakeApplication(inMemoryDatabase())), new Runnable() {
			@Override
			public void run() {
				Company company1 = new Company(1, "Company1", 60, new BigDecimal("0.9"), new BigDecimal("0.95"));
				Company company2 = new Company(2, "Company2", 120, new BigDecimal("0.8"), new BigDecimal("0.80"));

				assertEquals(WS.url(BASE_URL + ADD_COMP_URL).post(Json.toJson(company1)).get(10000).getStatus(), OK);
				assertEquals(WS.url(BASE_URL + ADD_COMP_URL).post(Json.toJson(company2)).get(10000).getStatus(), OK);

				String ident1Json = "{\"id\": 1, \"name\": \"Peter Huber\", \"startTime\": 1435667215, \"waitTime\": 45, \"companyId\": 1}";
				String ident2Json = "{\"id\": 2, \"name\": \"Gary Kirsten\", \"startTime\": 1435661267, \"waitTime\": 30, \"companyId\": 2}";

				assertEquals(WS.url(BASE_URL + ADD_IDENT_URL).post(Json.parse(ident1Json)).get(10000).getStatus(), OK);
				assertEquals(WS.url(BASE_URL + ADD_IDENT_URL).post(Json.parse(ident2Json)).get(10000).getStatus(), OK);

				String allIdentsJson = WS.url(BASE_URL + GET_IDENT_URL).get().get(10000).getBody();

				ObjectMapper mapper = new ObjectMapper();
				List<Ident> identList = null;
				try {
					identList = mapper.readValue(allIdentsJson, new TypeReference<List<Ident>>(){});
				} catch (IOException e) {
					e.printStackTrace();
				}

				Ident ident1 = Json.fromJson(Json.parse(ident1Json), Ident.class);
				ident1.setCompany(company1);
				Ident ident2 = Json.fromJson(Json.parse(ident2Json), Ident.class);
				ident2.setCompany(company1);

				List<Ident> sortedIdents = Arrays.asList(ident1, ident2);

				Assert.assertEquals(identList, sortedIdents);
			}
		});
	}

}

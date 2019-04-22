package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Company;
import models.CompanyRepository;
import models.Ident;
import models.IdentRepository;
import play.libs.Json;
import play.mvc.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RestController extends Controller {

    public Result startIdentification() {
        //Get the parsed JSON data
        JsonNode json = request().body().asJson();

        if (json == null) {
            return badRequest("Expecting Json data ");
        }
        int companyId = json.findPath("companyId").asInt();
        Company company = CompanyRepository.getCompanyRepository().getCompany(companyId);

        if (company == null) {
            return badRequest("Company not found for company id = " + companyId);
        }

        Ident ident = Json.fromJson(json, Ident.class);
        ident.setCompany(company);

        IdentRepository.getIdentRepository().addIdent(ident);

        List<Ident> result = IdentRepository.getIdentRepository().getAllIdents();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        return ok(jsonData);
    }

    public Result addCompany() {
        //Get the parsed JSON data
        JsonNode json = request().body().asJson();

        if (json == null) {
            return badRequest("Expecting Json data ");
        }
        CompanyRepository.getCompanyRepository().addCompany(Json.fromJson(json, Company.class));

        List<Company> result = CompanyRepository.getCompanyRepository().getAllCompanies();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
        return ok(jsonData);
    }

    public Result identifications() {
        JsonNode identifications = Json.newArray();

        List<Ident> allIdents = IdentRepository.getIdentRepository().getAllIdents();

        //Comparator<Ident> identComparator = (i1, i2) -> i1.getCompany().getSlaTime().compareTo(i2.getCompany().getSlaTime());

        List<Ident> sortedIdents = allIdents.stream().sorted(
                                        Comparator.comparingInt((Ident i) -> i.getCompany().getSlaTime())
                                                    .thenComparing((Ident i) -> i.getWaitTime())
                                                    .reversed()
                                                    .thenComparing((Ident i) -> i.getCompany().getCurrentSlaPercentage())
                                                    )
                                    .collect(Collectors.toList());



        return ok(Json.toJson(sortedIdents));
    }

}

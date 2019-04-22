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

        return ok();
    }

    public Result addCompany() {
        //Get the parsed JSON data
        JsonNode json = request().body().asJson();

        if (json == null) {
            return badRequest("Expecting Json data ");
        }

        //Add the company to repository
        CompanyRepository.getCompanyRepository().addCompany(Json.fromJson(json, Company.class));

        return ok();
    }

    public Result identifications() {
        JsonNode identifications = Json.newArray();

        List<Ident> allIdents = IdentRepository.getIdentRepository().getAllIdents();

        //First sort the idents based on the Company's SLA (ascending)
        //Then sort the idents based on the corresponding waiting time (descending)
        //Lastly sort the idents based on current SLA percentage (ascending)

        Comparator<Ident> slaComparator = Comparator.comparingInt((Ident i) -> i.getCompany().getSlaTime());
        Comparator<Ident> waitTimeComparator = Comparator.comparing(Ident::getWaitTime, (time1, time2) -> {
                                                                                    if(time1 == time2)  return 0;
                                                                                    return time1 < time2 ? 1 : -1;
                                                                                });
        Comparator<Ident> currSlaComparator = Comparator.comparing((Ident i) -> i.getCompany().getCurrentSlaPercentage());

        List<Ident> sortedIdents = allIdents.stream().sorted(
                                                        slaComparator
                                                                .thenComparing(waitTimeComparator)
                                                                .thenComparing(currSlaComparator)
                                                    )
                                    .collect(Collectors.toList());

        return ok(Json.toJson(sortedIdents));
    }

}

package db6.controller;

import db6.domain.Parents;
import db6.domain.Person;
import db6.domain.Person.Gender;
import db6.service.FamilyTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class FamilyTreeController {

    @Autowired
    private FamilyTreeService familyTreeService;

    @GetMapping("/")
    public String showFamilyTree(Model model) {
        // Fetch the family tree starting from the root person (e.g., the logged-in user)
        Person rootPerson = familyTreeService.getRootPerson();
        String familyTreeHtml = generateTreeHtml(rootPerson);

        // Pass the generated HTML to the template
        model.addAttribute("familyTreeHtml", familyTreeHtml);
        model.addAttribute("persons", familyTreeService.getAllPersons());
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model, Principal principal) {
        return showFamilyTree(model);
    }

    private static String getPortraitUrl(Person person) {
        return person.getPortraitUrl() != null && !person.getPortraitUrl().isEmpty()
            ? person.getPortraitUrl()
            : person.getGender() == Gender.MALE
                ?  "/images/portrait-male.webp"
                : "/images/portrait-female.jpg";
    }

    // Recursively generate HTML for whole family tree
    private String generateTreeHtml(Person person) {
        if (person == null) {
            return "";
        }

        StringBuilder html = new StringBuilder();

        // Generate HTML for the person's ancestors
        Parents parents = familyTreeService.getParents(person.getId());
        if (parents != null && parents.hasAny()) {
            html.append("<div class='parents'>");
            html.append(generateTreeHtml(parents.getFather().orElse(null)));
            html.append(generateTreeHtml(parents.getMother().orElse(null)));
            html.append("</div>");
        }

        // Generate the HTML for the current person
        html.append("<div class='person' onclick='showPersonDetails(")
            .append(person.getId())
            .append(")'>")
            .append("<img src='")
            .append(getPortraitUrl(person))
            .append("' alt='Portrait' class='portrait'>")
            .append("<p>")
            .append(person.getFirstName())
            .append(" ")
            .append(person.getLastName())
            .append("</p>")
            .append("</div>");

        return html.toString();
    }
}
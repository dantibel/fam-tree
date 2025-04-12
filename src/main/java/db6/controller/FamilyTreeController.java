package db6.controller;

import db6.domain.Parents;
import db6.domain.Person;
import db6.domain.Person.Gender;
import db6.service.AppUserService;
import db6.service.FamilyTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

// Main controller responsible for displaying the family tree
@Controller
public class FamilyTreeController {

    @Autowired
    private FamilyTreeService familyTreeService;

    @Autowired
    private AppUserService appUserService;

    private Long rootPersonId = null;
    private Long loggedUserId;

    @GetMapping("/fam-tree")
    public String showFamilyTree(Model model, Principal principal) {

        // Fetch the logged-in user's root person ID
        if (principal != null) {
            String username = principal.getName();
            loggedUserId = appUserService.getUserPersonId(username);
        }

        if (rootPersonId == null) {
            rootPersonId = loggedUserId;
        }

        // Fetch the family tree with the logged-in user as a root
        Person rootPerson = familyTreeService.getPerson(rootPersonId).orElse(null);
        String familyTreeHtml = generateTreeHtml(rootPerson);

        // Pass the generated HTML to the template
        model.addAttribute("familyTreeHtml", familyTreeHtml);
        model.addAttribute("rootPerson", rootPerson);
        model.addAttribute("loggedUserId", loggedUserId);
        model.addAttribute("persons", familyTreeService.getAllPersons());
        return "index";
    }

    @GetMapping("/set-root-person")
    public String setRootPerson(Model model, Principal principal, @RequestParam Long id) {
        rootPersonId = id;
        return "redirect:/";
    }

    private static String getPortraitUrl(Person person) {
        return person.getPortraitUrl() != null && !person.getPortraitUrl().isEmpty()
            ? person.getPortraitUrl()
            : person.getGender() == Gender.MALE
                ?  "/images/portrait-male.webp"
                : "/images/portrait-female.jpg";
    }

    // Recursively generate HTML for whole family tree (root person is at the bottom)
    private String generateTreeHtml(Person person) {
        if (person == null) {
            return "";
        }

        StringBuilder html = new StringBuilder();

        // Generate HTML for the person's ancestors
        Parents parents = familyTreeService.getParents(person.getId());
        html.append("<div class='family-tree-node'>");
        if (parents != null && parents.hasAny()) {
            html.append("<div class='parents'>");
            html.append(generateTreeHtml(parents.getFather().orElse(null)));
            html.append(generateTreeHtml(parents.getMother().orElse(null)));
            html.append("</div>");
            html.append("<div class='children'>");
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

        if (parents != null && parents.hasAny()) {
            html.append("</div>"); // Close childen div
        }
        html.append("</div>"); // Close family-tree-node div

        return html.toString();
    }
}
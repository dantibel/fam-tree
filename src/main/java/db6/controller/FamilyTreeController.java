package db6.controller;

import db6.domain.Parents;
import db6.domain.Person;
import db6.domain.Person.Gender;
import db6.service.AppUserService;
import db6.service.FamilyTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

            model.addAttribute("loggedUserId", loggedUserId);
            model.addAttribute("loggedUsername", username);
        }

        // Root person is the logged user's person by default
        if (rootPersonId == null || familyTreeService.getPerson(rootPersonId).isEmpty()) {
            rootPersonId = loggedUserId;
        }
        Person rootPerson = familyTreeService.getPerson(rootPersonId).orElse(null);

        model.addAttribute("rootPerson", rootPerson);
        model.addAttribute("persons", familyTreeService.getAllPersons());
        model.addAttribute("familyTreeHtml", generateTreeHtml(rootPerson));

        return "index";
    }

    @GetMapping("/set-root-person")
    public String setRootPerson(Model model, Principal principal, @RequestParam Long id) {
        rootPersonId = id;
        return "redirect:/";
    }

    @GetMapping("/add-person")
    public String addPerson(Model model, Principal principal) {
        model.addAttribute("loggedUserId", loggedUserId);
        model.addAttribute("loggedUsername", principal.getName());
        model.addAttribute("persons", familyTreeService.getAllPersons());
        return "add-person";
    }

    @GetMapping("/view-person")
    public String showPersonDetails(@RequestParam Long id, Model model, Principal principal) {
        model.addAttribute("loggedUsername", principal.getName());
        model.addAttribute("person", familyTreeService.getPerson(id).orElse(null));
        model.addAttribute("spouse", familyTreeService.getSpouse(id).orElse(null));
        model.addAttribute("parents", familyTreeService.getParents(id));
        model.addAttribute("siblings", familyTreeService.getSiblings(id));
        model.addAttribute("children", familyTreeService.getChildren(id));
        return "view-person";
    }

    @GetMapping("/edit-person")
    public String editPersonDetails(@RequestParam Long id, Model model, Principal principal) {
        model.addAttribute("loggedUsername", principal.getName());
        model.addAttribute("person", familyTreeService.getPerson(id).orElse(null));
        return "edit-person";
    }

    @PostMapping("/upload-portrait/{personId}")
    public ResponseEntity<String> uploadPortrait(@PathVariable Long personId, @RequestParam("file") MultipartFile file) {
        try {
            //familyTreeService.savePortrait(personId, file);
            return ResponseEntity.ok("Portrait uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload portrait: " + e.getMessage());
        }
    }

    private static String getPortraitUrl(Person person) {
        return //person.getPortrait() != null && !person.getPortrait().get
            //? person.getPortraitUrl()
            person.getGender() == Gender.MALE
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
        html.append("<div class='family-tree-node' style='background-color: #fdf5e6;'>");
        if (parents != null && parents.hasAny()) {
            html.append("<div class='parents'>");
            html.append(generateTreeHtml(parents.getFather().orElse(null)));
            html.append(generateTreeHtml(parents.getMother().orElse(null)));
            html.append("</div>");
            html.append("<div class='children'>");
        }

        // Generate the HTML for the current person with context menu
        html.append("<div class='person' style='background-color: #fdf5e6;' oncontextmenu='showContextMenu(event, " + person.getId() + ")'>")
            .append("<a href=\"/view-person?id=" + person.getId() + "\">")
            .append("<img src='" + getPortraitUrl(person) + "' alt='Portrait' class='portrait'>")
            .append("<p style='color: #5a3e36;'>" + person.getFirstName() + " " + (person.getMiddleName() != null ? (person.getMiddleName() + " ") : "") + person.getLastName())
            .append("</p>")
            .append("</a>")
            .append("</div>");

        if (parents != null && parents.hasAny()) {
            html.append("</div>"); // Close children div
        }
        html.append("</div>"); // Close family-tree-node div

        // Add the context menu HTML
        html.append("<div id='person-action-menu' class='context-menu'>")
            .append("<button onclick='viewPersonWithMenu()'>View Details</button><br>")
            .append("<button onclick='editPersonWithMenu()'>Edit</button><br>")
            .append("<button onclick='deletePersonWithMenu()'>Delete</button><br>")
            .append("</div>");

        return html.toString();
    }
}
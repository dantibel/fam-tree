package db6.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import db6.domain.Person;
import db6.service.FamilyTreeService;

@RestController
@RequestMapping("api")
public class RestApiController {

    @Autowired
    private FamilyTreeService familyTreeService;

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id, Model model) {
        if (familyTreeService.deletePerson(id))
            return ResponseEntity.ok("Person with ID " + id + " was successfully deleted.");

        return ResponseEntity.badRequest().body("Person with ID " + id + " could not be found.");
    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @RequestBody Person updatedPerson) {
        if (familyTreeService.updatePerson(id, updatedPerson)) {
            return ResponseEntity.ok("Person with ID " + id + " was successfully updated.");
        }
        return ResponseEntity.badRequest().body("Person with ID " + id + " could not be found.");
    }
}

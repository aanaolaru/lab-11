package com.example.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/people")
public class SocialNetworkController {

    private final List<Person> people = new ArrayList<>();

    public SocialNetworkController() {
        Person person1 = new Person(1, "irina", "irina");
        Person person2 = new Person(2, "ana-maria", "ana-maria");
        Person person3 = new Person(3, "elena", "elena");

        person3.addFriend(person2);
        person1.addFriend(person2);    // relationship
        person3.addFriend(person1);

        people.add(person1);
        people.add(person2);    // add persons in list
        people.add(person3);
    }

    @GetMapping
    public List<Person> getPeople() {
        return people;
    }    // get the list of the persons (GET request)

    @PostMapping
    public int createPerson(@RequestParam String name) {  // add new person in the list (POST request)
        int id = 1 + people.size();
        people.add(new Person(id, name, "12345"));
        return id;
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id) {    // get a person by id
        return people.stream()
                .filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @PutMapping("/{id}")                        // update the name of a person (PUT request)
    public ResponseEntity<String> updatePerson(
            @PathVariable int id, @RequestParam String name, @RequestParam String password) {
        Person person = findById(id);

        if (person == null) {
            return new ResponseEntity<>(
                    "Person not found", HttpStatus.NOT_FOUND); //or GONE
        }
        person.setName(name);
        person.setPassword(password);

        return new ResponseEntity<>(
                "Person updated successsfully", HttpStatus.OK);
    }

    private Person findById(int id) {
        for (Person person : people) {
            if (person.getId() == id)
                return person;
        }

        return null;
    }

    @DeleteMapping(value = "/{id}")     // deleting a person (DELETE request)
    public ResponseEntity<String> deletePerson(@PathVariable int id) {
        Person person = findById(id);

        if (person == null) {
            return new ResponseEntity<>(
                    "Person not found", HttpStatus.GONE);
        }
        people.remove(person);

        return new ResponseEntity<>(
                "Person removed", HttpStatus.OK);
    }

    @GetMapping("/popular/{k}")
    public List<Person> getPopularPeople(@PathVariable int k) {
        Collections.sort(people); // compara dupa nr de prieteni pe care ii are

        List<Person> popularPeople = new ArrayList<>();

        int length = people.size() - 1;
        for (int i = 0; i < k; i++)
            popularPeople.add(people.get(length - i));

        return popularPeople;
    }

    @GetMapping("/friends/{id}")
    public Set<Person> getFriends(@PathVariable int id) {
        Person person = findById(id);

        if (person == null) {
            return null;
        } else
            return person.getFriends();
    }

    @PostMapping("/{id}/addfriend")
    public ResponseEntity<String> getFriends(@PathVariable int id, @RequestParam int friendId) {
        Person person = findById(id);
        Person friend = findById(friendId);

        if (person == null || friend == null) {
            return new ResponseEntity<>(
                    "Person not found", HttpStatus.GONE);
        } else {
            person.addFriend(friend);
            friend.addFriend(person);

            return new ResponseEntity<>(
                    "Friend added succesfully", HttpStatus.OK);
        }
    }


}

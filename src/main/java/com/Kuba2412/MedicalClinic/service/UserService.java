package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.model.User;
import com.Kuba2412.MedicalClinic.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Test case 1: Dodawanie nowego użytkownika

     * - Opis: Sprawdza, czy metoda addUser dodaje nowego użytkownika do bazy danych.
     * - Kroki:
     *   1. Utwórz obiekt użytkownika z unikalnymi danymi (np. username, password).
     *   2. Wywołaj metodę addUser z utworzonym użytkownikiem.
     *   3. Sprawdź, czy użytkownik został dodany poprawnie do bazy danych.
     * - Wynik: Użytkownik jest dodany do bazy danych bez błędów.
     */

    public User addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    /**
     * Test case 1: Pobieranie użytkownika po identyfikatorze

     * - Opis: Sprawdza, czy metoda getUserId zwraca poprawnego użytkownika na podstawie jego identyfikatora.
     * - Kroki:
     *   1. Utwórz nowego użytkownika i dodaj go do bazy danych.
     *   2. Pobierz identyfikator utworzonego użytkownika.
     *   3. Wywołaj metodę getUserId z tym identyfikatorem.
     *   4. Porównaj zwróconego użytkownika z oczekiwanym użytkownikiem.
     * - Wynik: Metoda zwraca poprawnego użytkownika na podstawie identyfikatora.
     */

    public User getUserId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * Test case 1: Pobieranie wszystkich użytkowników z paginacją

     * - Opis: Sprawdza, czy metoda getAllUsers zwraca listę wszystkich użytkowników z użyciem paginacji.
     * - Kroki:
     *   1. Utwórz kilku użytkowników i dodaj ich do bazy danych.
     *   2. Utwórz obiekt Pageable z ustawioną paginacją (np. PageRequest.of(0, 10)).
     *   3. Wywołaj metodę getAllUsers z tym obiektem Pageable.
     *   4. Sprawdź czy lista instytucji zawiera oczekiwaną liczbę elementów (np. 10).
     * - Wynik: Metoda zwraca poprawnie paginowaną listę wszystkich użytkowników.
     */

    public List<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }

    /**
     * Test case 1: Aktualizacja hasła użytkownika

     * - Opis: Sprawdza, czy metoda updatePassword aktualizuje hasło użytkownika w bazie danych.
     * - Kroki:
     *   1. Utwórz nowego użytkownika i dodaj go do bazy danych.
     *   2. Utwórz nowe hasło dla tego użytkownika.
     *   3. Wywołaj metodę updatePassword z nazwą użytkownika i nowym hasłem.
     *   4. Pobierz użytkownika z bazdy danych i sprawdź, czy hasło zostało zaktualizowane.
     * - Wynik: Hasło użytkownika jest zaktualizowane zgodnie z oczekiwaniami.
     */

    public User updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
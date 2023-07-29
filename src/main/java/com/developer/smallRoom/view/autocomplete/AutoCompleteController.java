package com.developer.smallRoom.view.autocomplete;

import com.developer.smallRoom.application.autocomplete.AutoCompleteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/autocomplete")
public class AutoCompleteController {

    private final AutoCompleteService autoCompleteService;

    @GetMapping
    public ResponseEntity<List<String>> getAutoCompleteWords(@RequestParam(name = "query") String query) {
        List<String> autoCompleteWords = autoCompleteService.findAutoCompleteWords(query);
        return ResponseEntity.ok(autoCompleteWords);
    }
}

package com.example.sequence.generator.controller;

import com.example.sequence.generator.service.SequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.sequence.generator.model.entity.Sequence;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sequence")
public class SequenceController {

    private final SequenceService sequenceService;

    /**
     * Generates an account number.
     *
     * @return The generated account number.
     */
    @PostMapping
    public Sequence generateAccountNumber() {
        return sequenceService.create();
    }
}

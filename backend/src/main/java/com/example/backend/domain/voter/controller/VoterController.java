package com.example.backend.domain.voter.controller;

import com.example.backend.domain.voter.dto.VoterDTO;
import com.example.backend.domain.voter.entity.Voter;
import com.example.backend.domain.voter.service.VoterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voters")
public class VoterController {

}

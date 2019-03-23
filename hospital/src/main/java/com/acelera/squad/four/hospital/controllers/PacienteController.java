package com.acelera.squad.four.hospital.controllers;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acelera.squad.four.hospital.models.Paciente;

import io.swagger.annotations.Api;

@RestController
@Api(value = "paciente")
@RequestMapping(path = "/v1")
@ExposesResourceFor(Paciente.class)
public class PacienteController {
}

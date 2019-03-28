package com.acelera.squad.four.hospital.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@TypeAlias("Leito")
@Document(collection = "leitos")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Leito{

    @Id
	@JsonProperty(access = Access.READ_ONLY)
	private String id;

    private String pacienteID;

    public Leito(){}

    public Leito(String paienteID){
        super();
        this.pacienteID = pacienteID;
    }
}
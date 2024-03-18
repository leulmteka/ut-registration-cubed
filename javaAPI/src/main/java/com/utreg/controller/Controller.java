package com.utreg.controller;

import com.utreg.model.Model;
import com.utreg.model.ModelService;
import com.utreg.professor.Professor;
import com.utreg.professor.ProfessorService;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api" )
public class Controller {

	private final ModelService modelService;
	private final ProfessorService professorService;
	public Controller(ModelService modelService, ProfessorService professorService) {
		this.modelService = modelService;
        this.professorService = professorService;

    }

	//TESTING ENDPOINT
	@GetMapping("/hello")
	public String hello() {
		try{
			Professor t = professorService.getRMPInfo("John Cole");
			return ""+ t.getDifficulty();
		} catch (Exception e){
			System.out.println("epic fail");

			return null;
		}
	}

	@GetMapping("/models")
	public List<Model> getModels() {
		return modelService.getModels();
	}

	@GetMapping("/model/{name}")
	public Model getModel(@PathVariable String name) {
		List<Model> models = modelService.getModels();
		for (Model model : models) {
			if (model.getName().equals(name)) {
				return model;
			}
		}
		return null; // or throw an exception
	}

	@PostMapping("/")
	public String createModel(@RequestBody Model newModel) {
		modelService.getModels().add(newModel);
		return "good";
	}

	@DeleteMapping("/model/{id}")
	public String deleteModel(@PathVariable String id) {
		List<Model> models = modelService.getModels();
		for(int i = 0; i < models.size(); i++){
			if(models.get(i).getName().equals(id)){
				models.remove(i);
				return "removed";
			}
		}
		return "null";
	}

	@GetMapping("/dbmodels")
	public List<Model> getDBmodels() {
		return modelService.getDBmodels();
	}

}
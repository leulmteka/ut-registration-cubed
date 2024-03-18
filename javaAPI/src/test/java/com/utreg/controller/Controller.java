package com.utreg.controller;

import com.utreg.model.Model;
import com.utreg.model.ModelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api" )
public class Controller {

	private final ModelService modelService;
	public Controller(ModelService modelService) {
		this.modelService = modelService;
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello, World!";
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
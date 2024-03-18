package com.utreg.model;
import com.utreg.model.Model;
import com.utreg.service.ModelEntity;
import com.utreg.service.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelService {

    private final List<Model> model = new ArrayList<>();
    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<Model> getModels() {
        return model;
    }

    public List<Model> getDBmodels() {
        List<ModelEntity> entities = modelRepository.findAll();
        List<Model> models = new ArrayList<>();
        for (ModelEntity entity : entities) {
            System.out.println(entity.getA());
        }
        return models;
    }



}
package main.controllers.assemblers;

import main.controllers.TaskController;
import main.models.Task;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskAssembler implements RepresentationModelAssembler<Task, EntityModel<Task>> {
    @Override
    public EntityModel<Task> toModel(Task entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(TaskController.class).one(entity.getId())).withSelfRel(),
                linkTo(methodOn(TaskController.class).all()).withRel("/tasks"));
    }
}

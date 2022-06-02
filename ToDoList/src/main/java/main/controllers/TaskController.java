package main.controllers;

import main.controllers.assemblers.TaskAssembler;
import main.controllers.exceptions.TaskNotFoundException;
import main.models.Status;
import main.models.Task;
import main.models.repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("tasks")
public class TaskController {

    private TaskRepository repository;
    private TaskAssembler assembler;

    public TaskController(TaskRepository repository, TaskAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Task>> all() {
        List<EntityModel<Task>> tasks = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(tasks,
                linkTo(methodOn(TaskController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Task> one(@PathVariable Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() ->new TaskNotFoundException(id));
        return assembler.toModel(task);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Task task) {
        EntityModel<Task> entityModel = assembler.toModel(repository.save(task));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Task newTask) {
        Task updatedTask = repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        updatedTask.setTitle(newTask.getTitle());
        updatedTask.setDescription(newTask.getDescription());
        updatedTask.setDeadline(newTask.getDeadline());
        EntityModel<Task> entityModel = assembler.toModel(repository.save(updatedTask));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

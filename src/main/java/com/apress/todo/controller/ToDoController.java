package com.apress.todo.controller;

import com.apress.todo.PropertiesConfiguration;
import com.apress.todo.domain.ToDo;
import com.apress.todo.domain.ToDoBuilder;
import com.apress.todo.repository.ToDoRepository;
import com.apress.todo.validation.ToDoValidationError;
import com.apress.todo.validation.ToDoValidationErrorBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class ToDoController {
	// inject via application.properties
	private PropertiesConfiguration app;

	@Autowired
	public void setApp(PropertiesConfiguration app) {
	    this.app = app;
	}
    
    private static Logger log = LoggerFactory.getLogger(ToDoController.class);
    private ToDoRepository toDoRepository;

    @Autowired
    public ToDoController(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }


    @GetMapping("/todo")
    @ApiOperation(value="returns all existing ToDo objects", notes="returns zero or more ToDo objects", response=Iterable.class)
    public ResponseEntity<Iterable<ToDo>> getToDos(){
        return ResponseEntity.ok(toDoRepository.findAll());
    }

    
    @GetMapping("/todo/{id}")
    @ApiOperation(value="returns specific ToDo object", notes="returns at most 1 ToDo object based on id", response=ToDo.class)
    public ResponseEntity<ToDo> getToDoById(@ApiParam(value="The ID of the ToDo object to retrieve", required=true) @PathVariable String id){
        Optional<ToDo> toDo =
                toDoRepository.findById(id);
        if(toDo.isPresent())
         return ResponseEntity.ok(toDo.get());

        return ResponseEntity.notFound().build();
    }

   
    @PatchMapping("/todo/{id}")
    @ApiOperation(value="returns specific ToDo object updated", notes="Update one or more elements of a ToDo object")
    public ResponseEntity<ToDo> setCompleted(@PathVariable String id){
        Optional<ToDo> toDo = toDoRepository.findById(id);
        if(!toDo.isPresent())
            return ResponseEntity.notFound().build();

        ToDo result = toDo.get();
        result.setCompleted(true);
        toDoRepository.save(result);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.ok().header("Location",location.toString()).build();
    }

   
    @RequestMapping(value="/todo", method = {RequestMethod.POST,RequestMethod.PUT})
    @ApiOperation(value="returns specific ToDo object created", notes="Creates a ToDo object")
    public ResponseEntity<?> createToDo(@Valid @RequestBody ToDo toDo, Errors errors){
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(e -> log.error(e.getObjectName() + " " + e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }

        ToDo result = toDoRepository.save(toDo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(result); //ResponseEntity.created(location).build();
    }

    
    @DeleteMapping("/todo/{id}")
    @ApiOperation(value="returns removed ToDo object", notes="Delete a ToDo object based on ID")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable String id){
        toDoRepository.delete(ToDoBuilder.create().withId(id).build());
        return ResponseEntity.noContent().build();
    }

    
    @DeleteMapping("/todo")
    @ApiOperation(value="returns removed ToDo object", notes="Delete a ToDo object based on object passed")
    public ResponseEntity<ToDo> deleteToDo(@RequestBody ToDo toDo){
        toDoRepository.delete(toDo);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        log.error(exception.getMessage());
        return new ToDoValidationError(exception.getMessage());
    }

}

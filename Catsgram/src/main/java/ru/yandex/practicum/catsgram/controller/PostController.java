package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.util.SortOrder;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "desc") String sort,
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {

        SortOrder sortOrder = SortOrder.from(sort);

        if (sortOrder == null) {
            throw new ConditionsNotMetException("Параметр sort должен иметь значение asc или desc");
        }

        return postService.findAll(sortOrder, from, size);
    }

    @GetMapping("/{postId}")
    public Optional<Post> findById(@PathVariable long postId) {
        return postService.findById(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}

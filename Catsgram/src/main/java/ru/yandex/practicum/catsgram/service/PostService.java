package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.util.SortOrder;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {

    private final UserService userService;

    public PostService(UserService userService) {
        this.userService = userService;
    }

    private final Map<Long, Post> posts = new HashMap<>();

    public Collection<Post> findAll(SortOrder sort, int from, int size) {

        if (size <= 0) {
            throw new ConditionsNotMetException("Размер выборки должен быть больше 0");
        }

        if (from < 0) {
            throw new ConditionsNotMetException("Количество пропущенных постов должно быть больше нуля");
        }

        Comparator<Post> comparator =
                Comparator.comparing(post -> post.getPostDate());

        if (sort == SortOrder.DESCENDING) {
            comparator = comparator.reversed();
        }
        return posts.values().stream()
                .sorted(comparator)
                .skip(from)
                .limit(size)
                .toList();
    }

    public Optional<Post> findById(long postId) {
        return Optional.ofNullable(posts.get(postId));
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException("Автор с id = " + post.getAuthorId() + " не найден");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());

        posts.put(post.getId(), post);
        return post;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Post update(Post newPost) {
        // проверяем необходимые условия
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }
}

package ru.practicum.repositories.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.category.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    Optional<Category> findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category c " +
            "SET c.name = ?2 " +
            "WHERE c.id = ?1")
    void patch(Long catId, String name);
}

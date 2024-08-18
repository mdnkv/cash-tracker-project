package dev.mednikov.cashtracker.categories.models;

import dev.mednikov.cashtracker.users.models.User;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "categories_category")
public class Category implements Comparable<Category> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @Column(name = "category_name", nullable = false)
    private String name;

    @Column(name = "category_description")
    private String description;

    private String color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(owner, category.owner)
                && Objects.equals(name, category.name)
                && Objects.equals(color, category.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, color);
    }

    @Override
    public int compareTo(Category o) {
        return this.getName().compareTo(o.getName());
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class CategoryBuilder {
        private Long id;
        private User owner;
        private String name;
        private String description;
        private String color;

        public CategoryBuilder() {
        }

        public CategoryBuilder(Category other) {
            this.id = other.id;
            this.owner = other.owner;
            this.name = other.name;
            this.description = other.description;
            this.color = other.color;
        }

        public static CategoryBuilder aCategory() {
            return new CategoryBuilder();
        }

        public CategoryBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public CategoryBuilder withOwner(User owner) {
            this.owner = owner;
            return this;
        }

        public CategoryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CategoryBuilder withColor(String color) {
            this.color = color;
            return this;
        }

        public Category build() {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setColor(color);
            category.owner = this.owner;
            category.id = this.id;
            return category;
        }
    }

}

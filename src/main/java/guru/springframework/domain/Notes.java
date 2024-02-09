package guru.springframework.domain;

import org.aspectj.weaver.ast.Not;

import javax.persistence.*;

@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Recipe recipe;
    private String recipeNotes;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getRecipeNotes() {
        return recipeNotes;
    }

    public void setRecipeNotes(String recipeNotes) {
        this.recipeNotes = recipeNotes;
    }
}

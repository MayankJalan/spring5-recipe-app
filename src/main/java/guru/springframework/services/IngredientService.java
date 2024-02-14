package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    IngredientCommand  saveIngredientCommand(IngredientCommand ingredientCommand);

    public void deleteRecipeIngredient(Long recipeId, Long ingredientId);
}

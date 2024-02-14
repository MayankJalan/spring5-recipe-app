package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
@Service
public class IngredientServiceImpl implements IngredientService{
    private RecipeRepository recipeRepository;
    private IngredientToIngredientCommand ingredientToIngredientCommand;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    private IngredientCommandToIngredient ingredientCommandToIngredient;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, UnitOfMeasureRepository unitOfMeasureRepository, IngredientCommandToIngredient ingredientCommandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

//        Optional<Recipe> recipeOptional=recipeRepository.findById(recipeId);
//        IngredientCommand ingredientCommand=null;
//        if(recipeOptional.isPresent()){
//            ingredientCommand= recipeOptional.get().getIngredients().stream().filter(i -> i.getId()==ingredientId).map(i-> ingredientToIngredientCommand.convert(i)).findFirst().get();
//        }
//        return ingredientCommand;

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map( ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

        return ingredientCommandOptional.get();
    }



    @Transactional
    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

        if(!recipeOptional.isPresent()) {
            return new IngredientCommand();
        }
        else{
            Recipe recipe=recipeOptional.get();
            Optional<Ingredient> ingredientOptional=recipe.getIngredients().stream().filter(i -> i.getId()==ingredientCommand.getId()).findFirst();
            if(ingredientOptional.isPresent()){
                Ingredient ingredient=ingredientOptional.get();
                ingredient.setAmount(ingredientCommand.getAmount());
                ingredient.setDescription(ingredientCommand.getDescription());
                ingredient.setUnitOfMeasure(unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId()).orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));
            }
            else{
                recipe.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            return ingredientToIngredientCommand.convert(savedRecipe.getIngredients().stream().
                    filter(recipeIngredients -> recipeIngredients.getId()==ingredientCommand.getId())
                    .findFirst().get());

        }
    }

    @Override
    public void deleteRecipeIngredient(Long recipeId, Long ingredientId) {

        Optional<Recipe> recipeOptional=recipeRepository.findById(recipeId);

        if(!recipeOptional.isPresent()) {
            return ;
        }
        else {
            Recipe recipe = recipeOptional.get();
            Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream().filter(i -> i.getId() == ingredientId).findFirst();
            if (ingredientOptional.isPresent()) {
                Ingredient ingredient = ingredientOptional.get();
                ingredient.setRecipe(null);
                recipe.getIngredients().remove(ingredient);
                recipeRepository.save(recipe);
            }
        }


    }
}

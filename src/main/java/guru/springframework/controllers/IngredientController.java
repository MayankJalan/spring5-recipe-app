package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IngredientController {
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }
    @RequestMapping("/recipe/{id}/ingredients")
    String getIngredients(@PathVariable String id, Model model){
        RecipeCommand recipeCommand =recipeService.findCommandById(Long.valueOf(id));
        model.addAttribute("recipe",recipeCommand);
        return "/recipe/ingredients/list";

    }

    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId , Model model){
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId),Long.valueOf(ingredientId)));
        return "/recipe/ingredients/show";
    }

    @RequestMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id, Model model){
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));

        model.addAttribute("uomList", unitOfMeasureService.findAllUoms());
        return "/recipe/ingredients/ingredientform";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {

        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";

    }
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteRecipeIngredient(@PathVariable String recipeId,
                                         @PathVariable String id, Model model){
        ingredientService.deleteRecipeIngredient(Long.valueOf(recipeId), Long.valueOf(id));
        return "redirect:/recipe/"+recipeId+"/ingredients";
    }




}

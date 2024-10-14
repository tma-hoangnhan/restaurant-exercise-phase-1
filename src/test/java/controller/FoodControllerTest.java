package controller;

import org.example.controller.FoodController;
import org.example.model.food.Breakfast;
import org.example.model.food.Dinner;
import org.example.model.food.Food;
import org.example.model.food.Lunch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FoodControllerTest {
    FoodController foodController;

    @Before
    public void setUp() {
        foodController = FoodController.getFoodController();
    }

    @Test
    public void testSave_objectIsBreakfast() {
        String objectType = "Breakfast";
        Food expected = new Breakfast(99, "New Breakfast", "New Breakfast", "New Breakfast", 30);
        foodController.save(expected);

        Food actual = foodController.getFoodMenu().getBreakfasts().get(expected.getId());
        Assert.assertNotNull(actual);
        Assert.assertEquals(objectType, actual.getClass().getSimpleName());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getImg(), actual.getDescription());
        Assert.assertEquals(0, Double.compare(expected.getPrice(), actual.getPrice()));
    }

    @Test
    public void testSave_objectIsLunch() {
        String objectType = "Lunch";
        Food expected = new Lunch(99, "Lunch", "Lunch", "Lunch", 30);
        foodController.save(expected);

        Food actual = foodController.getFoodMenu().getLunches().get(expected.getId());
        Assert.assertNotNull(actual);
        Assert.assertEquals(objectType, actual.getClass().getSimpleName());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getImg(), actual.getDescription());
        Assert.assertEquals(0, Double.compare(expected.getPrice(), actual.getPrice()));
    }

    @Test
    public void testSave_objectIsDinner() {
        String objectType = "Dinner";
        Food expected = new Dinner(99, "Dinner", "Dinner", "Dinner", 30);
        foodController.save(expected);

        Food actual = foodController.getFoodMenu().getDinners().get(expected.getId());
        Assert.assertNotNull(actual);
        Assert.assertEquals(objectType, actual.getClass().getSimpleName());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getImg(), actual.getDescription());
        Assert.assertEquals(0, Double.compare(expected.getPrice(), actual.getPrice()));
    }

    @Test
    public void testDelete() {
        Food expected = new Breakfast(99, "New Breakfast", "New Breakfast", "New Breakfast", 30);
        foodController.delete(expected);

        Food actual = foodController.getFoodMenu().getBreakfasts().get(expected.getId());
        Assert.assertNull(actual);
    }

    @Test
    public void testFindById_found() {
        Food expected = new Breakfast(99, "New Breakfast", "New Breakfast", "New Breakfast", 30);
        foodController.save(expected);

        Food actual = foodController.findById(expected.getId());
        Assert.assertNotNull(actual);

        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getImg(), actual.getDescription());
        Assert.assertEquals(0, Double.compare(expected.getPrice(), actual.getPrice()));
    }

    @Test
    public void testFindById_notFound() {
        Food actual = foodController.findById(10000);
        Assert.assertNull(actual);
    }

}

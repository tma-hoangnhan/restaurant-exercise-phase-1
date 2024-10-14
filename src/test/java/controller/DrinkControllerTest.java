package controller;

import org.example.controller.DrinkController;
import org.example.model.drink.Alcohol;
import org.example.model.drink.Drink;
import org.example.model.drink.SoftDrink;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DrinkControllerTest {
    DrinkController drinkController;

    @Before
    public void setUp() {
        drinkController = DrinkController.getDrinkController();
    }

    @Test
    public void testSave_objectIsSoftDrink() {
        String objectType = "SoftDrink";
        Drink expected = new SoftDrink(99, "SoftDrink", "SoftDrink", "SoftDrink", 20);
        drinkController.save(expected);

        Drink actual = drinkController.getDrinkMenu().getSoftDrinks().get(expected.getId());
        Assert.assertNotNull(actual);

        Assert.assertEquals(objectType, actual.getClass().getSimpleName());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getImg(), actual.getDescription());
        Assert.assertEquals(0, Double.compare(expected.getPrice(), actual.getPrice()));
    }

    @Test
    public void testSave_objectIsAlcohol() {
        String objectType = "Alcohol";
        Drink expected = new Alcohol(99, "Alcohol", "Alcohol", "Alcohol", 20);
        drinkController.save(expected);

        Drink actual = drinkController.getDrinkMenu().getAlcohols().get(expected.getId());
        Assert.assertNotNull(actual);

        Assert.assertEquals(objectType, actual.getClass().getSimpleName());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getImg(), actual.getDescription());
        Assert.assertEquals(0, Double.compare(expected.getPrice(), actual.getPrice()));
    }

    @Test
    public void testDelete() {
        Drink expected = new SoftDrink(99, "Test", "Test", "Test", 20);
        drinkController.save(expected);
        drinkController.delete(expected);

        Drink result = drinkController.getDrinkMenu().getSoftDrinks().get(expected.getId());
        Assert.assertNull(result);
    }

    @Test
    public void testFindById_found() {
        Drink expected = new SoftDrink(99, "Test", "Test", "Test", 20);
        drinkController.save(expected);

        Drink actual = drinkController.findById(expected.getId());
        Assert.assertNotNull(actual);

        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        Assert.assertEquals(expected.getImg(), actual.getDescription());
        Assert.assertEquals(0, Double.compare(expected.getPrice(), actual.getPrice()));
    }

    @Test
    public void testFindById_notFound() {
        Drink actual = drinkController.findById(1000);
        Assert.assertNull(actual);
    }
}
